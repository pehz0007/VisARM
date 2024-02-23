/*
 * Decompiled with CFR 0.152.
 */
package visual;

import visual.ArgType;
import visual.SyntaxScanner;

public class Argument {
    private ArgType type;
    private int value;
    private String strValue;
    private SyntaxScanner.SubstitutionType subType;
    private int maxValue = -1;

    public Argument(ArgType type, int value) {
        this.type = type;
        this.value = value;
    }

    public Argument(ArgType type, String strValue) {
        this.type = type;
        this.strValue = strValue;
    }

    public Argument(ArgType type, String strValue, SyntaxScanner.SubstitutionType subType) {
        this.type = type;
        this.strValue = strValue;
        this.subType = subType;
    }

    public Argument(ArgType type, String strValue, SyntaxScanner.SubstitutionType subType, int maxValue) {
        this.type = type;
        this.strValue = strValue;
        this.subType = subType;
        this.maxValue = maxValue;
    }

    public ArgType getType() {
        return this.type;
    }

    public int getValue() {
        return this.value;
    }

    public String getStrValue() {
        return this.strValue;
    }

    public SyntaxScanner.SubstitutionType getSubType() {
        return this.subType;
    }

    public int getMaxValue() {
        return this.maxValue;
    }
}

