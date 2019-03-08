package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.OI;
import team.gif.robot.subsystems.Claw;
import team.gif.robot.subsystems.Climber;

public class RaiseFront extends Command {

    private Climber climber = Climber.getInstance();
    private Claw claw = Claw.getInstance();

    public RaiseFront() {
        requires(climber);
    }

    @Override
    protected void initialize() {
        climber.setFrontRack(false);
        claw.deployClaw(true);
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
