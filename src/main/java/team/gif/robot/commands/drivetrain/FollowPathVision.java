package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import team.gif.lib.MiniPID;
import team.gif.lib.drivers.Limelight;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Drivetrain;

public class FollowPathVision extends Command implements Runnable {

    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final Limelight limelight = Robot.limelight;
    private final EncoderFollower leftFollower;
    private final EncoderFollower rightFollower;
    private final MiniPID rotatePID;
    private final Notifier notifier;
    private final double visionDist;
    private double initDist;

    public FollowPathVision(Trajectory trajectory, double visionDist) {
        TankModifier modifier = new TankModifier(trajectory).modify(Constants.Drivetrain.TRACK_WIDTH);
        leftFollower = new EncoderFollower(modifier.getLeftTrajectory());
        rightFollower = new EncoderFollower(modifier.getRightTrajectory());
        rotatePID = new MiniPID(Constants.Drivetrain.ROTATE_P, Constants.Drivetrain.ROTATE_I, Constants.Drivetrain.ROTATE_D);
        notifier = new Notifier(this);
        this.visionDist = trajectory.get(trajectory.length() - 1).position - visionDist;

        leftFollower.configurePIDVA(Constants.Drivetrain.DRIVE_P, Constants.Drivetrain.DRIVE_I,
                Constants.Drivetrain.DRIVE_D, Constants.Drivetrain.V_LEFT_FWD, Constants.Drivetrain.A_LEFT);
        rightFollower.configurePIDVA(Constants.Drivetrain.DRIVE_P, Constants.Drivetrain.DRIVE_I,
                Constants.Drivetrain.DRIVE_D, Constants.Drivetrain.V_RIGHT_FWD, Constants.Drivetrain.A_RIGHT);

        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        leftFollower.configureEncoder(drivetrain.getLeftPosTicks(), Constants.Drivetrain.TICKS_PER_REV,
                Constants.Drivetrain.WHEEL_DIAMETER);
        rightFollower.configureEncoder(drivetrain.getRightPosTicks(), Constants.Drivetrain.TICKS_PER_REV,
                Constants.Drivetrain.WHEEL_DIAMETER);

        initDist = (drivetrain.getLeftPosInches() + drivetrain.getRightPosInches()) / 2;

        limelight.setPipeline(0);
        limelight.setLEDMode(3);
        notifier.startPeriodic(0.01);
    }

    @Override
    public void run() {
        double leftOutput = leftFollower.calculate(drivetrain.getLeftPosTicks());
        double rightOutput = rightFollower.calculate(drivetrain.getRightPosTicks());

        if (Math.abs(leftOutput) > 0.01) { leftOutput += Math.copySign(Constants.Drivetrain.V_INTERCEPT_LEFT_FWD, leftOutput); }
        if (Math.abs(rightOutput) > 0.01) { rightOutput += Math.copySign(Constants.Drivetrain.V_INTERCEPT_RIGHT_FWD, rightOutput); }

        double heading = Pathfinder.boundHalfDegrees(drivetrain.getHeadingDegrees());
        double headingTarget = Pathfinder.boundHalfDegrees(Math.toDegrees(leftFollower.getHeading()));
        double error = headingTarget - heading;
        if (Math.abs(error) > 180) {
            if (error > 0) {
                error -= 360;
            } else {
                error += 360;
            }
        }

        double turn = rotatePID.getOutput(-error, 0);
        if ((drivetrain.getLeftPosInches() + drivetrain.getRightPosInches()) / 2 - initDist > visionDist) {
            if (limelight.hasTarget()) turn += -Constants.Drivetrain.VISION_P * limelight.getXOffset();
        }

//        System.out.println("Heading: " + heading + ", Vision: " + limelight.getXOffset());

        drivetrain.setOutputs(leftOutput - turn, rightOutput + turn);
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