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
        lift.set(ControlMode.PercentOutput, percent);
//        lift.set(ControlMode.PercentOutput, percent);
    }

    public void setMotionMagic(double position) {
        lift.set(ControlMode.MotionMagic, position);
    }

    public void setMotionMagic(double position, double arbitraryFeedForward) {
        lift.set(ControlMode.MotionMagic, position, DemandType.ArbitraryFeedForward, arbitraryFeedForward);
    }

    public void setCruiseVelocity(int ticksPer100ms) {
        lift.configMotionCruiseVelocity(ticksPer100ms);
    }

    public void configF(double f) {
        lift.config_kF(0, f);
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

    public double getOutputCommand() {
        return lift.getMotorOutputPercent();
    }

    public double getVelTPS() {
        return lift.getSelectedSensorVelocity() * 10.0;
    }

    public double getVelRPS() {
        return getVelTPS() * Constants.Drivetrain.TPS_TO_RPS;
    }

    public double getCurrent() {
        return lift.getOutputCurrent();
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
        talon.setNeutralMode(NeutralMode.Brake);

        talon.config_kP(0, Constants.Elevator.P);
        talon.config_kI(0, Constants.Elevator.I);
        talon.config_kD(0, Constants.Elevator.D);
        talon.config_kF(0, Constants.Elevator.F);
        talon.configMotionCruiseVelocity(Constants.Elevator.MAX_VELOCITY);
        talon.configMotionAcceleration(Constants.Elevator.MAX_ACCELERATION);
        talon.configNominalOutputForward(0);
        talon.configNominalOutputReverse(0);
        talon.configPeakOutputForward(1);
        talon.configPeakOutputReverse(-1);

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
