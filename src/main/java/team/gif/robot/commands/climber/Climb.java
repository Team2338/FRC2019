package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.OI;
import team.gif.robot.subsystems.Climber;
import team.gif.robot.subsystems.Drivetrain;

public class Climb extends Command {

    private Climber climber = Climber.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private double time;
    private double kP;

    public Climb(double timeout) {
        super(timeout);
        requires(climber);
    }

    @Override
    protected void initialize() {
        time = Timer.getFPGATimestamp();
        kP = 0.01;
    }

    @Override
    protected void execute() {
        time = Timer.getFPGATimestamp();

        double[] ypr_deg = drivetrain.getYawPitchRoll();
        double[] errors = climber.getBalanceError();

        climber.setFrontLeft(1.0 - (kP * errors[0]), time);
        climber.setRearLeft(0.75 - (kP * errors[1]), time);
        climber.setFrontRight(1.0 - (kP * errors[2]), time);
        climber.setRearRight(0.75 - (kP * errors[3]), time);

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
        climber.setFrontLeft(0.0, time);
        climber.setRearLeft(0.0, time);
        climber.setFrontRight(0.0, time);
        climber.setRearRight(0.0, time);
    }
}
