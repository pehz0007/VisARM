/*
 * Decompiled with CFR 0.152.
 */
package visual;

public abstract class VisualisationInfo {
    protected int lineNumber = -1;
    protected boolean valid = false;

    public int getLineNumber() {
        return this.lineNumber;
    }

    public boolean isValid() {
        return this.valid;
    }
}

