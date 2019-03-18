package team.gif.robot.commands.auto;

import edu.wpi.first.wpilibj.command.CommandGroup;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import team.gif.lib.AutoPosition;
import team.gif.robot.Constants;
import team.gif.robot.commands.CommandTemplate;
import team.gif.robot.commands.drivetrain.FollowPath;
import team.gif.robot.commands.drivetrain.FollowPathReverse;
import team.gif.robot.subsystems.Drivetrain;

public class Mobility extends CommandGroup {

    private final Trajectory mobility = Pathfinder.generate(new Waypoint[] {
            new Waypoint(48 + Constants.Drivetrain.BUMPER_LENGTH / 2, 0, 0),
            new Waypoint(48 + Constants.Drivetrain.BUMPER_LENGTH / 2 + 120, 0, 0),
    }, Constants.Drivetrain.config);

    public Mobility(AutoPosition position) {
        if (position.getLevel() == 1) {
                addSequential(new FollowPathReverse(mobility));
        } else {
            addSequential(new CommandTemplate());
        }
    }

    @Override
    protected void initialize() {
        Drivetrain.getInstance().setYaw(180.0);
        Drivetrain.getInstance().resetEncoders();
    }
}
