package team.gif.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import team.gif.lib.AutoPosition;
import team.gif.robot.Constants;
import team.gif.robot.commands.CommandTemplate;
import team.gif.robot.commands.claw.SetClawMode;
import team.gif.robot.commands.claw.SetDeploy;
import team.gif.robot.commands.drivetrain.FollowPath;
import team.gif.robot.commands.drivetrain.FollowPathReverse;
import team.gif.robot.commands.elevator.SetElevatorPosition;
import team.gif.robot.subsystems.Drivetrain;

public class Mobility extends CommandGroup {

    private final Trajectory mobility = Pathfinder.generate(new Waypoint[] {
            AutoPosition.L1_CENTER.getRobotWaypoint(0),
            AutoPosition.L1_CENTER.getRobotWaypoint(60),

    }, Constants.Drivetrain.normalConfig);

    public Mobility(AutoPosition position) {
//        if (position.getLevel() == 1) {
//                addSequential(new FollowPath(mobility));
//        } else {
//            addSequential(new CommandTemplate());
//        }
        addSequential(new FollowPathReverse(mobility));
        addSequential(new SetDeploy(true));
        addParallel(new SetClawMode(true));
        addParallel(new SetElevatorPosition(Constants.Elevator.HATCH_LOW_POS));
    }

    @Override
    protected void initialize() {
//        Drivetrain.getInstance().setYaw(180.0);
        Drivetrain.getInstance().setYaw(0.0);
        Drivetrain.getInstance().resetEncoders();
    }
}
