/*
 * Decompiled with CFR 0.152.
 */
package visual;

import java.util.ArrayList;
import java.util.List;
import visual.StackEntry;
import visual.StackType;
import visual.VisualisationInfo;

public class StackInfo
extends VisualisationInfo {
    private int lineNumber;
    private int regPointer;
    private int address;
    private StackType stackType;
    private boolean store;
    private boolean writeback;
    private List<StackEntry> entries = new ArrayList<StackEntry>();
    private boolean valid = false;

    public StackInfo() {
    }

    public StackInfo(int lineNumber, int regPointer, int address, StackType stackType, boolean store, boolean writeback, List<StackEntry> entries) {
        this.lineNumber = lineNumber;
        this.regPointer = regPointer;
        this.address = address;
        this.stackType = stackType;
        this.store = store;
        this.writeback = writeback;
        this.entries = new ArrayList<StackEntry>(entries);
        this.valid = true;
    }

    public StackInfo(StackInfo toCopy) {
        if (!toCopy.isValid()) {
            return;
        }
        this.lineNumber = toCopy.getLineNumber();
        this.regPointer = toCopy.getRegPointer();
        this.address = toCopy.getAddress();
        this.stackType = toCopy.getStackType();
        this.store = toCopy.isStore();
        this.writeback = toCopy.isWriteback();
        List<StackEntry> oldList = toCopy.getEntries();
        for (int i = 0; i < oldList.size(); ++i) {
            this.entries.add(new StackEntry(oldList.get(i).getAddress(), oldList.get(i).getRegNumber(), oldList.get(i).getValue()));
        }
        this.valid = true;
    }

    public int getRegPointer() {
        return this.regPointer;
    }

    public int getAddress() {
        return this.address;
    }

    public StackType getStackType() {
        return this.stackType;
    }

    public boolean isStore() {
        return this.store;
    }

    public boolean isWriteback() {
        return this.writeback;
    }

    public List<StackEntry> getEntries() {
        return this.entries;
    }

    @Override
    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override
    public boolean isValid() {
        return this.valid;
    }
}

