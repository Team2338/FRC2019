package team.gif.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import team.gif.lib.AutoPosition;
import team.gif.lib.TargetPosition;
import team.gif.robot.Constants;
import team.gif.robot.commands.backhatch.BackEject;
import team.gif.robot.commands.claw.SetClawMode;
import team.gif.robot.commands.claw.SetDeploy;
import team.gif.robot.commands.claw.SmartCollect;
import team.gif.robot.commands.drivetrain.FollowPath;
import team.gif.robot.commands.drivetrain.FollowPathReverse;
import team.gif.robot.commands.drivetrain.FollowPathVision;
import team.gif.robot.commands.elevator.SetElevatorPosition;
import team.gif.robot.subsystems.Drivetrain;

public class LeftShipSideHatch extends CommandGroup {

    private final Trajectory reverseApproach = Pathfinder.generate(new Waypoint[] {
            AutoPosition.L1_LEFT.getRobotWaypoint(0.0),
            AutoPosition.L1_LEFT.getRobotWaypoint(48.275),
            TargetPosition.LEFT_SHIP_NEAR.getRelativeWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 48 + 1, 48 + 8, 0.0),
            TargetPosition.LEFT_SHIP_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 + 1, 8, false)
    }, Constants.Drivetrain.slowConfig);

    private final Trajectory loadingStation = Pathfinder.generate(new Waypoint[] {
            TargetPosition.LEFT_SHIP_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2, 0, true),
            TargetPosition.LEFT_SHIP_NEAR.getRelativeWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 48, 48, Math.toRadians(180)),
//            TargetPosition.LEFT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 24, 0, false),
            TargetPosition.LEFT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, false)
    }, Constants.Drivetrain.normalConfig);

    private final Trajectory reverseReturn = Pathfinder.generate(new Waypoint[] {
            TargetPosition.LEFT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, true),
            TargetPosition.LEFT_SHIP_FRONT.getRelativeWaypoint(-Constants.Drivetrain.BUMPER_WIDTH - 36, -48, Math.toRadians(-90))
    }, Constants.Drivetrain.fastConfig);

    private final Trajectory finalApproach = Pathfinder.generate(new Waypoint[] {
            TargetPosition.LEFT_SHIP_FRONT.getRelativeWaypoint(-Constants.Drivetrain.BUMPER_WIDTH - 60, -48, Math.toRadians(90)),
            TargetPosition.LEFT_ROCKET_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 72, 0, false),
//            TargetPosition.LEFT_ROCKET_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, false)
    }, Constants.Drivetrain.normalConfig);

//    private final Trajectory reverseReturn = Pathfinder.generate(new Waypoint[] {
//            TargetPosition.LEFT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, true),
//            TargetPosition.LEFT_SHIP_MID.getRelativeWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 20, 40 + 10, 0),
//            TargetPosition.LEFT_SHIP_MID.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 60, 10, true)
//    }, Constants.Drivetrain.normalConfig);

//    private final Trajectory finalApproach = Pathfinder.generate(new Waypoint[] {
//            TargetPosition.LEFT_SHIP_MID.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 60, 0, false),
//            TargetPosition.LEFT_SHIP_MID.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, true)
//    }, Constants.Drivetrain.slowConfig);

    public LeftShipSideHatch() {
        addSequential(new FollowPathReverse(reverseApproach));
        addSequential(new SetDeploy(true));
        addParallel(new SetClawMode(true));
        addParallel(new SetElevatorPosition(Constants.Elevator.HATCH_LOW_POS));
        addSequential(new BackEject(0.5));
        addSequential(new FollowPathVision(loadingStation, 24));
        addParallel(new SmartCollect());
        addSequential(new WaitCommand(0.2));
        addSequential(new FollowPathReverse(reverseReturn));
//        addSequential(new FollowPath(finalApproach));
    }

    @Override
    protected void initialize() {
        Drivetrain.getInstance().setYaw(180);
    }

}
