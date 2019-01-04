package team.gif.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import team.gif.robot.commands.drive.DriveTeleOp;

public class Drive extends Subsystem {

    private static Drive instance;

    private Drive() {

    }

    public static Drive getInstance() {
        if (instance == null) {
            instance = new Drive();
        }
        return instance;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveTeleOp());
    }
}
