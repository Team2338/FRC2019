package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.OI;
import team.gif.robot.subsystems.Climber;

public class ClimberStandby extends Command {

    private Climber climber = Climber.getInstance();

    public ClimberStandby() {
        requires(climber);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        if (climber.isDeployed()) {
            climber.setWinchPercent(0.15);
            climber.setDrive(4.0 * OI.getInstance().driver.getY(GenericHID.Hand.kLeft));
        } else {
            climber.setWinchPercent(0.05);
            climber.setWinchCurrentLimit(10);
            climber.setDrive(0.0);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        climber.setDrive(0.0);
    }
}
