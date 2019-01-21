package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import team.gif.lib.DriveController;
import team.gif.robot.OI;
import team.gif.robot.subsystems.Drivetrain;

public class DriveTeleOp extends Command {

    private Drivetrain drivetrain;
    private DriveController controller;

    public DriveTeleOp() {
        drivetrain = Drivetrain.getInstance();
        controller = new DriveController();
        requires(drivetrain);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        double magnitude = -OI.getInstance().driver.getY(GenericHID.Hand.kLeft);
        double rotation = OI.getInstance().driver.getX(GenericHID.Hand.kRight);
        boolean quickTurn = OI.getInstance().dRT.get();

        double[] outputs = controller.curvatureDrive(magnitude, rotation, quickTurn);
        drivetrain.setOutputs(outputs[0], outputs[1]);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        drivetrain.setOutputs(0.0, 0.0);
    }
}
