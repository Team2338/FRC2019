package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.lib.VariableRateSolenoid;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;

public class Climber extends Subsystem {

    private static Climber instance;

    private final VariableRateSolenoid frontLeft, rearLeft, frontRight, rearRight;
    private final TalonSRX climbDrive;
    private final PigeonIMU imu;

    private Climber() {
        frontLeft = new VariableRateSolenoid(RobotMap.FRONT_LEFT_FORWARD_ID, RobotMap.FRONT_LEFT_REVERSE_ID);
        rearLeft = new VariableRateSolenoid(RobotMap.REAR_LEFT_FORWARD_ID, RobotMap.REAR_LEFT_REVERSE_ID);
        frontRight = new VariableRateSolenoid(RobotMap.FRONT_RIGHT_FORWARD_ID, RobotMap.FRONT_RIGHT_REVERSE_ID);
        rearRight = new VariableRateSolenoid(RobotMap.REAR_RIGHT_FORWARD_ID, RobotMap.REAR_RIGHT_REVERSE_ID);

        climbDrive = new TalonSRX(RobotMap.CLIMB_DRIVE_ID);
        imu = new PigeonIMU(RobotMap.PIGEON_ID);
        climbDrive.configFactoryDefault();
        imu.configFactoryDefault();
    }

    public static Climber getInstance() {
        if (instance == null) {
            instance = new Climber();
        }
        return instance;
    }

    /**
     * Sets the duty cycle for each climber piston. This method must be called in a loop to update {@param time}.
     *
     * @param frontLeft output percent (-1.0 to +1.0)
     * @param rearLeft output percent (-1.0 to +1.0)
     * @param frontRight output percent (-1.0 to +1.0)
     * @param rearRight output percent (-1.0 to +1.0)
     * @param time system time in seconds (see {@link Timer#getFPGATimestamp()})
     */
    public void setPistons(double frontLeft, double rearLeft, double frontRight, double rearRight, double time) {
        this.frontLeft.setPercentOutput(frontLeft, Constants.Climber.VARIABLE_RATE_PISTON_PERIOD, time);
        this.rearLeft.setPercentOutput(rearLeft, Constants.Climber.VARIABLE_RATE_PISTON_PERIOD, time);
        this.frontRight.setPercentOutput(frontRight, Constants.Climber.VARIABLE_RATE_PISTON_PERIOD, time);
        this.rearRight.setPercentOutput(rearRight, Constants.Climber.VARIABLE_RATE_PISTON_PERIOD, time);
    }

    /**
     * Sets the percent voltage output for the rear wheel climb drive.
     *
     * @param percent output percent (-1.0 to +1.0)
     */
    public void setClimbDrive(double percent) {
        climbDrive.set(ControlMode.PercentOutput, percent);
    }

    /**
     * Calculates balance error for each corner of the drivetrain. Positive values are above the CG, negative values
     * are below. These are calculated by summing the relevant tilts for each corner. This means that each error has
     * a theoretical range of +180 to -180 degrees.
     *
     * @param ypr_deg array containing rotation on all axes
     * @return errors for front left [0], rear left [1], front right [2], and rear right [3] in degrees
     */
    public double[] getBalanceError(double[] ypr_deg) {
        double pitch = ypr_deg[1] - 0.395; // TODO: Make me a constant!
        double roll = ypr_deg[2] - 1.27;
        double frontLeftError = pitch + roll;
        double rearLeftError = -pitch + roll;
        double frontRightError = pitch - roll;
        double rearRightError = -pitch - roll;
        return new double[] {frontLeftError, rearLeftError, frontRightError, rearRightError};
    }

    TalonSRX getDriveEncoderTalon() {
        return climbDrive;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
