package team.gif.robot.commands.claw;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.subsystems.Claw;

public class SetClawOpen extends Command {

    private final Claw claw = Claw.getInstance();
    private boolean open;

    public SetClawOpen(boolean open) {
        this.open = open;
        requires(claw);
    }

    @Override
    protected void initialize() {
        if (claw.isHatchMode()) {
            claw.openClamp(false);
        } else {
            claw.openClamp(open);
        }
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
