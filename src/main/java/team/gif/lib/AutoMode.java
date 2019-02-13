package team.gif.lib;

public enum AutoMode {
    MOBILITY(0),
    DOUBLE_ROCKET(1),
    CARGO_SHIP_FRONT(2),
    CARGO_SHIP_NEAR(3),
    CARGO_SHIP_MID(4),
    CARGO_SHIP_FAR(5);

    private int value;
    AutoMode(int value) {
        this.value = value;
    }
}
