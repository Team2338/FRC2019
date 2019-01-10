package team.gif.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import team.gif.lib.oi.ComboButton;

public class OI {

    private static OI instance;

    private XboxController driver, aux;

    private JoystickButton start;
    private JoystickButton back;
    private ComboButton autoCancel;

    private OI() {
        driver = new XboxController(RobotMap.DRIVER_CONTROLLER_ID);
        aux = new XboxController(RobotMap.AUX_CONTROLLER_ID);

        start = new JoystickButton(driver, 8);
        back = new JoystickButton(driver, 7);
        autoCancel = new ComboButton(start, back);
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
