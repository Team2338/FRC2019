package team.gif.lib;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class VariableRateSolenoid {

    private final DoubleSolenoid solenoid;

    public VariableRateSolenoid(int forwardChannel, int reverseChannel) {
        solenoid = new DoubleSolenoid(forwardChannel, reverseChannel);
    }

    public VariableRateSolenoid(int moduleNumber, int forwardChannel, int reverseChannel) {
        solenoid = new DoubleSolenoid(moduleNumber, forwardChannel, reverseChannel);
    }

    public void setPercentOutput(double percent, double period, double time) {
        double waveTime = time % period;

        if (percent > 1.0) percent = 1.0;
        if (percent < -1.0) percent = -1.0;

        if (waveTime < Math.abs(percent * period)) {
            solenoid.set(percent > 0 ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        } else {
            solenoid.set(DoubleSolenoid.Value.kOff);
        }
    }
}
