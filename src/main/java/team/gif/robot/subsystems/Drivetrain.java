package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.lib.Odometry;
import team.gif.robot.RobotMap;
import team.gif.robot.commands.drivetrain.DriveTeleOp;

public class Drivetrain extends Subsystem {

    private static Drivetrain instance;

    private final TalonSRX leftMaster, rightMaster;
    private final VictorSPX leftSlave, rightSlave;

    private Odometry odometry = Odometry.getInstance();

    private Drivetrain() {
        leftMaster = new TalonSRX(RobotMap.LEFT_MASTER_ID);
        leftSlave = new VictorSPX(RobotMap.LEFT_SLAVE_ID);
        rightMaster = new TalonSRX(RobotMap.RIGHT_MASTER_ID);
        rightSlave = new VictorSPX(RobotMap.RIGHT_SLAVE_ID);

        leftMaster.configFactoryDefault();
        leftSlave.configFactoryDefault();
        rightMaster.configFactoryDefault();
        rightSlave.configFactoryDefault();
        leftMaster.setNeutralMode(NeutralMode.Brake);
        leftSlave.setNeutralMode(NeutralMode.Brake);
        rightMaster.setNeutralMode(NeutralMode.Brake);
        rightSlave.setNeutralMode(NeutralMode.Brake);

        leftSlave.set(ControlMode.Follower, RobotMap.LEFT_MASTER_ID);
        rightSlave.set(ControlMode.Follower, RobotMap.RIGHT_MASTER_ID);
    }

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
        }
        return instance;
    }

    /**
     * Sets the percent output of both sides of the drivetrain.
     *
     * @param left output percent (-1.0 to +1.0)
     * @param right output percent (-1.0 to +1.0)
     */
    public void setOutput(double left, double right) {
        // do a thing
    }

    /**
     * Accumulated position of the left side of the drivetrain.
     *
     * @return left position in ticks
     */
    public int getLeftEncPosition() {
        return 0;
    }

    /**
     * Accumulated position of the right side of the drivetrain.
     *
     * @return right position in ticks
     */
    public int getRightEncPosition() {
        return 0;
    }

    public double getLeftPosition() {
        return 0.0;
    }

    public double getRightPosition() {
        return 0.0;
    }

    /**
     * Accumulated heading of the robot.
     *
     * @return heading in degrees
     */
    public double getHeading() {
        return 0.0;
    }

    public void resetOdometry() {
        odometry.reset();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveTeleOp());
    }
}
