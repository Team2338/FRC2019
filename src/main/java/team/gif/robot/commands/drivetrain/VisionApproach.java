package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import team.gif.lib.MiniPID;
import team.gif.lib.drivers.Limelight;
import team.gif.robot.Constants;
import team.gif.robot.Robot;
import team.gif.robot.subsystems.Drivetrain;

public class VisionApproach extends Command implements Runnable {

    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final Limelight limelight = Robot.limelight;
    private TankModifier modifier;
    private EncoderFollower leftFollower;
    private EncoderFollower rightFollower;
    private MiniPID rotatePID;
    private Notifier notifier;
    private double[] initCamTran;
    private double initHeading;
//    private Command approach;

    public VisionApproach() {
        rotatePID = new MiniPID(Constants.Drivetrain.ROTATE_P, Constants.Drivetrain.ROTATE_I, Constants.Drivetrain.ROTATE_D);
        notifier = new Notifier(this);

        requires(drivetrain);
    }

    @Override
    protected void initialize() {

        initCamTran = limelight.getCamTran();
        initHeading = Pathfinder.boundHalfDegrees(drivetrain.getHeadingDegrees());

        Waypoint[] array = new Waypoint[] {
                new Waypoint(initCamTran[2] - Constants.Camera.POS_Y * Math.cos(Math.toRadians(initCamTran[4])) -
                        Constants.Camera.POS_X * Math.sin(Math.toRadians(initCamTran[4]))
                        , -initCamTran[0] + Constants.Camera.POS_Y * Math.sin(Math.toRadians(initCamTran[4])) +
                        Constants.Camera.POS_X * Math.cos(Math.toRadians(initCamTran[4])), Math.toRadians(initCamTran[4])),
                new Waypoint(-(Constants.Drivetrain.BUMPER_LENGTH/2 + 10.0), 0, 0)
        };

        Trajectory trajectory = Pathfinder.generate(array, Constants.Drivetrain.slowConfig);

        modifier = new TankModifier(trajectory).modify(Constants.Drivetrain.TRACK_WIDTH);
        leftFollower = new EncoderFollower(modifier.getLeftTrajectory());
        rightFollower = new EncoderFollower(modifier.getRightTrajectory());

        leftFollower.configurePIDVA(Constants.Drivetrain.DRIVE_P, Constants.Drivetrain.DRIVE_I,
                Constants.Drivetrain.DRIVE_D, Constants.Drivetrain.V_LEFT_FWD, Constants.Drivetrain.A_LEFT);
        rightFollower.configurePIDVA(Constants.Drivetrain.DRIVE_P, Constants.Drivetrain.DRIVE_I,
                Constants.Drivetrain.DRIVE_D, Constants.Drivetrain.V_RIGHT_FWD, Constants.Drivetrain.A_RIGHT);

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

        if (Math.abs(leftOutput) > 0.01) leftOutput += Math.copySign(Constants.Drivetrain.V_INTERCEPT_LEFT_FWD, leftOutput);
        if (Math.abs(rightOutput) > 0.01) rightOutput += Math.copySign(Constants.Drivetrain.V_INTERCEPT_RIGHT_FWD, rightOutput);

        double heading = Pathfinder.boundHalfDegrees(drivetrain.getHeadingDegrees()) - initHeading + initCamTran[4];
        double headingTarget = Pathfinder.boundHalfDegrees(Math.toDegrees(leftFollower.getHeading()));
        double turn = rotatePID.getOutput(heading, headingTarget);

        drivetrain.setOutputs(leftOutput - turn, rightOutput + turn);
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

        System.out.println("I am done with vision.");

        drivetrain.setOutputs(0.0, 0.0);
    }
}
