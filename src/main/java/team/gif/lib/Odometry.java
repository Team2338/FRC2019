package team.gif.lib;

import edu.wpi.first.wpilibj.Notifier;
import team.gif.robot.subsystems.Drivetrain;

public class Odometry implements Runnable {

    private static Odometry instance;

    private Drivetrain drive;
    private Notifier notifier;

    private double theta;
    private double x;
    private double y;
    private double lastPosition;

    private Odometry() {
        drive = Drivetrain.getInstance();
        notifier = new Notifier(this);
    }

    public static Odometry getInstance() {
        if (instance == null) {
            instance = new Odometry();
        }
        return instance;
    }

    public void start() {
        notifier.startPeriodic(0.005);
    }

    @Override
    public void run() {
        double position = (drive.getLeftPosInches() + drive.getRightPosInches()) / 2.0;
        double deltaPosition = position - lastPosition;
        theta = drive.getHeadingRadians();
        x +=  deltaPosition * Math.cos(theta);
        y +=  deltaPosition * Math.sin(theta);
        lastPosition = position;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTheta() {
        return theta;
    }

    public void reset() {
        x = 0.0;
        y = 0.0;
        lastPosition = 0.0;
    }

}
