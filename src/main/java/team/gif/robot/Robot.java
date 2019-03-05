package team.gif.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.util.WPILibVersion;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import team.gif.lib.AutoMode;
import team.gif.lib.AutoPosition;
import team.gif.lib.TargetPosition;
import team.gif.lib.drivers.Limelight;
import team.gif.robot.commands.CommandGroupTemplate;
import team.gif.robot.commands.auto.*;
import team.gif.robot.commands.drivetrain.DriveTeleOp;
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
    private OI oi = OI.getInstance();
    private Limelight limelight = Limelight.getInstance();
    private Compressor compressor = new Compressor();
//    private SerialPort port = new SerialPort(9600, SerialPort.Port.kUSB);

    private final ShuffleboardTab autoTab = Shuffleboard.getTab("Auto");
    private final ShuffleboardTab teleopTab = Shuffleboard.getTab("TeleOp");
    private final ShuffleboardTab debug = Shuffleboard.getTab("Debug");
    private final SendableChooser<AutoPosition> autoPositionChooser = new SendableChooser<>();
    private final SendableChooser<AutoMode> autoModeChooser = new SendableChooser<>();
    private AutoPosition selectedAutoPosition;
    private AutoMode selectedAutoMode;
    private Command auto;
    private boolean autoCanceled = false;

//    private final Trajectory backupAroundRocket = Pathfinder.generate(new Waypoint[] {
//            TargetPosition.RIGHT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4, 0, true),
//            TargetPosition.RIGHT_LOADING_STATION.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4 - 24, 0, true),
//            new Waypoint(229.125, -110, 0.0),
//            TargetPosition.RIGHT_ROCKET_FAR.getRobotWaypoint(-Constants.Drivetrain.BUMPER_LENGTH / 2 - 4 - 24, 36, true)
//    }, Constants.Drivetrain.config);

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
        autoTab.add("Field Position", autoPositionChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

        autoModeChooser.setDefaultOption("Mobility", AutoMode.MOBILITY);
        autoModeChooser.addOption("Double Rocket", AutoMode.DOUBLE_ROCKET);
        autoModeChooser.addOption("Cargo Ship: Front Left", AutoMode.CARGO_SHIP_FRONT_LEFT);
        autoModeChooser.addOption("Cargo Ship: Front Right", AutoMode.CARGO_SHIP_FRONT_RIGHT);
        autoModeChooser.addOption("Cargo Ship: Near", AutoMode.CARGO_SHIP_NEAR);
        autoModeChooser.addOption("Cargo Ship: Mid", AutoMode.CARGO_SHIP_MID);
        autoModeChooser.addOption("Cargo Ship: Far", AutoMode.CARGO_SHIP_FAR);
        autoTab.add("Auto Mode", autoModeChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

        selectedAutoPosition = autoPositionChooser.getSelected();
        selectedAutoMode = autoModeChooser.getSelected();

//        Pathfinder.writeToCSV(new File("C:\\Users\\Connor\\Desktop\\backupRocket.csv"), backupAroundRocket);
//        drivetrain.beginOdometry();

        System.out.println("Robot Initialized. WPILib Version " + WPILibVersion.Version);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, and tele-op.
     *
     * <P>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
//        System.out.println(drivetrain.getHeadingDegrees());
//        leftVel.setDouble(drivetrain.getLeftVelRPS());
    }

    @Override
    public void disabledInit() {
        drivetrain.setBrakeMode(false);
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
        compressor.stop();
        drivetrain.setBrakeMode(false);
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

        if (oi.dSpecial.get() || oi.aSpecial.get()){
            Scheduler.getInstance().removeAll();
//            teleopInit();
//            new DriveTeleOp().start();
        }

    }

    /**
     * This function is called once each time the robot enters tele-op mode.
     */
    @Override
    public void teleopInit() {
        if (auto != null) {
            auto.cancel();
        }

        compressor.start();
        drivetrain.setBrakeMode(true);
        limelight.setCamMode(1);
        limelight.setLEDMode(1);

        Shuffleboard.selectTab("TeleOp");
    }

    /**
     * This function is called periodically during tele-op mode.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
//        System.out.println("Heading: " + drivetrain.getHeadingDegrees());
//        System.out.println("LeftDistInches: " + drivetrain.getLeftPosInches());
//        System.out.println("RightDistInches: " + drivetrain.getRightPosInches());
//        System.out.println("Has Target: " + limelight.hasTarget());
//        System.out.println("Offset: " + limelight.getXOffset());
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
                auto = new LeftDoubleRocket();
            } else if (selectedAutoPosition == AutoPosition.L1_RIGHT) {
                auto = new RightDoubleRocket();
            } else {
                auto = new Mobility(selectedAutoPosition);
                DriverStation.reportError("This combination does not have an auto command.", false);
                System.out.println("WARNING: This combination does not have an auto command.");
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_FRONT_LEFT) {
            if (selectedAutoPosition == AutoPosition.L1_LEFT) {
                auto = new LeftFrontShip();
            } else if (selectedAutoPosition == AutoPosition.L1_CENTER) {
                auto = new CenterFrontLeftShip();
            } else {
                auto = new Mobility(selectedAutoPosition);
                System.out.println("WARNING: This combination does not have an auto command.");
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_FRONT_RIGHT) {
            if (selectedAutoPosition == AutoPosition.L1_CENTER) {
                auto = new CenterFrontRightShip();
            } else if (selectedAutoPosition == AutoPosition.L1_RIGHT) {
                auto = new RightFrontShip();
            } else {
                auto = new Mobility(selectedAutoPosition);
                System.out.println("WARNING: This combination does not have an auto command.");
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_NEAR) {
            if (selectedAutoPosition == AutoPosition.L1_LEFT) {
                auto = new LeftNearShipHatch();
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_MID) {

        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_FAR) {

        } else {
            auto = new Mobility(selectedAutoPosition);
            System.out.println("[WARNING]: This combination does not have an auto command.");
        }

        System.out.println("Selected Auto: " + auto);
    }

}
