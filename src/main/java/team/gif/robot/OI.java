package team.gif.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import team.gif.lib.oi.AxisButton;
import team.gif.lib.oi.ComboButton;

public class OI {

    private static OI instance;

    public XboxController driver, aux;

    public JoystickButton start, back;
    public ComboButton autoCancel;
    public AxisButton dRT;

    private OI() {
        driver = new XboxController(RobotMap.DRIVER_CONTROLLER_ID);
        aux = new XboxController(RobotMap.AUX_CONTROLLER_ID);

        start = new JoystickButton(driver, 8);
        back = new JoystickButton(driver, 7);
        autoCancel = new ComboButton(start, back);
        dRT = new AxisButton(driver, 3, 0.1);
    }

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    public boolean getAutoCancel() {
        return autoCancel.get();
    }
}
