/*
 * Decompiled with CFR 0.152.
 */
package visual;

public class CodeArrow {
    private int[] lines = new int[2];
    private String colour = "";

    public CodeArrow(int[] lines, String colour) {
        this.lines = lines;
        this.colour = colour;
    }

    public int[] getLines() {
        return this.lines;
    }

    public void setLines(int[] lines) {
        this.lines = lines;
    }

    public String getColour() {
        return this.colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}

