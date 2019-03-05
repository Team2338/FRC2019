package team.gif.lib;

import jaci.pathfinder.Waypoint;

public enum TargetPosition {

    LEFT_ROCKET_NEAR(new Waypoint(214.491, 142.885, Math.toRadians(28.767))),
    LEFT_ROCKET_MID(new Waypoint(229.125, 133.558, Math.toRadians(90.0))),
    LEFT_ROCKET_FAR(new Waypoint(243.759, 142.885, Math.toRadians(151.233))),
    RIGHT_ROCKET_NEAR(new Waypoint(214.491, -142.885, Math.toRadians(-28.767))),
    RIGHT_ROCKET_MID(new Waypoint(229.125, -133.558, Math.toRadians(-90.0))),
    RIGHT_ROCKET_FAR(new Waypoint(243.759, -142.885, Math.toRadians(-151.233))),
    LEFT_SHIP_FRONT(new Waypoint(220.5, 10.875, Math.toRadians(0.0))),
    LEFT_SHIP_NEAR(new Waypoint(261.0, 55.750, Math.toRadians(-90.0))),
    LEFT_SHIP_MID(new Waypoint(282.75, 55.750, Math.toRadians(-90.0))),
    LEFT_SHIP_FAR(new Waypoint(304.5, 55.750, Math.toRadians(-90.0))),
    RIGHT_SHIP_FRONT(new Waypoint(220.5, -10.875, Math.toRadians(0.0))),
    RIGHT_SHIP_NEAR(new Waypoint(261.0, -55.750, Math.toRadians(90.0))),
    RIGHT_SHIP_MID(new Waypoint(282.75, -55.750, Math.toRadians(90.0))),
    RIGHT_SHIP_FAR(new Waypoint(304.5, -55.750, Math.toRadians(90.0))),
    LEFT_LOADING_STATION(new Waypoint(0.0, 135.285, Math.toRadians(180.0))),
    RIGHT_LOADING_STATION(new Waypoint(0.0, -135.285, Math.toRadians(180.0)));


    private Waypoint position;

    TargetPosition(Waypoint waypoint) {
        this.position = waypoint;
    }

    public Waypoint getWaypoint() {
        return position;
    }

    public Waypoint getRobotWaypoint(double depthOffset, double horizOffset, boolean invert) {
        return new Waypoint(position.x + depthOffset * Math.cos(position.angle) + horizOffset * Math.sin(position.angle),
                position.y + depthOffset * Math.sin(position.angle) - horizOffset * Math.cos(position.angle),
                invert ? Math.PI + position.angle : position.angle);
    }
}
