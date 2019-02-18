package team.gif.robot.commands.elevator;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team.gif.robot.subsystems.Elevator;

public class VoltageRamper extends Command {

    private final Elevator elevator = Elevator.getInstance();
    private final ShuffleboardTab tab = Shuffleboard.getTab("Debug");
    private NetworkTableEntry voltage;
    private NetworkTableEntry rps;
    private double timeout;

    public VoltageRamper(double timeout){
        this.timeout = timeout;
        setTimeout(timeout);
        requires(elevator);
    }

    @Override
    protected void initialize() {
        voltage = tab.add("ElevVoltage", 0.0).getEntry();
        rps = tab.add("ElevRPS", 0.0).getEntry();
        Shuffleboard.startRecording();
    }

    @Override
    protected void execute() {
        elevator.setPercentOutput(timeSinceInitialized() * (0.25 / 12.0));
        voltage.setDouble(elevator.getVoltage());
        rps.setDouble(elevator.getVelocityRPS());
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        Shuffleboard.stopRecording();
        elevator.setPercentOutput(0);
    }

}
