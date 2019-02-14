package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.robot.RobotMap;

public class Claw extends Subsystem {

    private static Claw instance;

    private final TalonSRX intake;
    private final Solenoid deploy, clamp, hooks;

    private Claw() {
        intake = new TalonSRX(RobotMap.CLAW_INTAKE_ID);

        deploy = new Solenoid(1, RobotMap.CLAW_DEPLOY_ID);
        clamp = new Solenoid(1, RobotMap.CLAW_CLAMP_ID);
        hooks = new Solenoid(1, RobotMap.CLAW_HOOKS_ID);

        intake.configFactoryDefault();
    }

    public static Claw getInstance() {
        if (instance == null) {
            instance = new Claw();
        }
        return instance;
    }

    public void setIntake(double percent) {
        intake.set(ControlMode.PercentOutput, percent);
    }

    public void deployClaw(boolean out) {
        deploy.set(out);
    }

    public void setClamp(boolean in) {
        clamp.set(in);
    }

    public void setHooks(boolean out) {
        hooks.set(out);
    }

    TalonSRX getDriveEncoderTalon() {
        return intake;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
