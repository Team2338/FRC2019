package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.subsystems.Climber;

public class UnClimb extends Command {

    private Climber climber = Climber.getInstance();

    public UnClimb(double timeout) {
        super(timeout);
        requires(climber);
    }

    @Override
    protected void initialize() {
        climber.setFrontLeft(-1.0, 0.0);
        climber.setRearLeft(-1.0, 0.0);
        climber.setFrontRight(-1.0, 0.0);
        climber.setRearRight(-1.0, 0.0);

    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        climber.setFrontLeft(0.0, 0.0);
        climber.setRearLeft(0.0, 0.0);
        climber.setFrontRight(0.0, 0.0);
        climber.setRearRight(0.0, 0.0);
    }
}
