package team.gif.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team.gif.robot.commands.DoNothing;

public class AutoTemplate extends CommandGroup {

    public AutoTemplate() {
        addSequential(new DoNothing());
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
