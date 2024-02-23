/*
 * Decompiled with CFR 0.152.
 */
package visual;

public class Symbol {
    private String name = "";
    private int value;
    private int lineNumber;

    public Symbol(String name, int value, int lineNumber) {
        this.name = name;
        this.value = value;
        this.lineNumber = lineNumber;
    }

    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }
}

