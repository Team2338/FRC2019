package team.gif.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import team.gif.lib.AutoPosition;
import team.gif.lib.TargetPosition;
import team.gif.robot.Constants;
import team.gif.robot.commands.claw.SetDeploy;
import team.gif.robot.commands.drivetrain.FollowPathReverse;
import team.gif.robot.commands.elevator.SetElevatorPosition;
import team.gif.robot.commands.backhatch.BackEject;
import team.gif.robot.subsystems.Drivetrain;

public class RightRocketDouble extends CommandGroup {

    private final Trajectory reverseApproach = Pathfinder.generate(new Waypoint[] {
            AutoPosition.L1_RIGHT.getWaypoint(),
            new Waypoint(AutoPosition.L1_RIGHT.getWaypoint().x + 48.275, AutoPosition.L1_RIGHT.getWaypoint().y, 0.0),
            TargetPosition.RIGHT_ROCKET_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 12, 2, false),
            TargetPosition.RIGHT_ROCKET_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 + 4, 2, false)
    }, Constants.Drivetrain.slowConfig);

    private final Trajectory weirdApproach = Pathfinder.generate(new Waypoint[] {
            AutoPosition.L1_RIGHT.getWaypoint(),
//            new Waypoint(AutoPosition.L1_RIGHT.getWaypoint().x + 48.275, AutoPosition.L1_RIGHT.getWaypoint().y, 0.0),
            new Waypoint(AutoPosition.L1_RIGHT.getWaypoint().x + 12.0, AutoPosition.L1_LEFT.getWaypoint().y, 0.0),
            TargetPosition.RIGHT_ROCKET_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 30, -17, false),
//            TargetPosition.RIGHT_ROCKET_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, -24, false)
    }, Constants.Drivetrain.slowConfig);

    private final Trajectory loadingStation = Pathfinder.generate(new Waypoint[] {
            TargetPosition.RIGHT_ROCKET_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2, 0, true),
            TargetPosition.RIGHT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, false)

    }, Constants.Drivetrain.slowConfig);

    private final Trajectory backupAroundRocket = Pathfinder.generate(new Waypoint[] {
            TargetPosition.RIGHT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, true),
            TargetPosition.RIGHT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4 - 24, 0, true),
            new Waypoint(229.125, -110, 0.0),
            TargetPosition.RIGHT_ROCKET_FAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4 - 24, 0, true)
    }, Constants.Drivetrain.normalConfig);

    public RightRocketDouble() {
        addSequential(new FollowPathReverse(reverseApproach));
        addSequential(new SetDeploy(true));
        addParallel(new SetElevatorPosition(Constants.Elevator.HATCH_LOW_POS));
        addSequential(new BackEject(1.0));
//        addSequential(new FollowPathVision(loadingStation, 2.0));
//        addSequential(new FollowPath(loadingStation));
//        addParallel(new SmartCollect());
//        addSequential(new WaitCommand(0.35));
//        addSequential(new FollowPathReverse(backupAroundRocket));
    }

    @Override
    protected void initialize() {
        Drivetrain.getInstance().setYaw(180);
    }

}
