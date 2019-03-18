package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.lib.math.LinearRegression;
import team.gif.robot.subsystems.Drivetrain;

public class DriveVoltageRampTest extends Command {

    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private LinearRegression leftRegression, rightRegression;
    private double[] leftVoltage, rightVoltage, leftVelocity, rightVelocity;
    private int totalFrames;
    private int currentFrame;
    private boolean reverse;
    private int trim;

    public DriveVoltageRampTest(double timeout, boolean reverse, int trim){
        requires(drivetrain);
        this.reverse = reverse;
        this.trim = trim;
        totalFrames = (int)timeout * 50 - trim;
    }

    @Override
    protected void initialize() {
        leftVoltage = new double[totalFrames];
        rightVoltage = new double[totalFrames];
        leftVelocity = new double[totalFrames];
        rightVelocity = new double[totalFrames];
        currentFrame = -trim;
    }

    @Override
    protected void execute() {
        double percentOutput = (reverse ? -1 : 1) * timeSinceInitialized() * (0.25 / 12.0);
        drivetrain.setOutputs(percentOutput, percentOutput);
        if (currentFrame < totalFrames && currentFrame >= 0) {
            leftVoltage[currentFrame] = drivetrain.getOutputVoltage()[0] / 12;
            rightVoltage[currentFrame] = drivetrain.getOutputVoltage()[1] / 12;
            leftVelocity[currentFrame] = drivetrain.getLeftVelTPS() / 4096;
            rightVelocity[currentFrame] = drivetrain.getRightVelTPS() / 4096;
        }
        currentFrame++;
    }

    @Override
    protected boolean isFinished() {
        return !(currentFrame < totalFrames);
    }

    @Override
    protected void end() {
        drivetrain.setOutputs(0.0, 0.0);
        leftRegression = new LinearRegression(leftVelocity, leftVoltage);
        rightRegression = new LinearRegression(rightVelocity, rightVoltage);
        System.out.println("Left Result: " + leftRegression.toString());
        System.out.println("Right Result: " + rightRegression.toString());
    }

}
