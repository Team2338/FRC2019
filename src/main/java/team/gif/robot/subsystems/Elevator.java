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
    }

    public void setMotionMagic(double position, double feedForward) {
        lift.set(ControlMode.MotionMagic, position, DemandType.ArbitraryFeedForward, feedForward);
    }

    public boolean isStopped() {
        return Math.abs(lift.getClosedLoopError()) < Constants.Elevator.ALLOWABLE_ERROR;
    }

    private void configLift(TalonSRX talon) {
        talon.configFactoryDefault();
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10);
        talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10);
        talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        talon.setSensorPhase(false);
        talon.setInverted(false);

        talon.config_kP(0, Constants.Elevator.P);
        talon.config_kI(0, Constants.Elevator.I);
        talon.config_kD(0, Constants.Elevator.D);
        talon.config_kF(0, Constants.Elevator.F);
        talon.configMotionCruiseVelocity(Constants.Elevator.CRUISE_VELOCITY);
        talon.configMotionAcceleration(Constants.Elevator.ACCELERATION);

        talon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);
        talon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);
        talon.configClearPositionOnLimitR(true, 0);
    }

    @Override
    protected void initDefaultCommand() {

    }
}
