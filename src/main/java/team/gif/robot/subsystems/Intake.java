package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

    private static Intake instance;

    private Intake() {

    }

    public static Intake getInstance() {
        if (instance == null) {
            instance = new Intake();
        }
        return instance;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
