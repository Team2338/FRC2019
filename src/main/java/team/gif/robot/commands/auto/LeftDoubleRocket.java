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
import team.gif.robot.commands.claw.SmartEject;
import team.gif.robot.commands.drivetrain.FollowPathReverse;
import team.gif.robot.commands.drivetrain.FollowPathVision;
import team.gif.robot.commands.elevator.SetElevatorPosition;
import team.gif.robot.subsystems.Drivetrain;

public class LeftDoubleRocket extends CommandGroup {

    private final Trajectory reverseApproach = Pathfinder.generate(new Waypoint[] {
            AutoPosition.L1_LEFT.getRobotWaypoint(0),
            AutoPosition.L1_LEFT.getRobotWaypoint(48.275),
            TargetPosition.LEFT_ROCKET_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2, -2, false)
    }, Constants.Drivetrain.slowConfig);

    private final Trajectory loadingStation = Pathfinder.generate(new Waypoint[] {
            TargetPosition.LEFT_ROCKET_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2, 0, true),
//            TargetPosition.LEFT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 6 - 72, -5, false),
            TargetPosition.LEFT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 6, -3.5, false)
    }, Constants.Drivetrain.normalConfig);

    private final Trajectory backupAroundRocket = Pathfinder.generate(new Waypoint[] {
            TargetPosition.LEFT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, true),
//            TargetPosition.LEFT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4 - 24, 0, true),
            TargetPosition.LEFT_ROCKET_MID.getRelativeWaypoint(-Constants.Drivetrain.BUMPER_WIDTH / 2 - 6, 0, 0),
            TargetPosition.LEFT_ROCKET_FAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4 - 32, -7, true)
    }, Constants.Drivetrain.fastConfig);

    private final Trajectory finalApproach = Pathfinder.generate(new Waypoint[] {
            TargetPosition.LEFT_ROCKET_FAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4 - 32, 0, false),
            TargetPosition.LEFT_ROCKET_FAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, false),
    }, Constants.Drivetrain.slowConfig);

    public LeftDoubleRocket() {
        addSequential(new FollowPathReverse(reverseApproach));
        addSequential(new SetDeploy(true));
        addParallel(new SetClawMode(true));
        addParallel(new SetElevatorPosition(Constants.Elevator.HATCH_LOW_POS));
        addSequential(new BackEject(0.5));
        addSequential(new FollowPathVision(loadingStation, 36));
        addParallel(new SmartCollect());
        addSequential(new WaitCommand(0.2));
        addSequential(new FollowPathReverse(backupAroundRocket));
        addSequential(new WaitCommand(0.25));
        addSequential(new FollowPathVision(finalApproach, 32));
//        addParallel(new SmartEject());
    }

    @Override
    protected void initialize() {
        Drivetrain.getInstance().setYaw(180);
    }

}
