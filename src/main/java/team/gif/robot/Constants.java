package team.gif.robot;

public abstract class Constants {

    public static final double CAMERA_HEIGHT = 6.0; // Ground to lens height (in)
    public static final double CAMERA_ANGLE = 0; // Angle of the camera to the ground
    public static final double CAMERA_HORZ_FOV = 54; //Camera horizontal FOV in radians
    public static final double CAMERA_VERT_FOV = 41; // Camera vertical FOV in radians
    public static final int CAMERA_RES_HORZ = 320;
    public static final int CAMERA_RES_VERT=240;
    public static final double TARGET_TAPE_LENGTH = 5.5; // Length of one strip of vision tape (in)
    public static final double TARGET_TAPE_WIDTH = 2.0; // Width of one strip of vision tape (in)
    public static final double TARGET_TAPE_ANGLE = 14.5; // Angle of tape tilt (degrees)
    public static final double LOW_TARGET_TOP_HEIGHT = 31.5; // Ground to top of low target height (in)
    public static final double HIGH_TARGET_TOP_HEIGHT = 39.125; // Ground to top of high target height (in)
    public static final double TARGET_INNER_WIDTH = 8.0; // Gap at closest points of target tape (in)
    public static final double TARGET_TAPE_VERTICAL_CENTER = (TARGET_TAPE_LENGTH * Math.cos(Math.toRadians(TARGET_TAPE_ANGLE))
            + TARGET_TAPE_WIDTH * Math.sin(Math.toRadians(14.5))) / 2; // Half of individual tape height after tilt (in)
    public static final double TARGET_TAPE_HORIZONTAL_CENTER = (TARGET_TAPE_WIDTH * Math.cos(Math.toRadians(TARGET_TAPE_ANGLE))
            + TARGET_TAPE_LENGTH * Math.sin(Math.toRadians(14.5))) / 2; // Half of individual tape width after tilt (in)
    public static final double LOW_TARGET_CENTER_HEIGHT = LOW_TARGET_TOP_HEIGHT - TARGET_TAPE_VERTICAL_CENTER; // Ground to low target center (in)
    public static final double HIGH_TARGET_CENTER_HEIGHT = HIGH_TARGET_TOP_HEIGHT - TARGET_TAPE_VERTICAL_CENTER; // Ground to high target center (in)
    public static final double TARGET_CENTER_WIDTH = 2 * TARGET_TAPE_HORIZONTAL_CENTER + TARGET_INNER_WIDTH; // Center to center target width (in)

    public class Drivetrain {
        // Physical Stuff
        public static final double WHEEL_DIAMETER = 6.0;
        public static final double TRACK_WIDTH = 25.0;
        public static final int TICKS_PER_REV = 4096;

        // Tuned Stuff
        public static final double DRIVE_P = 0.0;
        public static final double DRIVE_I = 0.0;
        public static final double DRIVE_D = 0.0;
        public static final double ROTATE_P = 0.0;
        public static final double ROTATE_I = 0.0;
        public static final double ROTATE_D = 0.0;
        public static final double V_LEFT = 0.0;
        public static final double V_RIGHT = 0.0;
        public static final double V_INTERCEPT_LEFT = 0.0;
        public static final double V_INTERCEPT_RIGHT = 0.0;
        public static final double A_LEFT = 0.0;
        public static final double A_RIGHT = 0.0;
    }

    /*
    Steps to measure effective track width:
    1. Determine your effective wheel diameter
    2. Spin your bot around exactly 10 times, this can be monitored via gyro
    3. Take the "distance traveled" by each of your wheels and average them
    4. Divide this average by 10 for the circumference of a single rotation
    5. Circumference divided by pi is the diameter of a rotation, or effective track width
    ALTERNATIVELY
    Instead of spinning around exactly ten times,
    just note exactly how far you span in degrees and use this in the calculation
    */
}
