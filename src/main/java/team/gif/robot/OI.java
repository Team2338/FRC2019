package team.gif.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.POVButton;
import team.gif.lib.oi.AxisButton;
import team.gif.lib.oi.ComboButton;
import team.gif.lib.oi.NotButton;
import team.gif.robot.commands.claw.*;
import team.gif.robot.commands.climber.Climb;
import team.gif.robot.commands.climber.RaiseFront;
import team.gif.robot.commands.climber.RaiseRear;
import team.gif.robot.commands.drivetrain.DriveVoltageRampTest;
import team.gif.robot.commands.elevator.ElevatorVoltageRamper;
import team.gif.robot.commands.elevator.SmartElevatorPosition;
import team.gif.robot.commands.backhatch.BackEject;

public class OI {

    private static OI instance;

    public final XboxController driver = new XboxController(RobotMap.DRIVER_CONTROLLER_ID);
    public final XboxController aux = new XboxController(RobotMap.AUX_CONTROLLER_ID);

    public final JoystickButton dA = new JoystickButton(driver, 1);
    public final JoystickButton dB = new JoystickButton(driver, 2);
    public final JoystickButton dX = new JoystickButton(driver, 3);
    public final JoystickButton dY = new JoystickButton(driver, 4);
    public final JoystickButton dLB = new JoystickButton(driver, 5);
    public final JoystickButton dRB = new JoystickButton(driver, 6);
    public final JoystickButton dBack = new JoystickButton(driver, 7);
    public final JoystickButton dStart = new JoystickButton(driver, 8);
    public final JoystickButton dLS = new JoystickButton(driver, 9);
    public final JoystickButton dRS = new JoystickButton(driver, 10);

    public final JoystickButton aA = new JoystickButton(aux, 1);
    public final JoystickButton aB = new JoystickButton(aux, 2);
    public final JoystickButton aX = new JoystickButton(aux, 3);
    public final JoystickButton aY = new JoystickButton(aux, 4);
    public final JoystickButton aLB = new JoystickButton(aux, 5);
    public final JoystickButton aRB = new JoystickButton(aux, 6);
    public final JoystickButton aBack = new JoystickButton(aux, 7);
    public final JoystickButton aStart = new JoystickButton(aux, 8);
    public final JoystickButton aLS = new JoystickButton(aux, 9);
    public final JoystickButton aRS = new JoystickButton(aux, 10);

    public final AxisButton dLT = new AxisButton(driver, 2, 0.1);
    public final AxisButton dRT = new AxisButton(driver, 3, 0.1);
    public final AxisButton aLT = new AxisButton(aux, 2, 0.1);
    public final AxisButton aRT = new AxisButton(aux, 3, 0.1);

    public final POVButton dDPadUp = new POVButton(driver, 0);
    public final POVButton dDPadRight = new POVButton(driver, 90);
    public final POVButton dDPadDown = new POVButton(driver, 180);
    public final POVButton dDPadLeft = new POVButton(driver, 270);
    public final POVButton aDPadUp = new POVButton(aux, 0);
    public final POVButton aDPadRight = new POVButton(aux, 90);
    public final POVButton aDPadDown = new POVButton(aux, 180);
    public final POVButton aDPadLeft = new POVButton(aux, 270);

    // Special Buttons
    public final ComboButton dSpecial = new ComboButton(dBack, dStart);
    public final ComboButton aSpecial = new ComboButton(aBack, aStart);
    public final ComboButton aTriggers = new ComboButton(aLT, aRT);

    public final ComboButton elevHatchLow = new ComboButton(aDPadDown, new NotButton(aLB));
    public final ComboButton elevHatchMid = new ComboButton(aDPadRight, new NotButton(aLB));
    public final ComboButton elevHatchHigh = new ComboButton(aDPadUp, new NotButton(aLB));
    public final ComboButton elevCargoLow = new ComboButton(aDPadDown, aLB);
    public final ComboButton elevCargoMid = new ComboButton(aDPadRight, aLB);
    public final ComboButton elevCargoHigh = new ComboButton(aDPadUp, aLB);

    private OI() {
        dLB.whileHeld(new SmartEject());
        dRB.whileHeld(new SmartCollect());

        aB.whileHeld(new BackEject(10.0));
        aX.whenPressed(new ToggleDeploy());
        aY.whenPressed(new ToggleClawMode());

        aLB.whenPressed(new SmartElevatorPosition(SmartElevatorPosition.GenericPosition.COLLECT));
        aDPadLeft.whenPressed(new SmartElevatorPosition(SmartElevatorPosition.GenericPosition.LOAD));
        aDPadDown.whenPressed(new SmartElevatorPosition(SmartElevatorPosition.GenericPosition.LOW));
        aDPadRight.whenPressed(new SmartElevatorPosition(SmartElevatorPosition.GenericPosition.MID));
        aDPadUp.whenPressed(new SmartElevatorPosition(SmartElevatorPosition.GenericPosition.HIGH));

//        aRT.whileHeld(new VisionTurnTest());

        aTriggers.whenPressed(new Climb());
        aRB.whenPressed(new RaiseFront());
        aA.whileHeld(new RaiseRear());

        dA.whenPressed(new ElevatorVoltageRamper());
        dB.whenPressed(new DriveVoltageRampTest(11.0));

//        aBack.whenPressed(new Mobility(AutoPosition.L1_CENTER));
//        aDPadLeft.whenPressed(new SetElevatorPosition(Constants.Elevator.MIN_POS));
//        elevHatchLow.whenPressed(new SetElevatorPosition(Constants.Elevator.HATCH_LOW_POS));
//        elevHatchMid.whenPressed(new SetElevatorPosition(Constants.Elevator.HATCH_MID_POS));
//        elevHatchHigh.whenPressed(new SetElevatorPosition(Constants.Elevator.HATCH_HIGH_POS));
//        elevCargoLow.whenPressed(new SetElevatorPosition(Constants.Elevator.CARGO_LOW_POS));
//        elevCargoMid.whenPressed(new SetElevatorPosition(Constants.Elevator.CARGO_MID_POS));
//        elevCargoHigh.whenPressed(new SetElevatorPosition(Constants.Elevator.CARGO_HIGH_POS));
    }

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

}
