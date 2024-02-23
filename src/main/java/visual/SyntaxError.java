/*
 * Decompiled with CFR 0.152.
 */
package visual;

public class SyntaxError {
    private int lineNumber = -1;
    private String message = "";
    private String details = "";

    public SyntaxError(int lineNumber, String message, String details) {
        this.details = details;
        this.message = message;
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDetails() {
        return this.details;
    }
}

