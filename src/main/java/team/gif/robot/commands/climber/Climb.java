package team.gif.robot.commands.climber;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import team.gif.robot.Constants;
import team.gif.robot.OI;
import team.gif.robot.subsystems.Climber;
import team.gif.robot.subsystems.Drivetrain;

public class Climb extends Command {

    private final Climber climber = Climber.getInstance();
    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final double kP = Constants.Climber.GYRO_SENSITIVITY;
    private double currTime;
    private double timeout;

    public Climb(double timeout) {
//        super(timeout);
        this.timeout = timeout;
        requires(climber);
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        currTime = Timer.getFPGATimestamp();

        if (timeSinceInitialized() < timeout) {
            double[] errors = climber.getBalanceError(drivetrain.getYawPitchRoll());
//            double[] errors = new double[]{0,0,0,0};

            double frontLeftPercent = 0.84 - kP * errors[0];
            double rearLeftPercent = 0.65 - kP * errors[0];
            double frontRightPercent = 0.84 - kP * errors[0];
            double rearRightPercent = 0.65 - kP * errors[0];

            climber.setPistons(frontLeftPercent, rearLeftPercent, frontRightPercent, rearRightPercent, currTime);
        } else {
            climber.setClimbDrive(-OI.getInstance().driver.getY(GenericHID.Hand.kLeft));
        }
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        climber.setPistons(0.0, 0.0, 0.0, 0.0, 0.0);
    }
}
