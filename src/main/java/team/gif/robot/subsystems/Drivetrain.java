package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import team.gif.lib.Odometry;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;
import team.gif.robot.commands.drivetrain.DriveTeleOp;

public class Drivetrain extends Subsystem {

    private static Drivetrain instance;

    private final CANSparkMax leftMaster, leftSlave, rightMaster, rightSlave;
    private final PigeonIMU pigeon;
    private final TalonSRX leftEncoderTalon, rightEncoderTalon;
    private Odometry odometry;

    private Drivetrain() {
        leftMaster = new CANSparkMax(RobotMap.DRIVE_LEFT_MASTER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        leftSlave = new CANSparkMax(RobotMap.DRIVE_LEFT_SLAVE_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightMaster = new CANSparkMax(RobotMap.DRIVE_RIGHT_MASTER_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightSlave = new CANSparkMax(RobotMap.DRIVE_RIGHT_SLAVE_ID, CANSparkMaxLowLevel.MotorType.kBrushless);
        configNeo(leftMaster);
        configNeo(leftSlave);
        configNeo(rightMaster);
        configNeo(rightSlave);

        leftSlave.setIdleMode(CANSparkMax.IdleMode.kCoast);
        rightSlave.setIdleMode(CANSparkMax.IdleMode.kCoast);

        pigeon = new PigeonIMU(RobotMap.PIGEON_ID);

        leftEncoderTalon = Claw.getInstance().getDriveEncoderTalon();
        rightEncoderTalon = Climber.getInstance().getDriveEncoderTalon();
        configDriveEncoder(leftEncoderTalon);
        configDriveEncoder(rightEncoderTalon);
        rightEncoderTalon.setSensorPhase(false); // P: true C: false

        leftMaster.setInverted(false);
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
     * Sets the percent voltage output of each side of the drivetrain.
     *
     * @param left output percent (-1.0 to +1.0)
     * @param right output percent (-1.0 to +1.0)
     */
    public void setOutputs(double left, double right) {
        leftMaster.set(left);
        rightMaster.set(right);
    }

    public void setYaw(double yaw) {
        pigeon.setYaw(yaw);
    }

    public void setBrakeMode(boolean on) {
        leftMaster.setIdleMode(on ? CANSparkMax.IdleMode.kBrake : CANSparkMax.IdleMode.kCoast);
        rightMaster.setIdleMode(on ? CANSparkMax.IdleMode.kBrake : CANSparkMax.IdleMode.kCoast);
    }

    public void setRampRate(double seconds) {
        leftMaster.setOpenLoopRampRate(seconds);
        rightMaster.setOpenLoopRampRate(seconds);
        leftSlave.setOpenLoopRampRate(seconds);
        rightSlave.setOpenLoopRampRate(seconds);
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

    public double[] getOutputVoltage() {
        return new double[]{ leftMaster.getAppliedOutput() * leftMaster.getBusVoltage(),
        rightMaster.getAppliedOutput() * rightMaster.getBusVoltage() };
    }
    /**
     * @return array containing yaw[0], pitch[1], and roll[2] in degrees
     */
    public double[] getYawPitchRoll() {
        double[] ypr_deg = new double[3];
        pigeon.getYawPitchRoll(ypr_deg);
        return ypr_deg;
    }

    /**
     * @return heading in degrees
     */
    public double getHeadingDegrees() {
        return getYawPitchRoll()[0];
    }

    /**
     * @return heading in radians
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
     * @param spark {@link CANSparkMax} to configure
     */
    private void configNeo(CANSparkMax spark) {
        spark.setIdleMode(CANSparkMax.IdleMode.kBrake);
        spark.enableVoltageCompensation(12.0);
        spark.setSmartCurrentLimit(80);
        spark.setOpenLoopRampRate(0.15);
    }

    /**
     * Configures TalonSRX's to act as receivers for the drivetrain's encoders.
     *
     * @param talon {@link TalonSRX} to configure
     */
    private void configDriveEncoder(TalonSRX talon) {
        talon.configFactoryDefault();
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 5);
        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveTeleOp());
    }
}
