package team.gif.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.subsystems.Elevator;

public class SetElevatorPosition extends Command {

    private Elevator elevate = Elevator.getInstance();
    private final double position;

    public SetElevatorPosition(double position) {
        this.position = position;
        requires(elevate);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {

        //Need to set that GRAV_FEED_FORWARD constant we need to test for
        elevate.setMotionMagic(position, Constants.Elevator.GRAV_FEED_FORWARD);

    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {

    }
}
