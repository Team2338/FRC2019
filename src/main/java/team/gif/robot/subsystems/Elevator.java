package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.robot.RobotMap;

public class Elevator extends Subsystem {

    private static Elevator instance;

    private final TalonSRX liftMaster, liftSlave;

    private Elevator() {
        liftMaster = new TalonSRX(RobotMap.LIFT_MASTER_ID);
        liftSlave = new TalonSRX(RobotMap.LIFT_SLAVE_ID);

        liftMaster.setInverted(false);
        liftSlave.setInverted(InvertType.FollowMaster);

        liftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        liftMaster.setSensorPhase(false);

        liftSlave.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        liftSlave.setSensorPhase(false);

        liftSlave.follow(liftMaster);
    }

    public static Elevator getInstance() {
        if (instance == null) {
            instance = new Elevator();
        }
        return instance;
    }

    TalonSRX getDriveEncoderTalon() {
        return liftSlave;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
