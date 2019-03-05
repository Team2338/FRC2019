package team.gif.robot.commands.claw;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.subsystems.Claw;

public class SetClawMode extends Command {

    private final Claw claw = Claw.getInstance();
    private boolean isFinished = false;
    private boolean hatchMode;

    public SetClawMode(boolean hatchMode) {
        this.hatchMode = hatchMode;
        requires(claw);
    }

    @Override
    protected void initialize() {
        isFinished = false;
        claw.setHatchMode(hatchMode);
        claw.openClamp(true);
        claw.deployHooks(false);
    }

    @Override
    protected void execute() {
        if (timeSinceInitialized() > 0.4) {
            isFinished = true;
        } else if (timeSinceInitialized() > 0.1) {
            claw.engageServoBrakes(!hatchMode);
        }
    }

    @Override
    protected boolean isFinished() {
        return isFinished;
    }

    @Override
    protected void end() {
        claw.openClamp(false);
    }
}
