/*
 * Decompiled with CFR 0.152.
 */
package visual;

public class CycleCount {
    private String baseOpcode;
    private int cycleCount;

    public CycleCount(String baseOpcode, int cycleCount) {
        this.baseOpcode = baseOpcode;
        this.cycleCount = cycleCount;
    }

    public String getBaseOpcode() {
        return this.baseOpcode;
    }

    public int getCycleCount() {
        return this.cycleCount;
    }
}

