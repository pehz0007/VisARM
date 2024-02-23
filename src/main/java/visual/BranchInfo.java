/*
 * Decompiled with CFR 0.152.
 */
package visual;

import visual.VisualisationInfo;

public class BranchInfo
extends VisualisationInfo {
    private int lineNumber;
    private String branchCondition;
    private int sourceAddress;
    private int destAddress;
    private int destLineNumber;

    public BranchInfo(BranchInfo toCopy) {
        if (toCopy == null || !toCopy.isValid()) {
            this.valid = false;
            return;
        }
        this.valid = true;
        this.lineNumber = toCopy.getLineNumber();
        this.branchCondition = toCopy.getBranchCondition();
        this.sourceAddress = toCopy.getSourceAddress();
        this.destAddress = toCopy.getDestAddress();
        this.destLineNumber = toCopy.getDestLineNumber();
    }

    public BranchInfo(int lineNumber, String branchCondition, int sourceAddress, int destAddress, int destLineNumber) {
        this.lineNumber = lineNumber;
        this.branchCondition = branchCondition;
        this.sourceAddress = sourceAddress;
        this.destAddress = destAddress;
        this.destLineNumber = destLineNumber;
    }

    @Override
    public int getLineNumber() {
        return this.lineNumber;
    }

    public String getBranchCondition() {
        return this.branchCondition;
    }

    public int getSourceAddress() {
        return this.sourceAddress;
    }

    public int getDestAddress() {
        return this.destAddress;
    }

    public int getDestLineNumber() {
        return this.destLineNumber;
    }
}

