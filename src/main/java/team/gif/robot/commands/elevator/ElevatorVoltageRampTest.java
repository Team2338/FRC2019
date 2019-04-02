package team.gif.robot.commands.elevator;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import team.gif.lib.math.LinearRegression;
import team.gif.robot.Constants;
import team.gif.robot.subsystems.Elevator;

public class ElevatorVoltageRampTest extends Command {

    private final Elevator elevator = Elevator.getInstance();
    private LinearRegression regression;
    private double[] voltage, velocity;
    private int totalFrames;
    private int currentFrame;
    private boolean reverse;
    private int trim;

//    private final ShuffleboardTab tab = Shuffleboard.getTab("Debug");
//    private NetworkTableEntry voltage;
//    private NetworkTableEntry velocity;

    public ElevatorVoltageRampTest(double timeout, boolean reverse, int trim){
        requires(elevator);
        this.reverse = reverse;
        this.trim = trim;
        totalFrames = (int)timeout * 50 - trim;
    }

    @Override
    protected void initialize() {
        voltage = new double[totalFrames];
        velocity = new double[totalFrames];
        currentFrame = -trim;
    }

    @Override
    protected void execute() {
        double percentOutput = (reverse ? -1 : 1) * timeSinceInitialized() * (0.25 / 12.0);
//        double percentOutput = timeSinceInitialized() * -(0.25 / 12.0);
        elevator.setPercentOutput(percentOutput);
        if (currentFrame < totalFrames && currentFrame >= 0) {
            voltage[currentFrame] = elevator.getOutputVoltage() / 12 * 1023;
            velocity[currentFrame] = elevator.getVelTPS() / 10;
        }
        System.out.println("Frame: " + currentFrame + ", Voltage: " + elevator.getOutputVoltage() + ", Velocity: " + elevator.getVelTPS());
        currentFrame++;
    }

    @Override
    protected boolean isFinished() {
        return !(currentFrame < totalFrames);
    }

    @Override
    protected void end() {
        elevator.setPercentOutput(0.0);
        regression = new LinearRegression(velocity, voltage);
        System.out.println("Elev Result: " + regression.toString());
    }

}
