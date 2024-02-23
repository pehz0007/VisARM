/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.SimpleStringProperty
 */
package visual;

import javafx.beans.property.SimpleStringProperty;
import visual.Bases;
import visual.VisUAL;

public class RegHistEntry {
    private final SimpleStringProperty lineNumber = new SimpleStringProperty("");
    private final SimpleStringProperty dispValue = new SimpleStringProperty("");
    private Bases dispBase = VisUAL.getSettingsManager().getRegisterFormat();
    private int value;
    private int stateNo;

    RegHistEntry() {
        this(0, 0, 0);
    }

    RegHistEntry(int lineNumber, int value, int stateNo) {
        this.lineNumber.set("" + (lineNumber + 1));
        this.stateNo = stateNo;
        this.value = value;
        switch (this.dispBase) {
            case DEC: {
                this.dispValue.set("" + value);
                break;
            }
            case BIN: {
                this.dispValue.set("0b" + Integer.toBinaryString(value));
                break;
            }
            case HEX: {
                this.dispValue.set("0x" + Integer.toHexString(value).toUpperCase());
                break;
            }
        }
    }

    public String getLineNumber() {
        return this.lineNumber.get();
    }

    public String getDispValue() {
        return this.dispValue.get();
    }

    public Bases getDispBase() {
        return this.dispBase;
    }

    public int getStateNo() {
        return this.stateNo;
    }

    public void setDispBase(Bases base) {
        this.dispBase = base;
        switch (this.dispBase) {
            case DEC: {
                this.dispValue.set("" + this.value);
                break;
            }
            case BIN: {
                this.dispValue.set("0b" + Integer.toBinaryString(this.value));
                break;
            }
            case HEX: {
                this.dispValue.set("0x" + Integer.toHexString(this.value).toUpperCase());
                break;
            }
        }
    }
}

