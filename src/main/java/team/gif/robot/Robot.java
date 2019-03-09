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
import team.gif.robot.commands.CommandTemplate;
import team.gif.robot.commands.auto.*;
import team.gif.robot.subsystems.Claw;
import team.gif.robot.subsystems.Climber;
import team.gif.robot.subsystems.Drivetrain;
import team.gif.robot.subsystems.Elevator;

import java.io.File;
import java.sql.Driver;

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
    private final Limelight limelight = Limelight.getInstance();
    private final Compressor compressor = new Compressor();
//    private final PowerDistributionPanel pdp = new PowerDistributionPanel();
//    private SerialPort port = new SerialPort(9600, SerialPort.Port.kUSB);

    private final ShuffleboardTab autoTab = Shuffleboard.getTab("Auto");
    private final ShuffleboardTab teleopTab = Shuffleboard.getTab("TeleOp");
    private final ShuffleboardTab debug = Shuffleboard.getTab("Debug");
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
        autoTab.add("Field Position", autoPositionChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

        autoModeChooser.setDefaultOption("Mobility", AutoMode.MOBILITY);
        autoModeChooser.addOption("Double Rocket", AutoMode.DOUBLE_ROCKET);
        autoModeChooser.addOption("Cargo Ship: Front Left", AutoMode.CARGO_SHIP_FRONT_LEFT);
        autoModeChooser.addOption("Cargo Ship: Front Right", AutoMode.CARGO_SHIP_FRONT_RIGHT);
        autoModeChooser.addOption("Cargo Ship: Near", AutoMode.CARGO_SHIP_NEAR);
        autoModeChooser.addOption("Cargo Ship: Mid", AutoMode.CARGO_SHIP_MID);
        autoModeChooser.addOption("Cargo Ship: Far", AutoMode.CARGO_SHIP_FAR);
        autoModeChooser.addOption("Manual Control", AutoMode.MANUAL);
        autoTab.add("Auto Mode", autoModeChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

        selectedAutoPosition = autoPositionChooser.getSelected();
        selectedAutoMode = autoModeChooser.getSelected();

//        compressor.clearAllPCMStickyFaults();
//        pdp.clearStickyFaults();

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
        limelight.setCamMode(1);
        limelight.setLEDMode(1);

//        System.out.println("Left Pos: " + drivetrain.getLeftPosTicks());
//        System.out.println("Right Pos: " + drivetrain.getRightPosTicks());
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() {
        if (selectedAutoMode == AutoMode.MANUAL) {
            compressor.start();
            drivetrain.setBrakeMode(true);
            drivetrain.setRampRate(0.15);
        } else {
            compressor.stop();
            drivetrain.setBrakeMode(false);
            drivetrain.setRampRate(0.0);
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

        if (oi.dSpecial.get() || oi.aSpecial.get()){
            Scheduler.getInstance().removeAll();
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
        drivetrain.setRampRate(0.15);
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
        oi.setRumble(matchTime > 44.5 && matchTime < 45.0);

//        System.out.println("Heading: " + climber.getWinchPos());
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
                auto = new LeftRocketDouble();
            } else if (selectedAutoPosition == AutoPosition.L1_RIGHT) {
                auto = new RightRocketDouble();
            } else {
                auto = new Mobility(selectedAutoPosition);
                DriverStation.reportWarning("This combination does not have an auto command.", false);
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_FRONT_LEFT) {
            if (selectedAutoPosition == AutoPosition.L1_LEFT) {
                auto = new LeftShipFront();
            } else if (selectedAutoPosition == AutoPosition.L1_CENTER) {
                auto = new CenterLeftShipFront();
            } else if (selectedAutoPosition == AutoPosition.L1_RIGHT) {
                auto = new RightShipFront();
            } else {
                auto = new Mobility(selectedAutoPosition);
                DriverStation.reportWarning("This combination does not have an auto command.", false);
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_FRONT_RIGHT) {
            if (selectedAutoPosition == AutoPosition.L1_LEFT) {
                auto = new LeftShipFront();
            } else if (selectedAutoPosition == AutoPosition.L1_CENTER) {
                auto = new CenterRightShipFront();
            } else if (selectedAutoPosition == AutoPosition.L1_RIGHT) {
                auto = new RightShipFront();
            } else {
                auto = new Mobility(selectedAutoPosition);
                DriverStation.reportWarning("This combination does not have an auto command.", false);
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_NEAR) {
            if (selectedAutoPosition == AutoPosition.L1_LEFT) {
                auto = new LeftShipNearHatch();
            } else if (selectedAutoPosition == AutoPosition.L1_RIGHT) {
                auto = new RightShipNearHatch();
            } else {
                auto = new Mobility(selectedAutoPosition);
                DriverStation.reportWarning("This combination does not have an auto command.", false);
            }
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_MID) {
            auto = new Mobility(selectedAutoPosition);
            DriverStation.reportWarning("This combination does not have an auto command.", false);
        } else if (selectedAutoMode == AutoMode.CARGO_SHIP_FAR) {
            auto = new Mobility(selectedAutoPosition);
            DriverStation.reportWarning("This combination does not have an auto command.", false);
        } else if (selectedAutoMode == AutoMode.MANUAL) {
            auto = new CommandTemplate();
            DriverStation.reportWarning("No auto command will run during sandstorm.", false);
        } else {
            auto = new Mobility(selectedAutoPosition);
            DriverStation.reportWarning("This combination does not have an auto command.", false);
        }

        System.out.println("Selected Auto: " + auto);
    }

}
