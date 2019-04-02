package team.gif.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.subsystems.Elevator;

public class SetElevatorPosition extends Command {

    private final Elevator elevator = Elevator.getInstance();
    private final double position;

    public SetElevatorPosition(double position) {
        if (position > Constants.Elevator.MAX_POS) { position = Constants.Elevator.MAX_POS; }
        if (position < Constants.Elevator.MIN_POS) { position = Constants.Elevator.MIN_POS; }
        this.position = position;

        requires(elevator);
    }

    @Override
    protected void initialize() {
        if (position > elevator.getPosition()) {
            elevator.setCruiseVelocity(Constants.Elevator.MAX_VELOCITY);
            elevator.configF(Constants.Elevator.F);
            elevator.setMotionMagic(position, Constants.Elevator.GRAV_FEED_FORWARD);
        } else {
            elevator.setCruiseVelocity(Constants.Elevator.REV_MAX_VELOCITY);
            elevator.configF(Constants.Elevator.REV_F);
            elevator.setMotionMagic(position, Constants.Elevator.REV_GRAV_FEED_FORWARD);
        }

    }

    @Override
    protected void execute() {
//        if (!elevator.getFwdLimit() && elevator.getClosedLoopError() < 0) {
//            elevator.setCruiseVelocity(400);
//        } else {
//            elevator.setCruiseVelocity(Constants.Elevator.MAX_VELOCITY);
//        }
    }

    @Override
    protected boolean isFinished() {
        return elevator.isFinished();
    }

    @Override
    protected void end() {

    }
}
