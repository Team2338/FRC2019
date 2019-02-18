package team.gif.robot;

public abstract class RobotMap {

    // OI
    public static final int DRIVER_CONTROLLER_ID = 0;
    public static final int AUX_CONTROLLER_ID = 1;

    // Spark MAXs
    public static final int LEFT_MASTER_ID = 1;
    public static final int LEFT_SLAVE_ID = 2;
    public static final int RIGHT_MASTER_ID = 3;
    public static final int RIGHT_SLAVE_ID = 4;

    // Talon SRXs
    public static final int CLAW_INTAKE_ID = 0;
    public static final int ELEVATOR_LIFT_ID = 1;
    public static final int CLIMB_DRIVE_ID = 2;
    public static final int CLIMB_WINCH = 3;

    // Servos
    public static final int LEFT_SERVO_ID = 1;
    public static final int RIGHT_SERVO_ID = 0;

    // Solenoids
    public static final int CLAW_DEPLOY_ID = 0;
    public static final int CLAW_CLAMP_ID = 1;
    public static final int CLAW_HOOKS_ID = 2;

    public static final int FRONT_CLIMB_ID = 3;
    public static final int REAR_CLIMB_ID = 4;

    // Sensors
    public static final int PIGEON_ID = 0;
    public static final int BALL_SENSOR_ID = 0;
}
