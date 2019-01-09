package team.gif.robot.commands.drivetrain;

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
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {

    }
}
