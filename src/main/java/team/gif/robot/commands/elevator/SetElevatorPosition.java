package team.gif.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.subsystems.Elevator;

public class SetElevatorPosition extends Command {

    private final Elevator elevator;
    private final double position;

    public SetElevatorPosition(double position) {
        elevator = Elevator.getInstance();

        if (position > Constants.Elevator.MAX_POS) {
            position = Constants.Elevator.MAX_POS;
        } else if (position < Constants.Elevator.MIN_POS) {
            position = Constants.Elevator.MIN_POS;
        }
        this.position = position;

        requires(elevator);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {

        //Need to set that GRAV_FEED_FORWARD constant we need to test for
        elevator.setMotionMagic(position, Constants.Elevator.GRAV_FEED_FORWARD);

    }

    @Override
    protected boolean isFinished() {
        return elevator.isStopped();
    }

    @Override
    protected void end() {

    }
}
