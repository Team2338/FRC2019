package team.gif.robot.commands.backhatch;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.subsystems.BackHatch;

public class BackEject extends Command {

    private final BackHatch backHatch = BackHatch.getInstance();

    public BackEject(double timeout) {
        setTimeout(timeout);
        requires(backHatch);
    }

    @Override
    protected void initialize() {
        backHatch.set(true);
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        backHatch.set(false);
    }
}
