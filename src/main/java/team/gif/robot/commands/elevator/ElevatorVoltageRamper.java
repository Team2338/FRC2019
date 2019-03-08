package team.gif.robot.commands.elevator;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import team.gif.robot.Constants;
import team.gif.robot.subsystems.Elevator;

public class ElevatorVoltageRamper extends Command {

    private final Elevator elevator = Elevator.getInstance();
    private final ShuffleboardTab tab = Shuffleboard.getTab("Debug");
    private NetworkTableEntry voltage;
    private NetworkTableEntry velocity;

    public ElevatorVoltageRamper(){
        requires(elevator);
    }

    @Override
    protected void initialize() {
        voltage = tab.add("Elevator Native Voltage", 0.0).getEntry();
        velocity = tab.add("Elevator Native Velocity", 0.0).getEntry();
        Shuffleboard.startRecording();
    }

    @Override
    protected void execute() {
        elevator.setPercentOutput(timeSinceInitialized() * (0.25 / 12.0));
        voltage.setDouble(elevator.getOutputVoltage() / 12.0 * 1024);
        velocity.setDouble(elevator.getVelTPS() / 10.0);
    }

    @Override
    protected boolean isFinished() {
        return !elevator.getFwdLimit();
    }

    @Override
    protected void end() {
        Shuffleboard.stopRecording();
        elevator.setPercentOutput(0);
    }

}
