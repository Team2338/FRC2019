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

public class LeftNearShipHatch extends CommandGroup {

    private final Trajectory approach = Pathfinder.generate(new Waypoint[] {
            AutoPosition.L1_LEFT.getWaypoint(),
            new Waypoint(AutoPosition.L1_LEFT.getWaypoint().x + 48.275, AutoPosition.L1_LEFT.getWaypoint().y, 0.0),
            TargetPosition.LEFT_SHIP_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2, 4.5, false)
    }, Constants.Drivetrain.config);

    private final Trajectory loadingStation = Pathfinder.generate(new Waypoint[] {
            TargetPosition.RIGHT_SHIP_NEAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2, 0, true),
            TargetPosition.RIGHT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 24, 0, false)
    }, Constants.Drivetrain.config);


    public LeftNearShipHatch() {
        addSequential(new FollowPathReverse(approach));
        addParallel(new SetDeploy(true));
        addParallel(new SetClawMode(true));
        addParallel(new SetElevatorPosition(Constants.Elevator.HATCH_LOW_POS));
        addSequential(new BackEject(1.0));
        addSequential(new FollowPath(loadingStation));
//        addParallel(new SmartCollect());
//        addSequential(new WaitCommand(0.25));
//        addSequential(new FollowPathReverse(backup));
    }

    @Override
    protected void initialize() {
        Drivetrain.getInstance().setYaw(180);
    }

//    private class GoToShip extends CommandGroup {
//        public GoToShip() {
//            addParallel(new FollowPathReverse(approach));
//            addSequential(new WaitCommand(3.5));
//            addSequential(new SetDeploy(true));
//            addSequential(new SetClawMode(true));
//            addSequential(new SetElevatorPosition(Constants.Elevator.HATCH_LOW_POS));
//        }
//    }

}
