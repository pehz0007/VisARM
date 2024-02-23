/*
 * Decompiled with CFR 0.152.
 */
package visual;

public class LineLabel {
    private String id = "";
    private final int lineNo;
    private final int address;
    private int branchToCount = 0;
    private int totalBranchCount = 0;
    private boolean ignoreThreshold = false;

    public LineLabel(String id2, int lineNo, int address) {
        this.id = id2;
        this.lineNo = lineNo;
        this.address = address;
    }

    public String getId() {
        return this.id;
    }

    public int getLineNo() {
        return this.lineNo;
    }

    public int getAddress() {
        return this.address;
    }

    public int getBranchToCount(boolean includeAll) {
        return includeAll ? this.totalBranchCount : this.branchToCount;
    }

    public boolean isIgnoreThreshold() {
        return this.ignoreThreshold;
    }

    public void setIgnoreThreshold(boolean value) {
        this.ignoreThreshold = value;
    }

    public void incrementBranchToCount() {
        ++this.branchToCount;
        ++this.totalBranchCount;
    }

    public void resetBranchToCount() {
        this.branchToCount = 0;
    }

    public static enum LineLabelConflict {
        NONE,
        LABEL,
        CONSTANT,
        DIRECTIVE,
        SCANNED;

    }
}

