package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.OI;
import team.gif.robot.subsystems.Climber;

public class RaiseRear extends Command {

    private final Climber climber = Climber.getInstance();

    public RaiseRear() {
        requires(climber);
    }

    @Override
    protected void initialize() {
        climber.setRearRack(false);
        climber.setWinchPercent(0.7); // Rate of climb retract
        climber.setDeployed(false);
        climber.setWinchCurrentLimit(10);
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        climber.setRearRack(false);
        climber.setWinchPercent(0.0);
    }
}
