package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.OI;
import team.gif.robot.subsystems.Climber;

public class RaiseAll extends Command {

    private Climber climber = Climber.getInstance();

    public RaiseAll() {
        requires(climber);
    }

    @Override
    protected void initialize() {
        climber.setPistons(false, false);
        climber.setWinchPercent(0.02);
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        climber.setPistons(false, false);
        climber.setWinchPercent(0.0);
    }
}
