package team.gif.lib;

public enum AutoPosition {
    L1_LEFT(1),
    L1_CENTER(1),
    L1_RIGHT(1),
    L2_LEFT(2),
    L2_RIGHT(2);

    public int level;
    AutoPosition(int level) {
        this.level = level;
    }
}
