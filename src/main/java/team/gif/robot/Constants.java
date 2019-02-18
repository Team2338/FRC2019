package team.gif.robot;

import static java.lang.Math.*;

public abstract class Constants {

    public static class Camera {
        public static final double POS_X = 6.0; // X lens displacement from wheelbase center (in)
        public static final double POS_Y = 6.0; // Y lens displacement from wheelbase center (in)
        public static final double POS_Z = 8.75; // Ground to lens height (in)
        public static final double ANGLE_OFFSET = toRadians(-10.0); // Angle of camera to ground (radians)
        public static final double HORIZ_FOV = toRadians(59.6); // Horizontal FOV (radians)
        public static final double VERT_FOV = toRadians(49.7); // Vertical FOV (radians)
        public static final int HORIZ_RES = 320; // Horizontal resolution (pixels)
        public static final int VERT_RES = 240; // Vertical resolution (pixels)
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
        public static final double LOW_TARGET_CENTER_HEIGHT = LOW_TARGET_TOP_HEIGHT - (TARGET_TAPE_ADJUSTED_HEIGHT/2.0); // Ground to low target center (in)
        public static final double HIGH_TARGET_CENTER_HEIGHT = HIGH_TARGET_TOP_HEIGHT - (TARGET_TAPE_ADJUSTED_HEIGHT/2.0); // Ground to high target center (in)

    }

    public static class Claw {
        public static final double LEFT_BRAKE_POS = 0.2;
        public static final double LEFT_NEUTRAL_POS = 1.0;
        public static final double RIGHT_BRAKE_POS = 0.65;
        public static final double RIGHT_NEUTRAL_POS = 0.0;
    }

    public static class Climber {
        public static final double VARIABLE_RATE_PISTON_PERIOD = 0.2; // Resolution of the solenoid PWM in seconds
        public static final double GYRO_SENSITIVITY = 0.1;
        public static final double P = 0.0;
        public static final double I = 0.0;
        public static final double D = 0.0;
        public static final double F = 0.0;
        public static final double PISTON_FEED_FORWARD = 0.25;
    }

    public static class Drivetrain {
        // Physical Stuff
        public static final double WHEEL_DIAMETER = 5.0;
        public static final double TRACK_WIDTH = 25.0;
        public static final int TICKS_PER_REV = 4096;
        public static final double TICKS_TO_INCHES = (1.0 / TICKS_PER_REV) * (WHEEL_DIAMETER*PI);
        public static final double TPS_TO_RPS = (1.0 / TICKS_PER_REV) * (2 * PI);

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
        public static final double V_LEFT = 1.0 / (9.75 * WHEEL_DIAMETER / 2.0);
        public static final double V_RIGHT = 1.0 / (9.75 * WHEEL_DIAMETER / 2.0);
        public static final double V_INTERCEPT_LEFT = 0.0;
        public static final double V_INTERCEPT_RIGHT = 0.0;
        public static final double A_LEFT = 1.0 / (50.0 * WHEEL_DIAMETER / 2.0);
        public static final double A_RIGHT = 1.0 / (50.0 * WHEEL_DIAMETER / 2.0);
    }

    public static class Elevator {
        public static final double P = 0.1;
        public static final double I = 0.0;
        public static final double D = 0.0;
//        public static final double F = 0.615;
        public static final double GRAV_FEED_FORWARD = 3.58 / 12.0; // Percent constant to counteract gravity
        private static final double VOLTS_PER_RAD_PER_SEC = .33;
        public static final double F = (VOLTS_PER_RAD_PER_SEC * (1023.0 / (12.0 - 12.0*GRAV_FEED_FORWARD)) * (2*Math.PI/4096) * (10));

        public static final int ALLOWABLE_ERROR = 10; // Error to allow move command to end
        public static final int MAX_VELOCITY = 1500; // Elevator velocity (ticks/100ms)
        public static final int MAX_ACCELERATION = 1500; // Elevator acceleration (ticks/100ms/s)

        public static final int MIN_POS = 0; // Minimum soft limit
        public static final int MAX_POS = 100; // Maximum soft limit
        public static final int HATCH_LOW_POS = 0;
        public static final int HATCH_MID_POS = 50;
        public static final int HATCH_HIGH_POS = 100;
        public static final int CARGO_LOW_POS = 0;
        public static final int CARGO_MID_POS = 50;
        public static final int CARGO_HIGH_POS = 100;
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
