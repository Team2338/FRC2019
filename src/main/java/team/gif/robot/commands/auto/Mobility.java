package team.gif.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team.gif.lib.AutoPosition;
import team.gif.robot.commands.CommandGroupTemplate;
import team.gif.robot.commands.CommandTemplate;

public class Mobility extends CommandGroup {

    public Mobility(AutoPosition position) {
        if (position.level == 1) {
            addSequential(new CommandTemplate());
        } else {
            addSequential(new CommandTemplate());
        }
    }

    @Override
    protected void initialize() {

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

    }
}
