package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;

public class Elevator extends Subsystem {

    private static Elevator instance;

    private final TalonSRX lift;

    private Elevator() {
        lift = new TalonSRX(RobotMap.ELEVATOR_LIFT_ID);
        configLift(lift);

        int absPos = lift.getSensorCollection().getPulseWidthPosition();
        absPos &= 0xFFF;
        lift.setSelectedSensorPosition(absPos);
    }

    public static Elevator getInstance() {
        if (instance == null) {
            instance = new Elevator();
        }
        return instance;
    }

    public void setPercentOutput(double percent) {
//        lift.set(ControlMode.PercentOutput, percent, DemandType.ArbitraryFeedForward, Constants.Elevator.GRAV_FEED_FORWARD);
        lift.set(ControlMode.PercentOutput, percent);
    }

    public void setMotionMagic(double position) {
        lift.set(ControlMode.MotionMagic, position, DemandType.ArbitraryFeedForward, Constants.Elevator.GRAV_FEED_FORWARD);
    }

    public void setMotionVelocity(int ticksPer100ms) {
        lift.configMotionCruiseVelocity(ticksPer100ms);
    }

    public int getPosition() {
        return lift.getSelectedSensorPosition();
    }

    public boolean getFwdLimit() {
        return lift.getSensorCollection().isFwdLimitSwitchClosed();
    }

    public boolean getRevLimit() {
        return lift.getSensorCollection().isRevLimitSwitchClosed();
    }

    public boolean isFinished() {
        return Math.abs(lift.getClosedLoopError()) < Constants.Elevator.ALLOWABLE_ERROR;
    }

    public double getOutputVoltage() {
        return lift.getMotorOutputVoltage();
    }

    public double getVelTPS() {
        return lift.getSelectedSensorVelocity() * 10.0;
    }

    public double getVelRPS() {
        return getVelTPS() * Constants.Drivetrain.TPS_TO_RPS;
    }

    public int getClosedLoopError() {
        return lift.getClosedLoopError();
    }

    private void configLift(TalonSRX talon) {
        talon.configFactoryDefault();
//        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10);
//        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10);
        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        talon.enableVoltageCompensation(true);
        talon.setSensorPhase(true);
        talon.setInverted(false);

        talon.config_kP(0, Constants.Elevator.P);
        talon.config_kI(0, Constants.Elevator.I);
        talon.config_kD(0, Constants.Elevator.D);
        talon.config_kF(0, Constants.Elevator.F);
        talon.configMotionCruiseVelocity(Constants.Elevator.MAX_VELOCITY);
        talon.configMotionAcceleration(Constants.Elevator.MAX_ACCELERATION);

        talon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);
        talon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        talon.configForwardSoftLimitThreshold(Constants.Elevator.MAX_POS);
        talon.configReverseSoftLimitThreshold(Constants.Elevator.MIN_POS);
        talon.overrideLimitSwitchesEnable(false);
        talon.configForwardSoftLimitEnable(true);
        talon.configReverseSoftLimitEnable(true);
//        talon.configClearPositionOnLimitR(true, 0);
    }

    @Override
    protected void initDefaultCommand() {

    }
}
