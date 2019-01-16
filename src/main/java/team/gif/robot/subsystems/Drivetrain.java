package team.gif.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxFrames;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.lib.Odometry;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;
import team.gif.robot.commands.drivetrain.DriveTeleOp;

public class Drivetrain extends Subsystem {

    private static Drivetrain instance;

    private final CANSparkMax leftMaster, leftSlave, rightMaster, rightSlave;
    private Odometry odometry;

    private Drivetrain() {
        leftMaster = configNeo(RobotMap.LEFT_MASTER_ID);
        leftSlave = configNeo(RobotMap.LEFT_SLAVE_ID);
        rightMaster = configNeo(RobotMap.RIGHT_MASTER_ID);
        rightSlave = configNeo(RobotMap.RIGHT_SLAVE_ID);

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
    public int getLeftEncPosition() {
        return 0;
    }

    /**
     * @return native encoder position in ticks
     */
    public int getRightEncPosition() {
        return 0;
    }

    /**
     * @return encoder position in inches
     */
    public double getLeftPosition() {
        return getLeftEncPosition() * Constants.Drivetrain.TICKS_TO_INCHES;
    }

    /**
     * @return encoder position in inches
     */
    public double getRightPosition() {
        return getRightEncPosition() * Constants.Drivetrain.TICKS_TO_INCHES;
    }

    /**
     * @return native encoder velocity in ticks/100ms
     */
    public double getLeftEncVelocity() {
        return 0.0;
    }

    /**
     * @return native encoder velocity in ticks/100ms
     */
    public double getRightEncVelocity() {
        return 0.0;
    }

    /**
     * @return encoder velocity in rad/s
     */
    public double getLeftVelocity() {
        return getLeftEncVelocity() * Constants.Drivetrain.TICKS_PER_100MS_TO_RADS_PER_S;
    }

    /**
     * @return encoder velocity in rad/s
     */
    public double getRightVelocity() {
        return getRightEncVelocity() * Constants.Drivetrain.TICKS_PER_100MS_TO_RADS_PER_S;
    }

    /**
     * @return motor velocity in rpm
     */
    public double getLeftMotorVelocity() {
        return leftMaster.getEncoder().getVelocity();
    }

    /**
     * @return motor velocity in rpm
     */
    public double getRightMotorVelocity() {
        return rightMaster.getEncoder().getVelocity();
    }

    /**
     * @return accumulated heading in degrees
     */
    public double getHeading() {
        return 0.0;
    }

    public void beginOdometry() {
        odometry = Odometry.getInstance();
        odometry.start();
    }

    private CANSparkMax configNeo(int id) {
        CANSparkMax spark = new CANSparkMax(id, CANSparkMaxLowLevel.MotorType.kBrushless);
        spark.setIdleMode(CANSparkMax.IdleMode.kBrake);
        return spark;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveTeleOp());
    }
}
