package team.gif.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.WPILibVersion;
import team.gif.lib.AutoMode;
import team.gif.lib.AutoPosition;
import team.gif.robot.commands.auto.AutoTemplate;
import team.gif.robot.subsystems.Drivetrain;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private Drivetrain drivetrain = Drivetrain.getInstance();

    private final SendableChooser<AutoPosition> autoPositionChooser = new SendableChooser<>();
    private final SendableChooser<AutoMode> autoModeChooser = new SendableChooser<>();
    private AutoPosition selectedAutoPosition;
    private AutoMode selectedAutoMode;
    private CommandGroup auto;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        autoPositionChooser.setDefaultOption("Left", AutoPosition.LEFT);
        autoPositionChooser.addOption("Center", AutoPosition.CENTER);
        autoPositionChooser.addOption("Right", AutoPosition.RIGHT);
        autoPositionChooser.addOption("High Left", AutoPosition.HIGH_LEFT);
        autoPositionChooser.addOption("High Right", AutoPosition.HIGH_RIGHT);
        SmartDashboard.putData("Field Position", autoPositionChooser);

        autoModeChooser.setDefaultOption("Do Something", AutoMode.DO_SOMETHING);
        autoModeChooser.addOption("Do Something Else", AutoMode.DO_SOMETHING_ELSE);
        SmartDashboard.putData("Auto Mode", autoModeChooser);

        drivetrain.beginOdometry();

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

    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {

    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() {
        selectedAutoPosition = autoPositionChooser.getSelected();
        selectedAutoMode = autoModeChooser.getSelected();
        System.out.println("Position: " + selectedAutoPosition + ", Mode: " + selectedAutoMode);

        if (selectedAutoMode == AutoMode.DO_SOMETHING) {
            if (selectedAutoPosition == AutoPosition.LEFT) {
                auto = new AutoTemplate();
            } else if (selectedAutoPosition == AutoPosition.CENTER) {
                auto = new AutoTemplate();
            } else if (selectedAutoPosition == AutoPosition.RIGHT) {
                auto = new AutoTemplate();
            }
        } else if (selectedAutoMode == AutoMode.DO_SOMETHING_ELSE) {
            if (selectedAutoPosition == AutoPosition.LEFT) {
                auto = new AutoTemplate();
            } else if (selectedAutoPosition == AutoPosition.CENTER) {
                auto = new AutoTemplate();
            } else if (selectedAutoPosition == AutoPosition.RIGHT) {
                auto = new AutoTemplate();
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
        if (OI.getInstance().getAutoCancel()) {
            auto.cancel();
        }
    }

    /**
     * This function is called once each time the robot enters teleop mode.
     */
    @Override
    public void teleopInit() {
        if (auto != null) {
            auto.cancel();
        }
    }

    /**
     * This function is called periodically during teleop mode.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

}
