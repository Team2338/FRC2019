package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.lib.drivers.Limelight;

public class Claw extends Subsystem {

    private static Claw instance;

    private Claw() {

    }

    public static Claw getInstance() {
        if (instance == null) {
            instance = new Claw();
        }
        return instance;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
