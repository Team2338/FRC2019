package team.gif.robot.commands.claw;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.subsystems.Claw;

public class SmartEject extends Command {

    private final Claw claw = Claw.getInstance();

    public SmartEject() {
        requires(claw);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        if (claw.isHatchMode()) {
            claw.deployHooks(false);
        } else {
            claw.setIntake(-1.0);
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
