/*
 * Decompiled with CFR 0.152.
 */
package visual;

import visual.PointerType;
import visual.VisualisationInfo;

public class PointerInfo
extends VisualisationInfo {
    private String name;
    private int baseAdress;
    private int offset;
    private int currentValue;
    private boolean preIndexed = false;
    private boolean postIndexed = false;
    private PointerType type;
    private String destination;

    public PointerInfo(String name, int baseAddress, int lineNumber) {
        this.name = name;
        this.baseAdress = baseAddress;
        this.lineNumber = lineNumber;
        this.valid = true;
    }

    public PointerInfo(PointerInfo toCopy) {
        if (toCopy == null || !toCopy.isValid()) {
            this.valid = false;
            return;
        }
        this.valid = true;
        this.name = toCopy.getName();
        this.baseAdress = toCopy.getBaseAddress();
        this.offset = toCopy.getOffset();
        this.currentValue = toCopy.getCurrentValue();
        this.preIndexed = toCopy.isPreIndexed();
        this.postIndexed = toCopy.isPostIndexed();
        this.type = toCopy.getType();
        this.destination = toCopy.getDestination();
        this.lineNumber = toCopy.getLineNumber();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBaseAddress() {
        return this.baseAdress;
    }

    public void setBaseAddress(int baseAddress) {
        this.baseAdress = baseAddress;
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCurrentValue() {
        return this.currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public boolean isPreIndexed() {
        return this.preIndexed;
    }

    public void setPreIndexed(boolean preIndexed) {
        this.preIndexed = preIndexed;
    }

    public boolean isPostIndexed() {
        return this.postIndexed;
    }

    public void setPostIndexed(boolean postIndexed) {
        this.postIndexed = postIndexed;
    }

    public void setType(PointerType type) {
        this.type = type;
    }

    public PointerType getType() {
        return this.type;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return this.destination;
    }
}

