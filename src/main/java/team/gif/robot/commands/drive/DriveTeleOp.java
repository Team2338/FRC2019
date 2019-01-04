package team.gif.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.subsystems.Drive;

public class DriveTeleOp extends Command {

    private Drive drive = Drive.getInstance();

    public DriveTeleOp() {
        requires(drive);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {

    }

    @Override
    protected void end() {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
