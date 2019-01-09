package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {

    private static Elevator instance;

    private Elevator() {

    }

    public static Elevator getInstance() {
        if (instance == null) {
            instance = new Elevator();
        }
        return instance;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
