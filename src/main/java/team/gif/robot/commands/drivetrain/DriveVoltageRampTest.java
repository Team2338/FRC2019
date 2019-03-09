package team.gif.robot.commands.drivetrain;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import team.gif.robot.Constants;
import team.gif.robot.subsystems.Drivetrain;

public class DriveVoltageRampTest extends Command {

    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final ShuffleboardTab tab = Shuffleboard.getTab("Debug");
    private NetworkTableEntry leftVoltage;
    private NetworkTableEntry rightVoltage;
    private NetworkTableEntry leftVelocity;
    private NetworkTableEntry rightVelocity;

    public DriveVoltageRampTest(double timeout){
        setTimeout(timeout);
        requires(drivetrain);
    }

    @Override
    protected void initialize() {
        leftVoltage = tab.add("Left Drive Voltage", 0.0).getEntry();
        rightVoltage = tab.add("Right Drive Voltage", 0.0).getEntry();
        leftVelocity = tab.add("Left Drive Velocity", 0.0).getEntry();
        rightVelocity = tab.add("Right Drive Velocity", 0.0).getEntry();
        Shuffleboard.startRecording();
    }

    @Override
    protected void execute() {
        double percentOutput = timeSinceInitialized() * (0.25 / 12.0);
        drivetrain.setOutputs(percentOutput, percentOutput);
        leftVoltage.setDouble(drivetrain.getOutputVoltage()[0] / 12.0);
        rightVoltage.setDouble(drivetrain.getOutputVoltage()[1] / 12.0);
        leftVelocity.setDouble(drivetrain.getLeftVelTPS() / 4096.0);
        rightVelocity.setDouble(drivetrain.getRightVelTPS() / 4096.0);
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        Shuffleboard.stopRecording();
        drivetrain.setOutputs(0.0, 0.0);
    }

}
