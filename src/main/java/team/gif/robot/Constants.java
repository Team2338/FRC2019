package team.gif.robot;

import static java.lang.Math.*;

public abstract class Constants {



    public static class Camera {
        public static final double CAMERA_POS_X = 6.0; // X lens displacement from wheelbase center (in)
        public static final double CAMERA_POS_Y = 6.0; // Y lens displacement from wheelbase center (in)
        public static final double CAMERA_POS_Z = 6.0; // Ground to lens height (in)
        public static final double CAMERA_ANGLE_OFFSET = toRadians(0.0); // Angle of camera to ground (radians)
        public static final double CAMERA_HORIZ_FOV = toRadians(54.0); // Horizontal FOV (radians)
        public static final double CAMERA_VERT_FOV = toRadians(41.0); // Vertical FOV (radians)
//        public static final double CAMERA_HORIZ_FOV = toRadians(59.6); // Horizontal FOV (radians)
//        public static final double CAMERA_VERT_FOV = toRadians(49.7); // Vertical FOV (radians)
        public static final int CAMERA_HORIZ_RES = 320; // Horizontal resolution (pixels)
        public static final int CAMERA_VERT_RES = 240; // Vertical resolution (pixels)
    }

    public static class Target {
        public static final double TARGET_TAPE_LENGTH = 5.5; // Length of one strip of vision tape (in)
        public static final double TARGET_TAPE_WIDTH = 2.0; // Width of one strip of vision tape (in)
        public static final double TARGET_TAPE_ANGLE = toRadians(14.5); // Angle of tape tilt (radians)
        public static final double LOW_TARGET_TOP_HEIGHT = 31.5; // Ground to top of low target height (in)
        public static final double HIGH_TARGET_TOP_HEIGHT = 39.125; // Ground to top of high target height (in)
        public static final double TARGET_INNER_WIDTH = 8.0; // Gap at closest points of target tape (in)

        public static final double TARGET_TAPE_ADJUSTED_WIDTH = TARGET_TAPE_WIDTH * cos(TARGET_TAPE_ANGLE)
                + TARGET_TAPE_LENGTH * sin(TARGET_TAPE_ANGLE);
        public static final double TARGET_TAPE_ADJUSTED_HEIGHT = TARGET_TAPE_WIDTH * sin(TARGET_TAPE_ANGLE)
                + TARGET_TAPE_LENGTH * cos(TARGET_TAPE_ANGLE);

        public static final double TARGET_CENTER_WIDTH = TARGET_INNER_WIDTH + TARGET_TAPE_ADJUSTED_WIDTH; // Center to center target width (in)
        public static final double TARGET_OUTER_WIDTH = TARGET_INNER_WIDTH + 2 * TARGET_TAPE_ADJUSTED_WIDTH;
        public static final double LOW_TARGET_CENTER_HEIGHT = LOW_TARGET_TOP_HEIGHT - (TARGET_TAPE_ADJUSTED_HEIGHT/2); // Ground to low target center (in)
        public static final double HIGH_TARGET_CENTER_HEIGHT = HIGH_TARGET_TOP_HEIGHT - (TARGET_TAPE_ADJUSTED_HEIGHT/2); // Ground to high target center (in)

    }

    public static class Drivetrain {
        // Physical Stuff
        public static final double WHEEL_DIAMETER = 5.0;
        public static final double TRACK_WIDTH = 25.0;
        public static final int TICKS_PER_REV = 4096;
        public static final double TICKS_TO_INCHES = (1.0/TICKS_PER_REV) * (WHEEL_DIAMETER*PI);
        public static final double TICKS_PER_100MS_TO_RADS_PER_S = (1.0/TICKS_PER_REV) * (2*PI) * (1000.0/100.0);

        public static final double INPUT_DEADBAND = 0.02;
        public static final double QUICK_STOP_THRESHOLD = 0.2;
        public static final double QUICK_STOP_ALPHA = 0.1;

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
