package team.gif.robot;

import edu.wpi.first.hal.PowerJNI;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.WPILibVersion;
import team.gif.robot.commands.auto.DoNothing;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private enum FieldPosition {
        LEFT, CENTER, RIGHT
    }

    private enum AutoMode {
        DO_SOMETHING, DO_SOMETHING_ELSE
    }

    private final SendableChooser<FieldPosition> fieldPositionChooser = new SendableChooser<>();
    private final SendableChooser<AutoMode> autoModeChooser = new SendableChooser<>();
    private FieldPosition selectedFieldPosition;
    private AutoMode selectedAutoMode;
    private String gameData;
    private Command auto;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        fieldPositionChooser.setDefaultOption("Left", FieldPosition.LEFT);
        fieldPositionChooser.addOption("Center", FieldPosition.CENTER);
        fieldPositionChooser.addOption("Right", FieldPosition.RIGHT);
        SmartDashboard.putData("Field Position", fieldPositionChooser);

        autoModeChooser.setDefaultOption("Do Something", AutoMode.DO_SOMETHING);
        autoModeChooser.addOption("Do Something Else", AutoMode.DO_SOMETHING_ELSE);
        SmartDashboard.putData("Auto Mode", autoModeChooser);

        System.out.println("Robot Initialized. WPILib V" + WPILibVersion.Version);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {

    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() {
        selectedFieldPosition = fieldPositionChooser.getSelected();
        selectedAutoMode = autoModeChooser.getSelected();
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        System.out.println("Position: " + selectedFieldPosition + ", Auto: " + selectedAutoMode + ", Data: " + gameData);

        if (selectedAutoMode == AutoMode.DO_SOMETHING) {
            if (selectedFieldPosition == FieldPosition.LEFT) {
                auto = new DoNothing();
            } else if (selectedFieldPosition == FieldPosition.CENTER) {
                auto = new DoNothing();
            } else if (selectedFieldPosition == FieldPosition.RIGHT) {
                auto = new DoNothing();
            }
        } else if (selectedAutoMode == AutoMode.DO_SOMETHING_ELSE) {
            if (selectedFieldPosition == FieldPosition.LEFT) {
                auto = new DoNothing();
            } else if (selectedFieldPosition == FieldPosition.CENTER) {
                auto = new DoNothing();
            } else if (selectedFieldPosition == FieldPosition.RIGHT) {
                auto = new DoNothing();
            }
        }

        if (auto != null) {
            auto.start();
        }
    }

    /**
     * This function is called periodically during autonomous mode.
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called once each time the robot enters teleop mode.
     */
    @Override
    public void teleopInit() {

    }

    /**
     * This function is called periodically during teleop mode.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
    @Override
    public void testInit() {

    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {

    }
}
