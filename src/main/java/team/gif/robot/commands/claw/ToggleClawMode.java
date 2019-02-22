package team.gif.robot.commands.claw;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.subsystems.Claw;

public class ToggleClawMode extends Command {

    private final Claw claw = Claw.getInstance();
    private boolean isFinished = false;

    public ToggleClawMode() {
        requires(claw);
    }

    @Override
    protected void initialize() {
        isFinished = false;
        claw.setHatchMode(!claw.isHatchMode());
        claw.openClamp(true);
        claw.deployHooks(false);
        System.out.println("Is Hatch Mode? " + claw.isHatchMode());
    }

    @Override
    protected void execute() {
        if (timeSinceInitialized() > 0.4) {
            isFinished = true;
        } else if (timeSinceInitialized() > 0.1) {
            claw.engageServoBrakes(!claw.isHatchMode());
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
