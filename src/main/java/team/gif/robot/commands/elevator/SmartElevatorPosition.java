package team.gif.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.subsystems.Claw;
import team.gif.robot.subsystems.Elevator;


public class SmartElevatorPosition extends Command {

    public enum GenericPosition {
        LOW,
        MID,
        HIGH;
    }

    private final Elevator elevator = Elevator.getInstance();
    private final Claw claw = Claw.getInstance();
    private final GenericPosition position;

    public SmartElevatorPosition(GenericPosition position) {
        this.position = position;
        requires(elevator);
    }

    @Override
    protected void initialize() {
        boolean isHatchMode = claw.isHatchMode();
        if (claw.isDeployed()) {
            switch (position) {
                case LOW:
                    new SetElevatorPosition(isHatchMode ? Constants.Elevator.HATCH_LOW_POS :
                            Constants.Elevator.CARGO_LOW_POS).start();
                    break;
                case MID:
                    new SetElevatorPosition(isHatchMode ? Constants.Elevator.HATCH_MID_POS :
                            Constants.Elevator.CARGO_MID_POS).start();
                    break;
                case HIGH:
                    new SetElevatorPosition(isHatchMode ? Constants.Elevator.HATCH_HIGH_POS :
                            Constants.Elevator.CARGO_HIGH_POS).start();
            }
        }
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {

    }
}
