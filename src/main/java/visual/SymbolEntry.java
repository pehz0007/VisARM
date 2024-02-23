/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.SimpleStringProperty
 */
package visual;

import javafx.beans.property.SimpleStringProperty;
import visual.SymbolType;

public class SymbolEntry {
    private final SimpleStringProperty address = new SimpleStringProperty("");
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty value = new SimpleStringProperty("");
    private final SimpleStringProperty name = new SimpleStringProperty("");

    SymbolEntry() {
        this(0, "", "", SymbolType.DATA);
    }

    SymbolEntry(int wordAddress, String name, String value, SymbolType type) {
        this.address.set("0x" + Integer.toHexString(wordAddress).toUpperCase());
        this.name.set(name);
        this.value.set(value);
        switch (type) {
            case DATA: {
                this.type.set("Data");
                break;
            }
            case CODE: {
                this.type.set("Code");
                break;
            }
            case CONST: {
                this.type.set("EQU");
                this.address.set(value);
                this.value.set("--");
            }
        }
    }

    public String getAddress() {
        return this.address.get();
    }

    public String getValue() {
        return this.value.get();
    }

    public String getType() {
        return this.type.get();
    }

    public String getName() {
        return this.name.get();
    }
}

