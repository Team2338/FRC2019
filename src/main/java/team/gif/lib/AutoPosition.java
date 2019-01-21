package team.gif.lib;

public enum AutoPosition {
    L1_LEFT(0),
    L1_CENTER(1),
    L1_RIGHT(2),
    L2_LEFT(3),
    L2_RIGHT(4);

    private int value;
    AutoPosition(int value) {
        this.value = value;
    }
}
