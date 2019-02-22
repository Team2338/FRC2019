package team.gif.robot.commands.hatch_punch;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.subsystems.HatchPunch;

public class PushOut extends Command {

    private final HatchPunch pusher = HatchPunch.getInstance();

    public PushOut(double timeout) {
        setTimeout(timeout);
        requires(pusher);
    }

    @Override
    protected void initialize() {
        pusher.setPunch(true);
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
        pusher.setPunch(false);
    }
}
