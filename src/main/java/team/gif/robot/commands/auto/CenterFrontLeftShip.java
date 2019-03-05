package team.gif.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import team.gif.lib.AutoPosition;
import team.gif.lib.TargetPosition;
import team.gif.robot.Constants;
import team.gif.robot.commands.backhatch.BackEject;
import team.gif.robot.commands.drivetrain.FollowPath;
import team.gif.robot.commands.drivetrain.FollowPathReverse;

public class CenterFrontLeftShip extends CommandGroup {

    private final Trajectory approach = Pathfinder.generate(new Waypoint[] {
            AutoPosition.L1_CENTER.getWaypoint(),
            TargetPosition.LEFT_SHIP_FRONT.getRobotWaypoint(Constants.Drivetrain.BUMPER_LENGTH / 2, 0, false)
    }, Constants.Drivetrain.config);

    private final Trajectory backup = Pathfinder.generate(new Waypoint[] {
            TargetPosition.LEFT_SHIP_FRONT.getRobotWaypoint(Constants.Drivetrain.BUMPER_LENGTH / 2, 0, true),
            TargetPosition.LEFT_SHIP_FRONT.getRobotWaypoint(Constants.Drivetrain.BUMPER_LENGTH / 2 + 48, 0, true),
    }, Constants.Drivetrain.config);

    public CenterFrontLeftShip() {
        addSequential(new FollowPathReverse(approach));
        addSequential(new BackEject(1.5));
        addSequential(new FollowPath(backup));
    }

    @Override
    protected void initialize() {

    }

}
