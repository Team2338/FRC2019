package team.gif.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.subsystems.Drivetrain;

public class DriveTeleOp extends Command {

    private Drivetrain drivetrain = Drivetrain.getInstance();

    public DriveTeleOp() {
        requires(drivetrain);
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
