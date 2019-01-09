package team.gif.lib;

public enum AutoPosition {
    LEFT(0),
    CENTER(1),
    RIGHT(2),
    HIGH_LEFT(3),
    HIGH_RIGHT(4);

    public int value;
    AutoPosition(int value) {
        this.value = value;
    }
}
