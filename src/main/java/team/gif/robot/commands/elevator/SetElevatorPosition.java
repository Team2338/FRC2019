package team.gif.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.subsystems.Elevator;

public class SetElevatorPosition extends Command {

    private final Elevator elevator = Elevator.getInstance();
    private final double position;

    public SetElevatorPosition(double position) {
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
        elevator.setMotionMagic(position);
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return elevator.isFinished();
    }

    @Override
    protected void end() {

    }
}
