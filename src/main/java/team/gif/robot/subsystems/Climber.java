package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;
import team.gif.robot.commands.climber.RaiseFront;

public class Climber extends Subsystem {

    private static Climber instance;

    private final Solenoid frontRack, rearRack;
    private final TalonSRX climbDrive, climbWinch;
    private final PigeonIMU imu;

    private Climber() {
        frontRack = new Solenoid(RobotMap.FRONT_CLIMB_ID);
        rearRack = new Solenoid(RobotMap.REAR_CLIMB_ID);

        climbDrive = new TalonSRX(RobotMap.CLIMB_DRIVE_ID);
        climbWinch = new TalonSRX(RobotMap.CLIMB_WINCH);
        imu = new PigeonIMU(RobotMap.PIGEON_ID);

        climbDrive.configFactoryDefault();
        climbWinch.configFactoryDefault();
        climbWinch.enableVoltageCompensation(true);
        climbWinch.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        climbWinch.config_kP(0, Constants.Climber.P);
        climbWinch.config_kI(0, Constants.Climber.I);
        climbWinch.config_kD(0, Constants.Climber.D);
        climbWinch.config_kF(0, Constants.Climber.F);
        imu.configFactoryDefault();
    }

    public static Climber getInstance() {
        if (instance == null) {
            instance = new Climber();
        }
        return instance;
    }

    /**
     * Sets the duty cycle for each climber piston. This method must be called in a loop to update {@param time}.
     *
     * @param rear output percent (-1.0 to +1.0)
     * @param rear output percent (-1.0 to +1.0)
     */
    public void setPistons(boolean front, boolean rear) {
        this.frontRack.set(front);
        this.rearRack.set(rear);
    }

    /**
     * Sets the percent voltage output for the rearRack wheel climb drive.
     *
     * @param percent output percent (-1.0 to +1.0)
     */
    public void setDrive(double percent) {
        climbDrive.set(ControlMode.PercentOutput, percent);
    }

    public void setWinchPercent(double percent) {
        climbWinch.set(ControlMode.PercentOutput, percent, DemandType.ArbitraryFeedForward, Constants.Climber.PISTON_FEED_FORWARD);
    }

    public void setWinchVel(double velocity) {
        climbWinch.set(ControlMode.Velocity, velocity, DemandType.ArbitraryFeedForward, Constants.Climber.PISTON_FEED_FORWARD);
    }

    public int getWinchPos() {
        return climbWinch.getSelectedSensorPosition();
    }

    public double getWinchCurrent() {
        return climbWinch.getOutputCurrent();
    }

    /**
     * Calculates balance error for each corner of the drivetrain. Positive values are above the CG, negative values
     * are below. These are calculated by summing the relevant tilts for each corner. This means that each error has
     * a theoretical range of +180 to -180 degrees.
     *
     * @param ypr_deg array containing rotation on all axes
     * @return errors for frontRack left [0], rearRack left [1], frontRack right [2], and rearRack right [3] in degrees
     */
    public double[] getBalanceError(double[] ypr_deg) {
        double pitch = ypr_deg[1] - 0.571; // TODO: Make me a constant!
        double roll = ypr_deg[2] - 1.406; // TODO: Make me a constant!
        double frontLeftError = pitch + roll;
        double rearLeftError = -pitch + roll;
        double frontRightError = pitch - roll;
        double rearRightError = -pitch - roll;
        return new double[] {frontLeftError, rearLeftError, frontRightError, rearRightError};
    }

    TalonSRX getDriveEncoderTalon() {
        return climbDrive;
    }

    @Override
    protected void initDefaultCommand() {
//        setDefaultCommand(new RaiseFront());
    }
}
