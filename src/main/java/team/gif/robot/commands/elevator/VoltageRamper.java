package team.gif.robot.commands.elevator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team.gif.robot.subsystems.Elevator;

public class VoltageRamper extends Command {
    private final Elevator elevator;
    private int percentIncrease;
    private double lastTime;
    private double timeout;

    public VoltageRamper(double timeout){
        elevator = Elevator.getInstance();
        percentIncrease = 0;
        this.timeout = timeout;
        lastTime = Timer.getFPGATimestamp();
        requires(elevator);
    }

    @Override
    protected void initialize() {

    }
    @Override
    protected void execute() {

        if (Timer.getFPGATimestamp() - lastTime > .1) {
            elevator.setPercentOutput(percentIncrease);
            lastTime = Timer.getFPGATimestamp();
            percentIncrease += (0.25 / 12.0)*.1;

            SmartDashboard.putNumber("Voltage Running At", elevator.getMotorVoltage());
            SmartDashboard.putNumber("Speed in Radians/Sec", elevator.getMotorSpeed());
        }
    }

    @Override
    protected boolean isFinished() {
        return elevator.isStopped();
    }

    @Override
    protected void end() {
        elevator.setPercentOutput(0);

    }

}
