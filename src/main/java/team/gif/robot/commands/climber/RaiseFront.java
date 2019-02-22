package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.OI;
import team.gif.robot.subsystems.Climber;

public class RaiseFront extends Command {

    private Climber climber = Climber.getInstance();

    public RaiseFront() {
        requires(climber);
    }

    @Override
    protected void initialize() {
        climber.setPistons(false, true);
    }

    @Override
    protected void execute() {
        climber.setDrive(5.0 * -OI.getInstance().driver.getY(GenericHID.Hand.kLeft));
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        climber.setDrive(0.0);
        climber.setPistons(false, false);
    }
}
