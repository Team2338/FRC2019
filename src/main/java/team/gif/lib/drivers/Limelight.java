package team.gif.lib.drivers;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {

    private static Limelight instance;

    private NetworkTable table;

    private Limelight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
    }

    /**
     * Construct and/or retrieve the singleton Limelight instance.
     *
     * @return limelight instance
     */
    public static Limelight getInstance() {
        if (instance == null) {
            instance = new Limelight();
        }
        return instance;
    }

    /**
     * Sets the mode of the limelight's LED array.
     * Mode 0 uses LED mode in current pipeline (see {@link this#setPipeline(int)}
     * Mode 1 is 'force off'
     * Mode 2 is 'force blink'
     * Mode 3 is 'force on'
     *
     * @param mode desired LED mode
     */
    public void setLEDMode(int mode) {
        if (0 <= mode && mode <= 3) {
            table.getEntry("ledMode").setNumber(mode);
        }
    }

    /**
     * Sets the limelight's mode of operation.
     * Mode 0 activates vision processing (decreased exposure)
     * Mode 1 activates driver vision (increased exposure, no processing)
     *
     * @param mode desired camera mode
     */
    public void setCamMode(int mode) {
        if (0 <= mode && mode <= 1) {
            table.getEntry("camMode").setNumber(mode);
        }
    }

    /**
     * Sets the limelight's active vision pipeline.
     * The limelight stores up to 10 pipelines indexed 0-9 (These can be configured through the Web UI).
     *
     * @param pipeline desired vision pipeline
     */
    public void setPipeline(int pipeline) {
        if (0 <= pipeline && pipeline <= 9) {
            table.getEntry("pipeline").setNumber(pipeline);
        }
    }

    /**
     * Sets the limelight's streaming mode.
     * Mode 0 is "Standard - Side-by-side streams if a webcam is attached to Limelight"
     * Mode 1 is "PiP Main - The secondary camera stream is placed in the lower-right corner of the primary camera stream"
     * Mode 2 is "PiP Secondary - The primary camera stream is placed in the lower-right corner of the secondary camera stream"
     *
     * @param mode desired streaming mode
     */
    public void setStreamMode(int mode) {
        if (0 <= mode && mode <= 2) {
            table.getEntry("stream").setNumber(mode);
        }
    }

    /**
     * Sets the limelight's snapshot mode. The limelight allows for taking pictures throughout a match.
     * Mode 0 stops taking snapshots
     * Mode 1 takes two snapshots per second.
     *
     * @param mode desired snapshot mode
     */
    public void setSnapshotMode(int mode) {
        if (0 <= mode && mode <= 1) {
            table.getEntry("snapshot").setNumber(mode);
        }
    }

    /**
     * Returns whether or not the limelight has any valid targets.
     *
     * @return true if has target, false if not
     */
    public boolean hasTarget() {
        return table.getEntry("tv").getNumber(0).equals(1);
    }

    /**
     * Returns the horizontal offset from crosshair to target.
     *
     * @return offset in degrees (-27 to +27)
     */
    public double getXOffset() {
        return table.getEntry("tx").getDouble(0.0);
    }

    /**
     * Returns the vertical offset from crosshair to target.
     *
     * @return offset in degrees (-20.5 to +20.5)
     */
    public double getYOffset() {
        return table.getEntry("ty").getDouble(0.0);
    }

    /**
     * Returns the percentage of screen area that the target fills.
     *
     * @return percentage of image (0-100)
     */
    public double getArea() {
        return table.getEntry("ta").getDouble(0.0);
    }

    /**
     * Returns the skew or rotation of the target.
     *
     * @return rotation in degrees (-90 to 0)
     */
    public double getSkew() {
        return table.getEntry("ts").getDouble(0.0);
    }

    /**
     * Returns the pipeline's latency contribution. Add at least 11 ms for image capture latency.
     *
     * @return latency in ms
     */
    public double getLatency() {
        return table.getEntry("tl").getDouble(0.0);
    }

    /**
     * Returns a data value from 1 of 3 raw contours given a valid key.
     *
     * "tx[num]" : x position in normalized screenspace (-1 to +1)
     * "ty[num]" : y position in normalized screenspace (-1 to +1)
     * "ta[num]" : area (0 to 100)
     * "ts[num]" : skew (-90 to 0 degrees)
     *
     * Can also be used to retrieve a data value from 1 of 2 crosshairs.
     * "cx[num]" : x position in normalized screenspace (-1 to +1)
     * "cy[num]" : y position in normalized screenspace (-1 to +1)
     *
     * @param key String key for desired data
     * @return raw data value (units vary)
     */
    public double getCustomValue(String key) {
        return table.getEntry(key).getDouble(0.0);
    }
}
