package team.gif.robot;

public abstract class RobotMap {

    // OI (USB)
    public static final int DRIVER_CONTROLLER_ID = 0;
    public static final int AUX_CONTROLLER_ID = 1;

    // Spark MAXs (CAN)
    public static final int DRIVE_LEFT_MASTER_ID = 1;
    public static final int DRIVE_LEFT_SLAVE_ID = 2;
    public static final int DRIVE_RIGHT_MASTER_ID = 3;
    public static final int DRIVE_RIGHT_SLAVE_ID = 4;

    // Talon SRXs (CAN)
    public static final int CLAW_INTAKE_ID = 0;
    public static final int ELEVATOR_LIFT_ID = 1;
    public static final int CLIMB_DRIVE_ID = 2;
    public static final int CLIMB_WINCH_ID = 3;

    // Servos (PWM)
    public static final int CLAW_LEFT_SERVO_ID = 0;
    public static final int CLAW_RIGHT_SERVO_ID = 1;

    // Solenoids (PCM)
    public static final int CLAW_DEPLOY_ID = 1;
    public static final int CLAW_CLAMP_ID = 2;
    public static final int CLAW_HOOKS_FWD_ID = 0;
    public static final int CLAW_HOOKS_REV_ID = 3;

    public static final int CLIMB_FRONT_ID = 6;
    public static final int CLIMB_REAR_FWD_ID = 4;
    public static final int CLIMB_REAR_REV_ID = 5;

    public static final int BACK_HATCH_ID = 7;

    // Sensors
    public static final int PIGEON_ID = 0; // CAN
    public static final int CLAW_BALL_SENSOR_ID = 0; // Analog Input
}
