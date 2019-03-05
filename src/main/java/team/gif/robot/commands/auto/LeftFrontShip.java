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
import team.gif.robot.commands.claw.SetDeploy;
import team.gif.robot.commands.claw.SmartCollect;
import team.gif.robot.commands.drivetrain.FollowPath;
import team.gif.robot.commands.drivetrain.FollowPathReverse;
import team.gif.robot.commands.elevator.SetElevatorPosition;
import team.gif.robot.subsystems.Drivetrain;

public class LeftFrontShip extends CommandGroup {

    private final Trajectory reverseApproach = Pathfinder.generate(new Waypoint[] {
            AutoPosition.L1_LEFT.getWaypoint(),
            new Waypoint(AutoPosition.L1_LEFT.getWaypoint().x + 48.275, AutoPosition.L1_LEFT.getWaypoint().y, 0.0),
            TargetPosition.LEFT_SHIP_FRONT.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 + 1.5, 4.5, false)
    }, Constants.Drivetrain.config);

    private final Trajectory loadingStation = Pathfinder.generate(new Waypoint[] {
            TargetPosition.LEFT_SHIP_FRONT.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2, 0,true),
//            TargetPosition.LEFT_SHIP_FRONT.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2, 0, true),
//            TargetPosition.LEFT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, false),
            TargetPosition.LEFT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4 - 24, -6, false),
    }, Constants.Drivetrain.config);

    private final Trajectory backup = Pathfinder.generate(new Waypoint[] {
            TargetPosition.LEFT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, true),
            TargetPosition.LEFT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4 - 24, 0, true)
    }, Constants.Drivetrain.config);

    public LeftFrontShip() {
        addSequential(new FollowPathReverse(reverseApproach));
        addParallel(new SetDeploy(true));
        addParallel(new SetElevatorPosition(Constants.Elevator.HATCH_LOW_POS));
        addSequential(new BackEject(1.5));
        addSequential(new FollowPath(loadingStation));
//        addParallel(new SmartCollect());
//        addSequential(new WaitCommand(0.25));
//        addSequential(new FollowPathReverse(backup));
    }

    @Override
    protected void initialize() {
        Drivetrain.getInstance().setYaw(180);
    }

}
