package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.lib.Odometry;
import team.gif.lib.VisionMath;
import team.gif.lib.drivers.Limelight;
import team.gif.robot.Constants;

public class UpdateVisualOdometry extends Command {

    private Limelight limelight;
    private Odometry odometry;

    public UpdateVisualOdometry() {
        limelight = Limelight.getInstance();
        odometry = Odometry.getInstance();
        // 1. Obtain distance and angle offset to valid target
        // 2. Use current robot odometry and known target positions to estimate which target was read
        // 3. Use the known position of read target to update robot odometry (update position only)
        // NOTE: This form of visual odometry will likely be imprecise and may yield anywhere between 1-4
        // inches of error on either axis. This requires further testing. However, it does not accumulate
        // error in the same way that wheel odometry does and will ensure that measurements remain field-oriented
        // Basically, it's a trade off between precision and accuracy. Wheel odometry is more precise and visual odometry
        // is more accurate.
    }

    @Override
    protected void initialize() {
        double xOffsetRadians = Math.toRadians(limelight.getXOffset());
        double yOffsetRadians = Math.toRadians(limelight.getYOffset());
        double lensDistance = VisionMath.getCorrectedDistance(Constants.Camera.POS_Z, xOffsetRadians, yOffsetRadians);
        double robotX = odometry.getX();
        double robotY = odometry.getY();
        double robotTheta = odometry.getTheta();
        // TODO: Finish the above maybe
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {

    }
}
