package team.gif.robot.commands.claw;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.commands.elevator.SetElevatorPosition;
import team.gif.robot.commands.elevator.SmartElevatorPosition;
import team.gif.robot.subsystems.Claw;
import team.gif.robot.subsystems.Elevator;

public class SmartCollect extends Command {

    private final Claw claw = Claw.getInstance();
    private final Elevator elevator = Elevator.getInstance();

    public SmartCollect() {
        requires(claw);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        if (claw.isHatchMode()) {
            claw.deployHooks(true);
        } else {
            claw.setIntake(claw.hasBall() ? 0.1 : 0.5);
            if (claw.hasBall() && elevator.getPosition() < Constants.Elevator.CARGO_LOW_POS) {
                new SetElevatorPosition(Constants.Elevator.CARGO_LOW_POS).start();
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        claw.setIntake(0.0);
    }
}
