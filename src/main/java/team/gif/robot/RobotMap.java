package team.gif.robot;

public abstract class RobotMap {

    // OI
    public static final int DRIVER_CONTROLLER_ID = 0;
    public static final int AUX_CONTROLLER_ID = 1;

    // Spark MAXs
    public static final int LEFT_MASTER_ID = 0;
    public static final int LEFT_SLAVE_ID = 1;
    public static final int RIGHT_MASTER_ID = 2;
    public static final int RIGHT_SLAVE_ID = 3;

    // Talon SRXs
    public static final int ELEVATOR_LIFT_ID = 0;
    public static final int CLAW_INTAKE_ID = 1;
    public static final int CLIMB_DRIVE_ID = 2;

    // Solenoids
    public static final int FRONT_LEFT_FORWARD_ID = 4;
    public static final int FRONT_LEFT_REVERSE_ID = 5;
    public static final int REAR_LEFT_FORWARD_ID = 6;
    public static final int REAR_LEFT_REVERSE_ID = 7;
    public static final int FRONT_RIGHT_FORWARD_ID = 1;
    public static final int FRONT_RIGHT_REVERSE_ID = 0;
    public static final int REAR_RIGHT_FORWARD_ID = 3;
    public static final int REAR_RIGHT_REVERSE_ID = 2;

    public static final int CLAW_DEPLOY_ID = 0;
    public static final int CLAW_CLAMP_ID = 1;
    public static final int CLAW_HOOKS_ID = 2;

    // Sensors
    public static final int PIGEON_ID = 0;
}
