package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.robot.RobotMap;
import team.gif.robot.commands.drive.DriveTeleOp;

public class Drivetrain extends Subsystem {

    private static Drivetrain instance;

    private TalonSRX leftMaster, rightMaster;
    private VictorSPX leftSlave, rightSlave;

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

    public void setLeft(double leftPercent) {

    }

    public void setRight(double rightPercent) {

    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveTeleOp());
    }
}
