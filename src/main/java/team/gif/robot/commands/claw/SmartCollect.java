package team.gif.robot.commands.claw;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.subsystems.Claw;

public class SmartCollect extends Command {

    private final Claw claw = Claw.getInstance();

    public SmartCollect() {
        requires(claw);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        if (claw.isHatchMode()) {
            claw.deployHooks(true);
        } else {
            claw.setIntake(claw.hasBall() ? 0.1 : 0.5);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        claw.setIntake(0.0);
    }
}
