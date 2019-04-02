package team.gif.lib;

public enum AutoMode {
    MOBILITY(0),
    DOUBLE_ROCKET(1),
    CARGO_SHIP_FRONT_LEFT(2),
    CARGO_SHIP_FRONT_RIGHT(3),
    CARGO_SHIP_FRONT_NEAR_LEFT(4),
    CARGO_SHIP_FRONT_NEAR_RIGHT(5),
    CARGO_SHIP_NEAR(5),
    CARGO_SHIP_MID(6),
    CARGO_SHIP_FAR(7),
    MANUAL(8);

    private int value;
    AutoMode(int value) {
        this.value = value;
    }
}
