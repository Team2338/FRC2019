package team.gif.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.util.WPILibVersion;
import team.gif.lib.AutoMode;
import team.gif.lib.AutoPosition;
import team.gif.lib.drivers.Limelight;
import team.gif.robot.commands.auto.*;
import team.gif.robot.subsystems.Claw;
import team.gif.robot.subsystems.Climber;
import team.gif.robot.subsystems.Drivetrain;
import team.gif.robot.subsystems.Elevator;

import java.util.Map;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private final Claw claw = Claw.getInstance();
    private final Climber climber = Climber.getInstance();
    private final Drivetrain drivetrain = Drivetrain.getInstance();
    private final Elevator elevator = Elevator.getInstance();
    private final OI oi = OI.getInstance();

    public static final Limelight limelight = new Limelight();
    private final Compressor compressor = new Compressor();
    private final AnalogInput workingPressureSensor = new AnalogInput(RobotMap.WORKING_PRESSURE_SENSOR_ID);
    private final AnalogInput storedPressureSensor = new AnalogInput(RobotMap.STORED_PRESSURE_SENSOR_ID);

    private ShuffleboardTab autoTab, teleopTab, debugTab;
    private SendableChooser<AutoPosition> autoPositionChooser = new SendableChooser<>();
    private SendableChooser<AutoMode> autoModeChooser = new SendableChooser<>();
    private NetworkTableEntry workingPressureEntry, storedPressureEntry, batteryVoltageEntry, matchTimeEntry, headingEntry,
            elevPosEntry, leftPosEntry, rightPosEntry, winchPosEntry;

    private AutoPosition selectedAutoPosition;
    private AutoMode selectedAutoMode;
    private Command auto;
    private boolean autoComplete;

    public static final boolean isCompBot = true;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        setupShuffleboard();
        selectedAutoPosition = autoPositionChooser.getSelected();
        selectedAutoMode = autoModeChooser.getSelected();
        updateSelectedAuto();

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
        if (oi.dMenuButtons.get() || oi.aMenuButtons.get()) Scheduler.getInstance().removeAll();
//        updateShuffleboard();
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
        drivetrain.setBrakeMode(true);
        if (auto != null) {
            auto.start();
            compressor.stop();
            limelight.setCamMode(0);
            limelight.setLEDMode(3);
            autoComplete = false;
        } else {
            teleopInit();
            autoComplete = true;
        }
    }

    /**
     * This function is called periodically during autonomous mode.
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        if (!autoComplete && auto.isCompleted()) {
            teleopInit();
            autoComplete = true;
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

        double matchTime = DriverStation.getInstance().getMatchTime();
        oi.setRumble(matchTime > 44.0 && matchTime < 46.0);
    }

    private void setupShuffleboard() {
        autoTab = Shuffleboard.getTab("Auto");
        teleopTab = Shuffleboard.getTab("TeleOp");
        debugTab = Shuffleboard.getTab("Debug");

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
        autoModeChooser.addOption("Cargo Ship: Front & Near Left", AutoMode.CARGO_SHIP_FRONT_NEAR_LEFT);
        autoModeChooser.addOption("Cargo Ship: Front & Near Right", AutoMode.CARGO_SHIP_FRONT_NEAR_RIGHT);
        autoModeChooser.addOption("Cargo Ship: Near", AutoMode.CARGO_SHIP_NEAR);
        autoModeChooser.addOption("Manual Control", AutoMode.MANUAL);
        autoTab.add("Auto Mode", autoModeChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

//        workingPressureEntry = teleopTab.add("Working Pressure", 0.0)
//                .withWidget(BuiltInWidgets.kDial)
//                .withProperties(Map.of("min", 45, "max", 75))
//                .withPosition(0, 0)
//                .getEntry();
//        storedPressureEntry = teleopTab.add("Stored Pressure", 0.0)
//                .withWidget(BuiltInWidgets.kDial)
//                .withProperties(Map.of("min", 60, "max", 120))
//                .withPosition(0, 1)
//                .getEntry();
//        batteryVoltageEntry = teleopTab.add("Battery Voltage", 0.0)
//                .withWidget(BuiltInWidgets.kNumberBar)
//                .withPosition(9, 0)
//                .withProperties(Map.of("min", 6, "max", 13))
//                .getEntry();
//        matchTimeEntry = teleopTab.add("Match Time", 0).getEntry();
//
//
//        headingEntry = debugTab.add("Heading", 0).withWidget(BuiltInWidgets.kGyro).getEntry();
//        elevPosEntry = debugTab.add("Elev Pos", 0).getEntry();
//        leftPosEntry = debugTab.add("Left Pos", 0).getEntry();
//        rightPosEntry = debugTab.add("Right Pos", 0).getEntry();
//        winchPosEntry = debugTab.add("Winch Pos", 0).getEntry();
    }

    private void updateShuffleboard() {
//        workingPressureEntry.setNumber(250 * (workingPressureSensor.getAverageVoltage() / RobotController.getVoltage5V()) - 25);
//        storedPressureEntry.setNumber(250 * (storedPressureSensor.getAverageVoltage() / RobotController.getVoltage5V()) - 25);
//        batteryVoltageEntry.setNumber(RobotController.getBatteryVoltage());
//        matchTimeEntry.setNumber(DriverStation.getInstance().getMatchTime());
//        headingEntry.setNumber(drivetrain.getHeadingDegrees());
//        elevPosEntry.setNumber(elevator.getPosition());
//        leftPosEntry.setNumber(drivetrain.getLeftPosTicks());
//        rightPosEntry.setNumber(drivetrain.getRightPosTicks());
//        winchPosEntry.setNumber(climber.getWinchPos());
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
                DriverStation.reportWarning("This combination does not have an auto command.", false);
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_FRONT_LEFT) {
            if (selectedAutoPosition == AutoPosition.L1_LEFT) {
                auto = new LeftShipFrontSideHatch();
            } else if (selectedAutoPosition == AutoPosition.L1_CENTER) {
                auto = new CenterLeftShipFront();
            } else {
                auto = new Mobility(selectedAutoPosition);
                DriverStation.reportWarning("This combination does not have an auto command.", false);
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_FRONT_RIGHT) {
            if (selectedAutoPosition == AutoPosition.L1_CENTER) {
                auto = new CenterRightShipFront();
            } else if (selectedAutoPosition == AutoPosition.L1_RIGHT) {
                auto = new RightShipFrontSideHatch();
            } else {
                auto = new Mobility(selectedAutoPosition);
                DriverStation.reportWarning("This combination does not have an auto command.", false);
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_FRONT_NEAR_LEFT) {
            if (selectedAutoPosition == AutoPosition.L1_LEFT) {
                auto = new LeftShipFrontSideHatch();
            } else if (selectedAutoPosition == AutoPosition.L1_CENTER) {
                auto = new CenterToLeftShipFrontSideHatch();
            } else {
                auto = new Mobility(selectedAutoPosition);
                DriverStation.reportWarning("This combination does not have an auto command.", false);
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_FRONT_NEAR_RIGHT) {
            if (selectedAutoPosition == AutoPosition.L1_CENTER) {
                auto = new CenterToRightShipFrontSideHatch();
            } else if (selectedAutoPosition == AutoPosition.L1_RIGHT) {
                auto = new RightShipFrontSideHatch();
            } else {
                auto = new Mobility(selectedAutoPosition);
                DriverStation.reportWarning("This combination does not have an auto command.", false);
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_NEAR) {
            if (selectedAutoPosition == AutoPosition.L1_LEFT) {
                auto = new LeftShipSideHatch();
            } else if (selectedAutoPosition == AutoPosition.L1_RIGHT) {
                auto = new RightShipSideHatch();
            } else {
                auto = new Mobility(selectedAutoPosition);
                DriverStation.reportWarning("This combination does not have an auto command.", false);
            }
        } else if (selectedAutoMode == AutoMode.MANUAL) {
            auto = null;
            DriverStation.reportWarning("No auto command will run during sandstorm.", false);
        } else {
            auto = new Mobility(selectedAutoPosition);
            DriverStation.reportWarning("This combination does not have an auto command.", false);
        }

        System.out.println("Selected Auto: " + auto);
    }

}
