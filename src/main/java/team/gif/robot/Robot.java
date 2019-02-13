package team.gif.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.util.WPILibVersion;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import team.gif.lib.AutoMode;
import team.gif.lib.AutoPosition;
import team.gif.lib.drivers.Limelight;
import team.gif.robot.commands.CommandGroupTemplate;
import team.gif.robot.commands.auto.Mobility;
import team.gif.robot.subsystems.Claw;
import team.gif.robot.subsystems.Climber;
import team.gif.robot.subsystems.Drivetrain;
import team.gif.robot.subsystems.Elevator;

import java.io.File;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private Claw claw = Claw.getInstance();
    private Climber climber = Climber.getInstance();
    private Drivetrain drivetrain = Drivetrain.getInstance();
    private Elevator elevator = Elevator.getInstance();
    private Limelight limelight = Limelight.getInstance();
//    private SerialPort port = new SerialPort(9600, SerialPort.Port.kUSB);

    private final SendableChooser<AutoPosition> autoPositionChooser = new SendableChooser<>();
    private final SendableChooser<AutoMode> autoModeChooser = new SendableChooser<>();
    private AutoPosition selectedAutoPosition;
    private AutoMode selectedAutoMode;
    private Command auto;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        autoPositionChooser.setDefaultOption("L1: Left", AutoPosition.L1_LEFT);
        autoPositionChooser.addOption("L1: Center", AutoPosition.L1_CENTER);
        autoPositionChooser.addOption("L1: Right", AutoPosition.L1_RIGHT);
        autoPositionChooser.addOption("L2: Left", AutoPosition.L2_LEFT);
        autoPositionChooser.addOption("L2: Right", AutoPosition.L2_RIGHT);
        Shuffleboard.getTab("Auto").add("Field Position", autoPositionChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

        autoModeChooser.setDefaultOption("Mobility", AutoMode.MOBILITY);
        autoModeChooser.addOption("Double Rocket", AutoMode.DOUBLE_ROCKET);
        autoModeChooser.addOption("Cargo Ship: Front", AutoMode.CARGO_SHIP_FRONT);
        autoModeChooser.addOption("Cargo Ship: Near", AutoMode.CARGO_SHIP_NEAR);
        autoModeChooser.addOption("Cargo Ship: Mid", AutoMode.CARGO_SHIP_MID);
        autoModeChooser.addOption("Cargo Ship: Far", AutoMode.CARGO_SHIP_FAR);
        Shuffleboard.getTab("Auto").add("Auto Mode", autoModeChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

        selectedAutoPosition = autoPositionChooser.getSelected();
        selectedAutoMode = autoModeChooser.getSelected();

//        drivetrain.beginOdometry();

        System.out.println("Robot Initialized. WPILib Version " + WPILibVersion.Version);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, and tele-op.
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
        if (!selectedAutoPosition.equals(autoPositionChooser.getSelected()) || !selectedAutoMode.equals(autoModeChooser.getSelected())) {
            updateSelectedAuto();
        }
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() {
        updateSelectedAuto();

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
     * This function is called once each time the robot enters tele-op mode.
     */
    @Override
    public void teleopInit() {
        if (auto != null) {
            auto.cancel();
        }

        Shuffleboard.selectTab("TeleOp");
    }

    /**
     * This function is called periodically during tele-op mode.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * Retrieves the selected auto mode and position from {@link Shuffleboard} and updates the relevant member
     * variables. Depending on the selected mode and position, an auto {@link Command} is chosen and stored to be run
     * at the beginning of autonomous. The selection of each of these parameters is printed to the console.
     *
     * All combinations that do not have a designated auto should be assigned a default command and print a message.
     */
    private void updateSelectedAuto() {
        selectedAutoPosition = autoPositionChooser.getSelected();
        selectedAutoMode = autoModeChooser.getSelected();
        System.out.println("Position: " + selectedAutoPosition + ", Mode: " + selectedAutoMode);

        if (selectedAutoMode == AutoMode.MOBILITY) {
            auto = new Mobility(selectedAutoPosition);
        } else if (selectedAutoMode == AutoMode.DOUBLE_ROCKET) {
            if (selectedAutoPosition == AutoPosition.L1_LEFT) {
                auto = new CommandGroupTemplate();
            } else if (selectedAutoPosition == AutoPosition.L1_CENTER) {
                auto = new Mobility(selectedAutoPosition);
                System.out.println("[WARNING]: This combination does not have an auto command.");
            } else if (selectedAutoPosition == AutoPosition.L1_RIGHT) {
                auto = new CommandGroupTemplate();
            } else if (selectedAutoPosition == AutoPosition.L2_LEFT) {
                auto = new CommandGroupTemplate();
            } else if (selectedAutoPosition == AutoPosition.L2_RIGHT) {
                auto = new CommandGroupTemplate();
            }
        }

        System.out.println("Selected Auto: " + auto);
    }

}
