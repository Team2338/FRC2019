package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.robot.RobotMap;

public class BackHatch extends Subsystem {

    private static BackHatch instance;

    private final Solenoid punch;

    private BackHatch() {
        punch = new Solenoid(RobotMap.BACK_HATCH_ID);
    }

    public static BackHatch getInstance() {
        if (instance == null) {
            instance = new BackHatch();
        }
        return instance;
    }

    public void set(boolean out) {
        punch.set(out);
    }

    @Override
    protected void initDefaultCommand() {

    }
}
