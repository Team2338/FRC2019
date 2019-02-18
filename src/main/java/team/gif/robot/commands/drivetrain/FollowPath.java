package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import team.gif.lib.MiniPID;
import team.gif.robot.Constants;
import team.gif.robot.subsystems.Drivetrain;

public class FollowPath extends Command implements Runnable {

    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final TankModifier modifier;
    private final EncoderFollower leftFollower;
    private final EncoderFollower rightFollower;
    private final MiniPID rotatePID;
    private final Notifier notifier;

    public FollowPath(Trajectory trajectory) {
        modifier = new TankModifier(trajectory).modify(Constants.Drivetrain.TRACK_WIDTH);
        leftFollower = new EncoderFollower(modifier.getLeftTrajectory());
        rightFollower = new EncoderFollower(modifier.getRightTrajectory());
        rotatePID = new MiniPID(Constants.Drivetrain.ROTATE_P, Constants.Drivetrain.ROTATE_I, Constants.Drivetrain.ROTATE_D);
        notifier = new Notifier(this);

        leftFollower.configurePIDVA(Constants.Drivetrain.DRIVE_P, Constants.Drivetrain.DRIVE_I,
                Constants.Drivetrain.DRIVE_D, Constants.Drivetrain.V_LEFT, Constants.Drivetrain.A_LEFT);
        rightFollower.configurePIDVA(Constants.Drivetrain.DRIVE_P, Constants.Drivetrain.DRIVE_I,
                Constants.Drivetrain.DRIVE_D, Constants.Drivetrain.V_RIGHT, Constants.Drivetrain.A_RIGHT);

        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        leftFollower.configureEncoder(drivetrain.getLeftPosTicks(), Constants.Drivetrain.TICKS_PER_REV,
                Constants.Drivetrain.WHEEL_DIAMETER);
        rightFollower.configureEncoder(drivetrain.getRightPosTicks(), Constants.Drivetrain.TICKS_PER_REV,
                Constants.Drivetrain.WHEEL_DIAMETER);

        notifier.startPeriodic(0.01);
    }

    @Override
    public void run() {
        double leftOutput = leftFollower.calculate(drivetrain.getLeftPosTicks());
        double rightOutput = rightFollower.calculate(drivetrain.getRightPosTicks());

        if (Math.abs(leftOutput) > 0.01) leftOutput += Math.copySign(Constants.Drivetrain.V_INTERCEPT_LEFT, leftOutput);
        if (Math.abs(rightOutput) > 0.01) rightOutput += Math.copySign(Constants.Drivetrain.V_INTERCEPT_RIGHT, rightOutput);

        double heading = Pathfinder.boundHalfDegrees(drivetrain.getHeadingDegrees());
        double headingTarget = Math.toDegrees(leftFollower.getHeading());
        double turn = rotatePID.getOutput(heading, headingTarget);

        drivetrain.setOutputs(leftOutput + turn, rightOutput - turn);
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return leftFollower.isFinished() && rightFollower.isFinished();
    }

    @Override
    protected void end() {
        notifier.stop();
        notifier.close();
        drivetrain.setOutputs(0.0, 0.0);
    }

}
