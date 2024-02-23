/*
 * Decompiled with CFR 0.152.
 */
package visual;

public enum Bases {
    DEC(10),
    HEX(16),
    BIN(2);

    private int value;

    private Bases(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}

