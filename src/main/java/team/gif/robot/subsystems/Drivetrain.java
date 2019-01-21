package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.lib.Odometry;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;
import team.gif.robot.commands.drivetrain.DriveTeleOp;

public class Drivetrain extends Subsystem {

    private static Drivetrain instance;

    private final CANSparkMax leftMaster, leftSlave, rightMaster, rightSlave;
    private final TalonSRX leftEncoderTalon, rightEncoderTalon;
    private Odometry odometry;

    private Drivetrain() {
        leftMaster = configNeo(RobotMap.LEFT_MASTER_ID);
        leftSlave = configNeo(RobotMap.LEFT_SLAVE_ID);
        rightMaster = configNeo(RobotMap.RIGHT_MASTER_ID);
        rightSlave = configNeo(RobotMap.RIGHT_SLAVE_ID);

        leftEncoderTalon = Elevator.getInstance().getDriveEncoderTalon();
        rightEncoderTalon = Claw.getInstance().getDriveEncoderTalon();

        rightMaster.setInverted(true);

        leftSlave.follow(leftMaster, false);
        rightSlave.follow(rightMaster, false);
    }

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
        }
        return instance;
    }

    /**
     * Sets the percent voltage output of both sides of the drivetrain.
     *
     * @param left output percent (-1.0 to +1.0)
     * @param right output percent (-1.0 to +1.0)
     */
    public void setOutputs(double left, double right) {
        leftMaster.set(left);
        rightMaster.set(right);
    }

    /**
     * @return native encoder position in ticks
     */
    public int getLeftPosTicks() {
        return leftEncoderTalon.getSelectedSensorPosition();
    }

    /**
     * @return native encoder position in ticks
     */
    public int getRightPosTicks() {
        return rightEncoderTalon.getSelectedSensorPosition();
    }

    /**
     * @return encoder position in inches
     */
    public double getLeftPosInches() {
        return getLeftPosTicks() * Constants.Drivetrain.TICKS_TO_INCHES;
    }

    /**
     * @return encoder position in inches
     */
    public double getRightPosInches() {
        return getRightPosTicks() * Constants.Drivetrain.TICKS_TO_INCHES;
    }

    /**
     * @return encoder velocity in ticks/s
     */
    public double getLeftVelTPS() {
        return leftEncoderTalon.getSelectedSensorVelocity() * 10.0;
    }

    /**
     * @return encoder velocity in ticks/s
     */
    public double getRightVelTPS() {
        return rightEncoderTalon.getSelectedSensorVelocity() * 10.0;
    }

    /**
     * @return encoder velocity in rad/s
     */
    public double getLeftVelRPS() {
        return getLeftVelTPS() * Constants.Drivetrain.TPS_TO_RPS;
    }

    /**
     * @return encoder velocity in rad/s
     */
    public double getRightVelRPS() {
        return getRightVelTPS() * Constants.Drivetrain.TPS_TO_RPS;
    }

    /**
     * @return array containing accumulated yaw[0], pitch[1], and roll[2] in degrees
     */
    public double[] getYawPitchRoll() {
        return Climber.getInstance().getYawPitchRoll();
    }

    /**
     * @return accumulated heading in degrees
     */
    public double getHeadingDegrees() {
        return getYawPitchRoll()[0];
    }

    /**
     * @return accumulated heading in radians
     */
    public double getHeadingRadians() {
        return Math.toRadians(getHeadingDegrees());
    }

    /**
     * Initializes the odometry subprocess and begins estimating robot pose.
     */
    public void beginOdometry() {
        odometry = Odometry.getInstance();
        odometry.start();
    }

    /**
     * Configures CANSparkMax's for use with NEO motors in a drivetrain.
     *
     * @param id the CAN id of the spark
     * @return a configured CANSparkMax
     */
    private CANSparkMax configNeo(int id) {
        CANSparkMax spark = new CANSparkMax(id, CANSparkMaxLowLevel.MotorType.kBrushless);
        spark.setIdleMode(CANSparkMax.IdleMode.kBrake);
        spark.setSmartCurrentLimit(80);
        spark.setRampRate(0.0);
        return spark;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveTeleOp());
    }
}
