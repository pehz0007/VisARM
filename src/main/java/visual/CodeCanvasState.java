/*
 * Decompiled with CFR 0.152.
 */
package visual;

import visual.CanvasType;

public class CodeCanvasState {
    private int lineNumber;
    private boolean hide;
    private CanvasType type;
    private String colour;
    private int[] arrowLines = new int[2];

    public CodeCanvasState(int lineNumber, boolean hide, CanvasType type, String colour, int[] arrowLines) {
        this.lineNumber = lineNumber;
        this.hide = hide;
        this.type = type;
        this.colour = colour;
        this.arrowLines[0] = arrowLines[0];
        this.arrowLines[1] = arrowLines[1];
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public CanvasType getType() {
        return this.type;
    }

    public String getColour() {
        return this.colour;
    }

    public int[] getArrowLines() {
        return this.arrowLines;
    }

    public boolean isHide() {
        return this.hide;
    }
}

