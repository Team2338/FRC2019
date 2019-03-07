package team.gif.lib;

public enum AutoMode {
    MOBILITY(0),
    DOUBLE_ROCKET(1),
    CARGO_SHIP_FRONT_LEFT(2),
    CARGO_SHIP_FRONT_RIGHT(3),
    CARGO_SHIP_NEAR(4),
    CARGO_SHIP_MID(5),
    CARGO_SHIP_FAR(6),
    MANUAL(7);

    private int value;
    AutoMode(int value) {
        this.value = value;
    }
}
