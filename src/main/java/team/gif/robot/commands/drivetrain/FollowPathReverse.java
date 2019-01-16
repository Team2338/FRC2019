package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import team.gif.lib.MiniPID;
import team.gif.robot.Constants;
import team.gif.robot.subsystems.Drivetrain;

public class FollowPathReverse extends Command {

    private Drivetrain drivetrain;
    private TankModifier modifier;
    private EncoderFollower leftFollower;
    private EncoderFollower rightFollower;
    private MiniPID rotatePID;

    public FollowPathReverse(Trajectory trajectory) {
        drivetrain = Drivetrain.getInstance();
        modifier = new TankModifier(trajectory).modify(Constants.Drivetrain.TRACK_WIDTH);
        leftFollower = new EncoderFollower(modifier.getRightTrajectory());
        rightFollower = new EncoderFollower(modifier.getLeftTrajectory());
        rotatePID = new MiniPID(Constants.Drivetrain.ROTATE_P, Constants.Drivetrain.ROTATE_I, Constants.Drivetrain.ROTATE_D);

        leftFollower.configurePIDVA(Constants.Drivetrain.DRIVE_P, Constants.Drivetrain.DRIVE_I,
                Constants.Drivetrain.DRIVE_D, Constants.Drivetrain.V_LEFT, Constants.Drivetrain.A_LEFT);
        rightFollower.configurePIDVA(Constants.Drivetrain.DRIVE_P, Constants.Drivetrain.DRIVE_I,
                Constants.Drivetrain.DRIVE_D, Constants.Drivetrain.V_RIGHT, Constants.Drivetrain.A_RIGHT);

        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        leftFollower.configureEncoder(-drivetrain.getLeftEncPosition(), Constants.Drivetrain.TICKS_PER_REV,
                Constants.Drivetrain.WHEEL_DIAMETER);
        rightFollower.configureEncoder(-drivetrain.getRightEncPosition(), Constants.Drivetrain.TICKS_PER_REV,
                Constants.Drivetrain.WHEEL_DIAMETER);
    }

    @Override
    protected void execute() {
        double leftOutput = leftFollower.calculate(-drivetrain.getLeftEncPosition());
        double rightOutput = rightFollower.calculate(-drivetrain.getRightEncPosition());

        if (Math.abs(leftOutput) > 0.01) leftOutput += Math.copySign(Constants.Drivetrain.V_INTERCEPT_LEFT, leftOutput);
        if (Math.abs(rightOutput) > 0.01) rightOutput += Math.copySign(Constants.Drivetrain.V_INTERCEPT_RIGHT, rightOutput);

        double heading = Pathfinder.boundHalfDegrees(drivetrain.getHeading());
        double headingTarget = Math.toDegrees(-leftFollower.getHeading()); //TODO: Check if both followers yield same heading.
        double turn = rotatePID.getOutput(heading, headingTarget);

        drivetrain.setOutputs(-(leftOutput + turn), -(rightOutput - turn));
    }

    @Override
    protected boolean isFinished() {
        return leftFollower.isFinished() && rightFollower.isFinished();
    }

    @Override
    protected void end() {
        drivetrain.setOutputs(0.0, 0.0);
    }
}
