package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.robot.RobotMap;

public class HatchPunch extends Subsystem {

    private static HatchPunch instance;

    private final Solenoid punch;

    private HatchPunch() {
        punch = new Solenoid(RobotMap.HATCH_PUNCH_ID);
    }

    public static HatchPunch getInstance() {
        if (instance == null) {
            instance = new HatchPunch();
        }
        return instance;
    }

    public void setPunch(boolean out) {
        punch.set(out);
    }

    @Override
    protected void initDefaultCommand() {

    }
}
