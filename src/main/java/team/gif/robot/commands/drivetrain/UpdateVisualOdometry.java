package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.lib.Odometry;
import team.gif.lib.drivers.Limelight;

public class UpdateVisualOdometry extends Command {

    private Limelight limelight = Limelight.getInstance();
    private Odometry odometry = Odometry.getInstance();

    public UpdateVisualOdometry() {
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
