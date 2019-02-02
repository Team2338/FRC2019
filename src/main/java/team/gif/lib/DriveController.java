package team.gif.lib;

import team.gif.robot.Constants;

/**
 * A class for driving differential drive/skid-steer drive platforms such as the Kit of Parts drive
 * base, "tank drive", or West Coast Drive.
 *
 * <p>These drive bases typically have drop-center / skid-steer with two or more wheels per side
 * (e.g., 6WD or 8WD). This class takes a SpeedController per side. For four and
 * six motor drivetrains, construct and pass in {@link edu.wpi.first.wpilibj.SpeedControllerGroup}
 * instances as follows.
 *
 * <p>Four motor drivetrain:
 * <pre><code>
 * public class Robot {
 *   Spark m_frontLeft = new Spark(1);
 *   Spark m_rearLeft = new Spark(2);
 *   SpeedControllerGroup m_left = new SpeedControllerGroup(m_frontLeft, m_rearLeft);
 *
 *   Spark m_frontRight = new Spark(3);
 *   Spark m_rearRight = new Spark(4);
 *   SpeedControllerGroup m_right = new SpeedControllerGroup(m_frontRight, m_rearRight);
 *
 *   DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);
 * }
 * </code></pre>
 *
 * <p>Six motor drivetrain:
 * <pre><code>
 * public class Robot {
 *   Spark m_frontLeft = new Spark(1);
 *   Spark m_midLeft = new Spark(2);
 *   Spark m_rearLeft = new Spark(3);
 *   SpeedControllerGroup m_left = new SpeedControllerGroup(m_frontLeft, m_midLeft, m_rearLeft);
 *
 *   Spark m_frontRight = new Spark(4);
 *   Spark m_midRight = new Spark(5);
 *   Spark m_rearRight = new Spark(6);
 *   SpeedControllerGroup m_right = new SpeedControllerGroup(m_frontRight, m_midRight, m_rearRight);
 *
 *   DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);
 * }
 * </code></pre>
 *
 * <p>A differential drive robot has left and right wheels separated by an arbitrary width.
 *
 * <p>Drive base diagram:
 * <pre>
 * |_______|
 * | |   | |
 *   |   |
 * |_|___|_|
 * |       |
 * </pre>
 *
 * <p>Each drive() function provides different inverse kinematic relations for a differential drive
 * robot. Motor outputs for the right side are negated, so motor direction inversion by the user is
 * usually unnecessary.
 *
 * <p>This library uses the NED axes convention (North-East-Down as external reference in the world
 * frame): http://www.nuclearprojects.com/ins/images/axis_big.png.
 *
 * <p>The positive X axis points ahead, the positive Y axis points right, and the positive Z axis
 * points down. Rotations follow the right-hand rule, so clockwise rotation around the Z axis is
 * positive.
 */
public class DriveController {

    private double deadband = Constants.Drivetrain.INPUT_DEADBAND;
    private double quickStopThreshold = Constants.Drivetrain.QUICK_STOP_THRESHOLD;
    private double quickStopAlpha = Constants.Drivetrain.QUICK_STOP_ALPHA;
    private double quickStopAccumulator;

    /**
     * Construct a DriveController.
     */
    public DriveController() {

    }

    /**
     * Arcade drive method for differential drive platform.
     * The calculated values will be squared to decrease sensitivity at low speeds.
     *
     * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                  positive.
     */
    @SuppressWarnings("ParameterName")
    public double[] arcadeDrive(double xSpeed, double zRotation) {
        return arcadeDrive(xSpeed, zRotation, true);
    }

    /**
     * Arcade drive method for differential drive platform.
     *
     * @param xSpeed        The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param zRotation     The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                      positive.
     * @param squareInputs If set, decreases the input sensitivity at low speeds.
     */
    @SuppressWarnings("ParameterName")
    public double[] arcadeDrive(double xSpeed, double zRotation, boolean squareInputs) {
        xSpeed = limit(xSpeed);
        xSpeed = applyDeadband(xSpeed, deadband);

        zRotation = limit(zRotation);
        zRotation = applyDeadband(zRotation, deadband);

        // Square the inputs (while preserving the sign) to increase fine control
        // while permitting full power.
        if (squareInputs) {
            xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
            zRotation = Math.copySign(zRotation * zRotation, zRotation);
        }

        double leftMotorOutput;
        double rightMotorOutput;

        double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

        if (xSpeed >= 0.0) {
            // First quadrant, else second quadrant
            if (zRotation >= 0.0) {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            } else {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            }
        } else {
            // Third quadrant, else fourth quadrant
            if (zRotation >= 0.0) {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            } else {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            }
        }

        return new double[] {limit(leftMotorOutput), limit(rightMotorOutput)};
    }

    /**
     * Curvature drive method for differential drive platform.
     *
     * <p>The rotation argument controls the curvature of the robot's path rather than its rate of
     * heading change. This makes the robot more controllable at high speeds. Also handles the
     * robot's quick turn functionality - "quick turn" overrides constant-curvature turning for
     * turn-in-place maneuvers.
     *
     * @param magnitude      The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param rotation   The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                    positive.
     * @param quickTurn If set, overrides constant-curvature turning for
     *                    turn-in-place maneuvers.
     */
    @SuppressWarnings({"ParameterName", "PMD.CyclomaticComplexity"})
    public double[] curvatureDrive(double magnitude, double rotation, boolean quickTurn) {
        magnitude = limit(magnitude);
        magnitude = applyDeadband(magnitude, deadband);

        rotation = limit(rotation);
        rotation = applyDeadband(rotation, deadband);

        double angularPower;
        boolean overPower;

        if (quickTurn) {
            if (Math.abs(magnitude) < quickStopThreshold) {
                quickStopAccumulator = (1 - quickStopAlpha) * quickStopAccumulator
                        + quickStopAlpha * limit(rotation) * 2;
            }
            overPower = true;
            angularPower = rotation;
        } else {
            overPower = false;
            angularPower = Math.abs(magnitude) * rotation - quickStopAccumulator;

            if (quickStopAccumulator > 1) {
                quickStopAccumulator -= 1;
            } else if (quickStopAccumulator < -1) {
                quickStopAccumulator += 1;
            } else {
                quickStopAccumulator = 0.0;
            }
        }

        double leftMotorOutput = magnitude + angularPower;
        double rightMotorOutput = magnitude - angularPower;

        // If rotation is overpowered, reduce both outputs to within acceptable range
        if (overPower) {
            if (leftMotorOutput > 1.0) {
                rightMotorOutput -= leftMotorOutput - 1.0;
                leftMotorOutput = 1.0;
            } else if (rightMotorOutput > 1.0) {
                leftMotorOutput -= rightMotorOutput - 1.0;
                rightMotorOutput = 1.0;
            } else if (leftMotorOutput < -1.0) {
                rightMotorOutput -= leftMotorOutput + 1.0;
                leftMotorOutput = -1.0;
            } else if (rightMotorOutput < -1.0) {
                leftMotorOutput -= rightMotorOutput + 1.0;
                rightMotorOutput = -1.0;
            }
        }

        // Normalize the wheel speeds
        double maxMagnitude = Math.max(Math.abs(leftMotorOutput), Math.abs(rightMotorOutput));
        if (maxMagnitude > 1.0) {
            leftMotorOutput /= maxMagnitude;
            rightMotorOutput /= maxMagnitude;
        }

        return new double[] {limit(leftMotorOutput), limit(rightMotorOutput)};
    }

    /**
     * Tank drive method for differential drive platform.
     * The calculated values will be squared to decrease sensitivity at low speeds.
     *
     * @param leftSpeed  The robot's left side speed along the X axis [-1.0..1.0]. Forward is
     *                   positive.
     * @param rightSpeed The robot's right side speed along the X axis [-1.0..1.0]. Forward is
     *                   positive.
     */
    public double[] tankDrive(double leftSpeed, double rightSpeed) {
        return tankDrive(leftSpeed, rightSpeed, true);
    }

    /**
     * Tank drive method for differential drive platform.
     *
     * @param leftSpeed     The robot left side's speed along the X axis [-1.0..1.0]. Forward is
     *                      positive.
     * @param rightSpeed    The robot right side's speed along the X axis [-1.0..1.0]. Forward is
     *                      positive.
     * @param squareInputs If set, decreases the input sensitivity at low speeds.
     */
    public double[] tankDrive(double leftSpeed, double rightSpeed, boolean squareInputs) {
        leftSpeed = limit(leftSpeed);
        leftSpeed = applyDeadband(leftSpeed, deadband);

        rightSpeed = limit(rightSpeed);
        rightSpeed = applyDeadband(rightSpeed, deadband);

        // Square the inputs (while preserving the sign) to increase fine control
        // while permitting full power.
        if (squareInputs) {
            leftSpeed = Math.copySign(leftSpeed * leftSpeed, leftSpeed);
            rightSpeed = Math.copySign(rightSpeed * rightSpeed, rightSpeed);
        }

        return new double[] {leftSpeed, rightSpeed};
    }

    /**
     * Sets the QuickStop speed threshold in curvature drive.
     *
     * <p>QuickStop compensates for the robot's moment of inertia when stopping after a QuickTurn.
     *
     * <p>While QuickTurn is enabled, the QuickStop accumulator takes on the rotation rate value
     * outputted by the low-pass filter when the robot's speed along the X axis is below the
     * threshold. When QuickTurn is disabled, the accumulator's value is applied against the computed
     * angular power request to slow the robot's rotation.
     *
     * @param threshold X speed below which quick stop accumulator will receive rotation rate values
     *                  [0..1.0].
     */
    public void setQuickStopThreshold(double threshold) {
        quickStopThreshold = threshold;
    }

    /**
     * Sets the low-pass filter gain for QuickStop in curvature drive.
     *
     * <p>The low-pass filter filters incoming rotation rate commands to smooth out high frequency
     * changes.
     *
     * @param alpha Low-pass filter gain [0.0..2.0]. Smaller values result in slower output changes.
     *              Values between 1.0 and 2.0 result in output oscillation. Values below 0.0 and
     *              above 2.0 are unstable.
     */
    public void setQuickStopAlpha(double alpha) {
        quickStopAlpha = alpha;
    }

    /**
     * Limit motor values to the -1.0 to +1.0 range.
     */
    private double limit(double value) {
        if (value > 1.0) {
            return 1.0;
        }
        if (value < -1.0) {
            return -1.0;
        }
        return value;
    }

    /**
     * Returns 0.0 if the given value is within the specified range around zero. The remaining range
     * between the deadband and 1.0 is scaled from 0.0 to 1.0.
     *
     * @param value    value to clip
     * @param deadband range around zero
     */
    private double applyDeadband(double value, double deadband) {
        if (Math.abs(value) > deadband) {
            if (value > 0.0) {
                return (value - deadband) / (1.0 - deadband);
            } else {
                return (value + deadband) / (1.0 - deadband);
            }
        } else {
            return 0.0;
        }
    }
}
