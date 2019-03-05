package team.gif.robot.commands.claw;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.subsystems.Claw;

public class SetDeploy extends Command {

    private final Claw claw = Claw.getInstance();
    private final boolean out;

    public SetDeploy(boolean out) {
        this.out = out;
        requires(claw);
    }

    @Override
    protected void initialize() {
        claw.deployClaw(out);
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
