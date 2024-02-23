/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.SimpleStringProperty
 */
package visual;

import javafx.beans.property.SimpleStringProperty;

public class ErrorPopOverEntry {
    private final SimpleStringProperty lineNumber = new SimpleStringProperty("");
    private final SimpleStringProperty errorMessage = new SimpleStringProperty("");

    ErrorPopOverEntry() {
        this(0, "");
    }

    ErrorPopOverEntry(int lineNumber, String errorMessage) {
        this.lineNumber.set("" + (lineNumber + 1));
        this.errorMessage.set(errorMessage);
    }

    public String getLineNumber() {
        return this.lineNumber.get();
    }

    public String getErrorMessage() {
        return this.errorMessage.get();
    }
}

