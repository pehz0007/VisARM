/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.SimpleStringProperty
 */
package visual;

import javafx.beans.property.SimpleStringProperty;
import visual.Bases;

public class MemWordEntry {
    private final SimpleStringProperty wordAddress = new SimpleStringProperty("");
    private final SimpleStringProperty byte3 = new SimpleStringProperty("");
    private final SimpleStringProperty byte2 = new SimpleStringProperty("");
    private final SimpleStringProperty byte1 = new SimpleStringProperty("");
    private final SimpleStringProperty byte0 = new SimpleStringProperty("");
    private final SimpleStringProperty wordValue = new SimpleStringProperty("");

    MemWordEntry() {
        this(0, 0, Bases.HEX);
    }

    MemWordEntry(int wordAddress, int value, Bases dispFormat) {
        this.wordAddress.set("0x" + Integer.toHexString(wordAddress).toUpperCase());
        this.byte0.set("0x" + Integer.toHexString((byte)value & 0xFF).toUpperCase());
        this.byte1.set("0x" + Integer.toHexString((byte)(value >>> 8) & 0xFF).toUpperCase());
        this.byte2.set("0x" + Integer.toHexString((byte)(value >>> 16) & 0xFF).toUpperCase());
        this.byte3.set("0x" + Integer.toHexString((byte)(value >>> 24) & 0xFF).toUpperCase());
        switch (dispFormat) {
            case HEX: {
                this.wordValue.set("0x" + Integer.toHexString(value).toUpperCase());
                break;
            }
            default: {
                this.wordValue.set("" + value);
            }
        }
    }

    public String getWordAddress() {
        return this.wordAddress.get();
    }

    public String getByte3() {
        return this.byte3.get();
    }

    public String getByte2() {
        return this.byte2.get();
    }

    public String getByte1() {
        return this.byte1.get();
    }

    public String getByte0() {
        return this.byte0.get();
    }

    public String getWordValue() {
        return this.wordValue.get();
    }
}

