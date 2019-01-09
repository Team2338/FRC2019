package team.gif.robot;

public abstract class Constants {

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
