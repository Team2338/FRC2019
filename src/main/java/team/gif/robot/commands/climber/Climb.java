package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.OI;
import team.gif.robot.subsystems.Climber;
import team.gif.robot.subsystems.Drivetrain;

public class Climb extends Command {

    private final Climber climber = Climber.getInstance();
    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final double P = Constants.Climber.GYRO_SENSITIVITY;

    public Climb() {
        requires(climber);
    }

    @Override
    protected void initialize() {
        climber.setFrontRack(true);
        climber.setRearRack(true);
        climber.setDeployed(true);
        climber.setWinchCurrentLimit(0);
    }

    @Override
    protected void execute() {
        double pitch = drivetrain.getYawPitchRoll()[1];

        if (climber.getWinchPos() < 21000) {
            climber.setWinchPercent(-(0.225 + P * pitch)); // Rate of climb
        } else {
            climber.setWinchPercent(0.1);
        }

        climber.setDrive(5.0 * -OI.getInstance().driver.getY(GenericHID.Hand.kLeft));
    }

    @Override
    protected boolean isFinished() {
        return climber.getWinchPos() > 21000;
    }

    @Override
    protected void end() {
        climber.setWinchPercent(0.15);
        climber.setDrive(0.0);
    }
}
