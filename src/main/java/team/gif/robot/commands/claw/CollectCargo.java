package team.gif.robot.commands.claw;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.subsystems.Claw;

public class CollectCargo extends Command {

    private final Claw claw = Claw.getInstance();

    public CollectCargo() {
        requires(claw);
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {

    }
}
