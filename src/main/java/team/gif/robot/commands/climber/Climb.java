package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.subsystems.Climber;

public class Climb extends Command {

    private final Climber climber = Climber.getInstance();
    private final double kP = Constants.Climber.GYRO_SENSITIVITY;
    private double time;

    public Climb(double timeout) {
        super(timeout);
        requires(climber);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        time = Timer.getFPGATimestamp();

        //Blocked out below code cause not being used
        //double[] ypr_deg = drivetrain.getYawPitchRoll();
        double[] errors = climber.getBalanceError();
//        double[] heightError = climber.getRelativeHeight();

        double frontLeftPercent = 1.0 - kP * errors[0];
        double rearLeftPercent = 0.75 - kP * errors[0];
        double frontRightPercent = 1.0 - kP * errors[0];
        double rearRightPercent = 0.75 - kP * errors[0];

        climber.setPistons(frontLeftPercent, rearLeftPercent, frontRightPercent, rearRightPercent, time);

//        climber.setFrontLeft(.6 - (kP * heightError[0]), time);
//        climber.setRearLeft(0.6 - (kP * heightError[1]), time);
//        climber.setFrontRight(.6 - (kP * heightError[2]), time);
//        climber.setRearRight(0.6 - (kP * heightError[3]), time);


//        climber.setFrontLeft(0.75, timeMs);
//        climber.setRearLeft(0.5, timeMs);
//        climber.setFrontRight(0.75, timeMs);
//        climber.setRearRight(0.5, timeMs);
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        climber.setPistons(0.0, 0.0, 0.0, 0.0, 0.0);
    }
}
