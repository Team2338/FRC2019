package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import team.gif.lib.drivers.Limelight;
import team.gif.robot.subsystems.Drivetrain;

public class DriveToTarget extends Command {

    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Limelight limelight = Limelight.getInstance();

    private Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC,
            Trajectory.Config.SAMPLES_FAST, 0.01, 72.0, 72.0, 9999.0);

    public DriveToTarget() {
        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        if (limelight.hasTarget()) {
            double[] camPos = limelight.getCamTran();

            Waypoint[] arr = new Waypoint[] {
                    new Waypoint(camPos[2], camPos[0], drivetrain.getHeadingRadians()),
                    new Waypoint(0.0, 0.0, drivetrain.getHeadingRadians() + camPos[4]) // Look at limelight stuff
            };

            Trajectory traj = Pathfinder.generate(arr, config);
            new FollowPath(traj).start();
        }
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
