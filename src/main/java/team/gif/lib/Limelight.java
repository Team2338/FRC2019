package team.gif.lib;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {

    Limelight instance;

    private NetworkTable table;

    private Limelight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public Limelight getInstance() {
        if (instance == null) {
            instance = new Limelight();
        }
        return instance;
    }

    public void setLEDMode(int mode) {
        if (0 <= mode && mode <= 3) {
            table.getEntry("ledMode").setNumber(mode);
        }
    }

    public void setCamMode(int mode) {
        if (0 <= mode && mode <= 1) {
            table.getEntry("camMode").setNumber(mode);
        }
    }

    public void setPipeline(int pipeline) {
        if (0 <= pipeline && pipeline <= 9) {
            table.getEntry("pipeline").setNumber(pipeline);
        }
    }

    public boolean hasTarget() {
        return table.getEntry("tv").getBoolean(false);
    }

    public double getXOffset() {
        return table.getEntry("tx").getDouble(0.0);
    }

    public double getYOffset() {
        return table.getEntry("ty").getDouble(0.0);
    }

    public double getArea() {
        return table.getEntry("ta").getDouble(0.0);
    }

    public double getSkew() {
        return table.getEntry("ts").getDouble(0.0);
    }

    public double getLatency() {
        return table.getEntry("tl").getDouble(0.0);
    }
}
