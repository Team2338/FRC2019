package team.gif.lib;

import edu.wpi.first.wpilibj.Notifier;
import team.gif.robot.subsystems.Drivetrain;

public class Odometry implements Runnable{

    private static Odometry instance;

    private Drivetrain drive;
    private Notifier notifier;

    private double currPos;
    private double lastPos;
    private double deltaPos;
    private double theta;
    private double x;
    private double y;

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
        currPos = (drive.getLeftPosition() + drive.getRightPosition()) / 2.0;
        deltaPos = currPos - lastPos;
        theta = Math.toRadians(drive.getHeading());
        x +=  Math.cos(theta) * deltaPos;
        y +=  Math.sin(theta) * deltaPos;
        lastPos = currPos;
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
        lastPos = 0.0;
    }
}
