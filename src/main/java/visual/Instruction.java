/*
 * Decompiled with CFR 0.152.
 */
package visual;

import java.util.ArrayList;
import java.util.List;
import visual.Argument;

public class Instruction {
    private String opcode = "";
    private String opcode2 = "";
    private List<Argument> args = new ArrayList<Argument>();
    private boolean preIndex = false;
    private boolean postIndex = false;
    private final int lineNumber;
    private int branchToCount = 0;
    private int totalBranchCount = 0;
    private boolean ignoreThreshold = false;
    private boolean secondOperandCarry = false;
    private final int address;
    private Integer substituteCycleCount = null;

    public Instruction(int lineNumber, int address, String opcode, List<Argument> args) {
        this.lineNumber = lineNumber;
        this.opcode = opcode;
        this.args = args;
        this.address = address;
    }

    public Instruction(int lineNumber, int address, String opcode, String opcode2, List<Argument> args) {
        this.lineNumber = lineNumber;
        this.opcode = opcode;
        this.opcode2 = opcode2;
        this.args = args;
        this.address = address;
    }

    public String getOpcode() {
        return this.opcode;
    }

    public String getSecondOpcode() {
        return this.opcode2;
    }

    public List<Argument> getArgs() {
        return this.args;
    }

    public void setPreIndex(boolean b) {
        this.preIndex = b;
    }

    public boolean getPreIndex() {
        return this.preIndex;
    }

    public void setPostIndex(boolean b) {
        this.postIndex = b;
    }

    public boolean getPostIndex() {
        return this.postIndex;
    }

    public int getLineNumber() {
        return this.lineNumber;
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

    public void setSecondOperandCarry(boolean value) {
        this.secondOperandCarry = value;
    }

    public boolean getSecondOperandCarry() {
        return this.secondOperandCarry;
    }

    public void setSubstituteCycleCount(int value) {
        this.substituteCycleCount = value;
    }

    public Integer getSubstituteCycleCount() {
        return this.substituteCycleCount;
    }
}

