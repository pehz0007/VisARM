/*
 * Decompiled with CFR 0.152.
 */
package visual;

import visual.StatusBit;
import visual.StatusBitStyle;

public class StatusBitState {
    private StatusBitStyle style;
    private StatusBit bit;
    private boolean value;

    public StatusBitState(StatusBit bit, StatusBitStyle style, boolean value) {
        this.bit = bit;
        this.style = style;
        this.value = value;
    }

    public StatusBitStyle getStyle() {
        return this.style;
    }

    public boolean getValue() {
        return this.value;
    }

    public StatusBit getBit() {
        return this.bit;
    }
}

