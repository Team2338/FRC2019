package team.gif.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.Command;
import team.gif.lib.drivers.Limelight;

import static team.gif.robot.Constants.*;

public class LimelightMethods extends Command {

    private Limelight limelight = Limelight.getInstance();

    public LimelightMethods() {

    }

    @Override
    protected void initialize() {


    }

    @Override
    protected void execute() {

    }

    protected double getDistance(){
        double yOffset = limelight.getYOffset();
        double distance = (LOW_TARGET_CENTER_HEIGHT - CAMERA_HEIGHT) / Math.tan(Math.toRadians(CAMERA_ANGLE + yOffset));
        return distance;
    }

    protected double pixelsToAngles(double pixelLength, int pixelAmount, double angleFOV){
        double toPixels = pixelLength * (1/(pixelAmount / 2));
        double vpw = 2.0 * Math.tan(Math.toRadians(angleFOV)/2);
        double x = (vpw/2) * toPixels;
        double finalAngle = Math.atan2(1, x);
        return finalAngle;

    }
    protected double getSkewHorz(){
        double dTheta = pixelsToAngles(limelight.getHorizLength(), CAMERA_RES_HORZ, CAMERA_HORZ_FOV)/2;
        double w =  (TARGET_TAPE_HORIZONTAL_CENTER*4 + TARGET_INNER_WIDTH)/2 ;
        double phi  = Math.asin((getDistance()*Math.sin(dTheta))/w);
        double ang1 = Math.toRadians(180) - dTheta - phi;
        double ang2 = phi-dTheta;
        return ang1;


    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {

    }
}
