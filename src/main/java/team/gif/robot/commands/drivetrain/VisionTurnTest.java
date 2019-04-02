package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import team.gif.lib.MiniPID;
import team.gif.lib.drivers.Limelight;
import team.gif.robot.Constants;
import team.gif.robot.OI;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Drivetrain;

public class VisionTurnTest extends Command {

    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final Limelight limelight = Robot.limelight;

    public VisionTurnTest() {
        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        limelight.setLEDMode(3);
        limelight.setCamMode(0);
    }

    @Override
    protected void execute() {
        double turn = OI.getInstance().aux.getX(GenericHID.Hand.kRight);
        turn = -0.006 * limelight.getXOffset();
        drivetrain.setOutputs(-turn, turn);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        limelight.setLEDMode(1);
        limelight.setCamMode(1);
        drivetrain.setOutputs(0.0, 0.0);
    }
}
