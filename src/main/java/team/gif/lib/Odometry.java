package team.gif.lib;

import edu.wpi.first.wpilibj.Notifier;
import team.gif.robot.subsystems.Drivetrain;

public class Odometry implements Runnable {

    Drivetrain drive = Drivetrain.getInstance();
    Notifier notifier = new Notifier(this);

    private double currPos;
    private double lastPos;
    private double deltaPos;
    private double theta;
    private double x;
    private double y;

    public Odometry() {
        notifier.startPeriodic(0.005);
    }

    @Override
    public void run() {
        currPos = (drive.getLeftPosition() + drive.getRightPosition())/2.0;
        deltaPos = currPos - lastPos;
        theta = drive.getHeading();
        x +=  Math.cos(Math.toRadians(theta)) * deltaPos;
        y +=  Math.sin(Math.toRadians(theta)) * deltaPos;
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
