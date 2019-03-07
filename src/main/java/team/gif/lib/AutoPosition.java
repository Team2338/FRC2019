package team.gif.lib;

import jaci.pathfinder.Waypoint;
import team.gif.robot.Constants;

public enum AutoPosition {
    L1_LEFT(1, new Waypoint(48 + Constants.Drivetrain.BUMPER_LENGTH / 2.0, 64 - Constants.Drivetrain.BUMPER_WIDTH / 2.0, 0.0)),
    L1_CENTER(1, new Waypoint(48 + Constants.Drivetrain.BUMPER_LENGTH / 2.0, 0.0, 0.0)),
    L1_RIGHT(1, new Waypoint(48 + Constants.Drivetrain.BUMPER_LENGTH / 2.0, Constants.Drivetrain.BUMPER_WIDTH / 2.0 - 64, 0.0)),
    L2_LEFT(2, new Waypoint(Constants.Drivetrain.BUMPER_LENGTH / 2.0, 64 - Constants.Drivetrain.BUMPER_WIDTH / 2.0, 0.0)),
    L2_RIGHT(2, new Waypoint(Constants.Drivetrain.BUMPER_LENGTH / 2.0, Constants.Drivetrain.BUMPER_WIDTH / 2.0 - 64, 0.0));

    private int level;
    private Waypoint position;

    AutoPosition(int level, Waypoint position) {
        this.level = level;
        this.position = position;
    }

    public int getLevel() {
        return level;
    }

    public Waypoint getWaypoint() {
        return position;
    }
}
