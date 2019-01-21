package team.gif.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.lib.VariableRateSolenoid;
import team.gif.robot.Constants;
import team.gif.robot.RobotMap;

public class Climber extends Subsystem {

    private static Climber instance;

    private final VariableRateSolenoid frontLeft, rearLeft, frontRight, rearRight;
    private final TalonSRX climbDrive;
    private final PigeonIMU imu;

    private Climber() {
        frontLeft = new VariableRateSolenoid(RobotMap.FRONT_LEFT_FORWARD_ID, RobotMap.FRONT_LEFT_REVERSE_ID);
        rearLeft = new VariableRateSolenoid(RobotMap.REAR_LEFT_FORWARD_ID, RobotMap.REAR_LEFT_REVERSE_ID);
        frontRight = new VariableRateSolenoid(RobotMap.FRONT_RIGHT_FORWARD_ID, RobotMap.FRONT_RIGHT_REVERSE_ID);
        rearRight = new VariableRateSolenoid(RobotMap.REAR_RIGHT_FORWARD_ID, RobotMap.REAR_RIGHT_REVERSE_ID);

        climbDrive = new TalonSRX(RobotMap.CLIMB_DRIVE_ID);
        imu = new PigeonIMU(climbDrive);
        climbDrive.configFactoryDefault();
        imu.configFactoryDefault();
    }

    public static Climber getInstance() {
        if (instance == null) {
            instance = new Climber();
        }
        return instance;
    }

    public void setFrontLeft(double percent, double time) {
        frontLeft.setPercentOutput(percent, Constants.Climber.VARIABLE_RATE_PISTON_PERIOD, time);
    }

    public void setRearLeft(double percent, double time) {
        rearLeft.setPercentOutput(percent, Constants.Climber.VARIABLE_RATE_PISTON_PERIOD, time);
    }

    public void setFrontRight(double percent, double time) {
        frontRight.setPercentOutput(percent, Constants.Climber.VARIABLE_RATE_PISTON_PERIOD, time);
    }

    public void setRearRight(double percent, double time) {
        rearRight.setPercentOutput(percent, Constants.Climber.VARIABLE_RATE_PISTON_PERIOD, time);
    }

    public void setClimbDrive(double percent) {
        climbDrive.set(ControlMode.PercentOutput, percent);
    }

    double[] getYawPitchRoll() {
        double[] ypr_deg = new double[3];
        imu.getYawPitchRoll(ypr_deg);
        return ypr_deg;
    }

    public double[] getBalanceError() {
        double pitch = getYawPitchRoll()[1];
        double roll = getYawPitchRoll()[2];
        double frontLeftError = pitch + roll; // negative error means more effort required e.g. negative is lower
        double rearLeftError = -pitch + roll; // TODO: Test whether these return the right polarities with native pitch and roll
        double frontRightError = pitch - roll;
        double rearRightError = -pitch - roll;
        return new double[] {frontLeftError, rearLeftError, frontRightError, rearRightError};
    }

    @Override
    protected void initDefaultCommand() {

    }
}
