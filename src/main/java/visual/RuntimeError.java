/*
 * Decompiled with CFR 0.152.
 */
package visual;

public class RuntimeError
extends Exception {
    int lineNumber;
    String message;
    String details;

    public RuntimeError(int lineNumber, String message, String details) {
        this.lineNumber = lineNumber;
        this.message = message;
        this.details = details;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public String getDetails() {
        return this.details;
    }
}

