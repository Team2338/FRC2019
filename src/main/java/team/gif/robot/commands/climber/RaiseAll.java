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

    }

    @Override
    protected void execute() {
        climber.setPistons(false, true);
        climber.setWinchPercent(0.5);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {

    }
}
