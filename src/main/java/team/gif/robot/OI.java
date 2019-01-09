package team.gif.robot;

import edu.wpi.first.wpilibj.XboxController;

public class OI {

    private static OI instance;

    private XboxController driver, aux;

    private OI() {
        driver = new XboxController(RobotMap.DRIVER_CONTROLLER_ID);
        aux = new XboxController(RobotMap.AUX_CONTROLLER_ID);

    }

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }
}
