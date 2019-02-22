package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;

public class Claw extends Subsystem {

    private static Claw instance;

    private final TalonSRX intake;
    private final Solenoid deploy, clamp, hooks;
    private final Servo left, right;

    public final AnalogInput ballSensor;

    private boolean hatchMode = true;

    private Claw() {
        intake = new TalonSRX(RobotMap.CLAW_INTAKE_ID);

        deploy = new Solenoid(RobotMap.CLAW_DEPLOY_ID);
        clamp = new Solenoid(RobotMap.CLAW_CLAMP_ID);
        hooks = new Solenoid(RobotMap.CLAW_HOOKS_ID);

        left = new Servo(RobotMap.CLAW_LEFT_SERVO_ID);
        right = new Servo(RobotMap.CLAW_RIGHT_SERVO_ID);

        ballSensor = new AnalogInput(RobotMap.CLAW_BALL_SENSOR_ID);

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

    public void openClamp(boolean open) {
        clamp.set(open);
    }

    public void deployHooks(boolean out) {
        hooks.set(out);
    }

    public void engageServoBrakes(boolean engaged) {
        left.set(engaged ? Constants.Claw.LEFT_BRAKE_POS : Constants.Claw.LEFT_NEUTRAL_POS);
        right.set(engaged ? Constants.Claw.RIGHT_BRAKE_POS : Constants.Claw.RIGHT_NEUTRAL_POS);
    }

    public void setHatchMode(boolean hatchMode) {
        this.hatchMode = hatchMode;
    }

    public boolean isHatchMode() {
        return hatchMode;
    }

    public boolean isDeployed() {
        return deploy.get();
    }

    public boolean hasBall() {
        return ballSensor.getAverageVoltage() < 1.0;
    }

    TalonSRX getDriveEncoderTalon() {
        return intake;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
