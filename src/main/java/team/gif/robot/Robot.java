package team.gif.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.util.WPILibVersion;
import team.gif.lib.AutoMode;
import team.gif.lib.AutoPosition;
import team.gif.lib.drivers.Limelight;
import team.gif.robot.commands.CommandGroupTemplate;
import team.gif.robot.commands.auto.Mobility;
import team.gif.robot.subsystems.Claw;
import team.gif.robot.subsystems.Climber;
import team.gif.robot.subsystems.Drivetrain;
import team.gif.robot.subsystems.Elevator;

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
//    private SerialPort port = new SerialPort(9600, SerialPort.Port.kUSB);

    private final ShuffleboardTab autoTab = Shuffleboard.getTab("Auto");
    private final ShuffleboardTab teleopTab = Shuffleboard.getTab("TeleOp");
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
        autoModeChooser.addOption("Cargo Ship: Front", AutoMode.CARGO_SHIP_FRONT);
        autoModeChooser.addOption("Cargo Ship: Near", AutoMode.CARGO_SHIP_NEAR);
        autoModeChooser.addOption("Cargo Ship: Mid", AutoMode.CARGO_SHIP_MID);
        autoModeChooser.addOption("Cargo Ship: Far", AutoMode.CARGO_SHIP_FAR);
        autoTab.add("Auto ClawMode", autoModeChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

        selectedAutoPosition = autoPositionChooser.getSelected();
        selectedAutoMode = autoModeChooser.getSelected();

//        drivetrain.beginOdometry();

        System.out.println("Robot Initialized. WPILib Version " + WPILibVersion.Version);

        System.out.println("Hatch Low Pos: " + Constants.Elevator.HATCH_LOW_POS);
        System.out.println("Hatch Mid Pos: " + Constants.Elevator.HATCH_MID_POS);
        System.out.println("Hatch High Pos: " + Constants.Elevator.HATCH_HIGH_POS);
        System.out.println("Cargo Low Pos: " + Constants.Elevator.CARGO_LOW_POS);
        System.out.println("Cargo Mid Pos: " + Constants.Elevator.CARGO_MID_POS);
        System.out.println("Cargo High Pos: " + Constants.Elevator.CARGO_HIGH_POS);

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
        System.out.println("Pos: " + elevator.getPosition() + " Rev: " + elevator.getRevLimit() + " Fwd: " + elevator.getFwdLimit());
//        System.out.println(Arrays.toString(limelight.getCamTran()));
//        System.out.println("Left" + claw.leftServoPos() + "Right" + claw.rightServoPos());
//        System.out.println("WinchPos: " + climber.getWinchPos());
//        System.out.println("LeftVel: " + drivetrain.getLeftVelTPS() + ", RightVel: " + drivetrain.getRightVelTPS() +
//                ", LeftOutput: " + drivetrain.leftMaster.getAppliedOutput() + ", RightOutput: " + drivetrain.rightMaster.getAppliedOutput());
//    System.out.println("Has Ball: " + claw.hasBall());
        limelight.setLEDMode(1);
        limelight.setCamMode(1);
//        port.writeString("<e,c,165>");
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
//        if (oi.dDPadUp.get()) {
//            elevator.setPercentOutput(0.2);
//        } else if (oi.dDPadDown.get()) {
//            elevator.setPercentOutput(-0.4);
//        } else {
//            elevator.setPercentOutput(-0.02);
//        }
//        elevator.setPercentOutput(-oi.aux.getY(GenericHID.Hand.kLeft) - 0.02);

//        if (oi.dY.get()) {
//            climber.setPistons(true, false);
//        } else if (oi.dA.get()) {
//            climber.setPistons(false, true);
//        } else {
//            climber.setPistons(false, false);
//        }
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
        System.out.println("Position: " + selectedAutoPosition + ", ClawMode: " + selectedAutoMode);

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
