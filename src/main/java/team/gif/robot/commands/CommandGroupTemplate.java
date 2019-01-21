package team.gif.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class CommandGroupTemplate extends CommandGroup {

    public CommandGroupTemplate() {
        addSequential(new CommandTemplate());
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
