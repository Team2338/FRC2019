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
import team.gif.robot.commands.drivetrain.FollowPathReverse;
import team.gif.robot.commands.drivetrain.FollowPathVision;
import team.gif.robot.commands.elevator.SetElevatorPosition;
import team.gif.robot.subsystems.Drivetrain;

public class CenterToRightShipFrontSideHatch extends CommandGroup {

    private final Trajectory reverseApproach = Pathfinder.generate(new Waypoint[] {
            AutoPosition.L1_CENTER.getRobotWaypoint(0),
            AutoPosition.L1_CENTER.getRobotWaypoint(48.275),
            TargetPosition.RIGHT_SHIP_FRONT.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 1, false)
    }, Constants.Drivetrain.slowConfig);

    private final Trajectory loadingStation = Pathfinder.generate(new Waypoint[] {
            TargetPosition.RIGHT_SHIP_FRONT.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2, 0, true),
            TargetPosition.RIGHT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4 - 48, 1, false),
            TargetPosition.RIGHT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 1, false)
    }, Constants.Drivetrain.normalConfig);

    private final Trajectory reverseReturn = Pathfinder.generate(new Waypoint[] {
            TargetPosition.RIGHT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, true),
            TargetPosition.RIGHT_SHIP_NEAR.getRelativeWaypoint(-Constants.Drivetrain.BUMPER_WIDTH / 2 - 12 + 12, -36 - 8, 0),
            TargetPosition.RIGHT_SHIP_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 48 + 12, -8, true)
    }, Constants.Drivetrain.fastConfig);

    private final Trajectory finalApproach = Pathfinder.generate(new Waypoint[] {
            TargetPosition.RIGHT_SHIP_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 48, 0, false),
            TargetPosition.RIGHT_SHIP_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, false)
    }, Constants.Drivetrain.slowConfig);

    public CenterToRightShipFrontSideHatch() {
        addSequential(new FollowPathReverse(reverseApproach));
        addSequential(new SetDeploy(true));
        addParallel(new SetClawMode(true));
        addParallel(new SetElevatorPosition(Constants.Elevator.HATCH_LOW_POS));
        addSequential(new BackEject(0.5));
        addSequential(new FollowPathVision(loadingStation, 36));
        addParallel(new SmartCollect());
        addSequential(new WaitCommand(0.2));
        addSequential(new FollowPathReverse(reverseReturn));
        addSequential(new FollowPathVision(finalApproach, 36));
    }

    @Override
    protected void initialize() {
        Drivetrain.getInstance().setYaw(180);
    }

}
