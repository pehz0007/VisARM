/*
 * Decompiled with CFR 0.152.
 */
package visual;

import java.util.ArrayList;
import java.util.List;
import visual.Bases;
import visual.ShiftType;
import visual.VisualisationInfo;

public class ShiftInfo
extends VisualisationInfo {
    private List<String> values = new ArrayList<String>();
    private ShiftType type;
    private List<String> carrys = new ArrayList<String>();
    private boolean useShiftCarry;
    private int count;
    private Bases dispFormat = Bases.HEX;
    private int lineNumber = -1;
    private boolean valid = false;

    public ShiftInfo(List<String> values, ShiftType type, int count, List<String> carrys, boolean useShiftCarry, Bases dispFormat, int lineNumber) {
        this.values = new ArrayList<String>(values);
        this.carrys = new ArrayList<String>(carrys);
        this.useShiftCarry = useShiftCarry;
        this.type = type;
        this.count = count;
        this.dispFormat = dispFormat;
        this.lineNumber = lineNumber;
        this.valid = true;
    }

    public ShiftInfo(ShiftInfo toCopy) {
        if (toCopy == null || !toCopy.isValid()) {
            this.valid = false;
            return;
        }
        this.valid = true;
        this.values = new ArrayList<String>(toCopy.getValues());
        this.type = toCopy.getType();
        this.carrys = new ArrayList<String>(toCopy.getCarrys());
        this.useShiftCarry = toCopy.useShiftCarry();
        this.count = toCopy.getCount();
        this.dispFormat = toCopy.getDispFormat();
        this.lineNumber = toCopy.getLineNumber();
    }

    public ShiftType getType() {
        return this.type;
    }

    public List<String> getValues() {
        return this.values;
    }

    public List<String> getCarrys() {
        return this.carrys;
    }

    public boolean useShiftCarry() {
        return this.useShiftCarry;
    }

    public int getCount() {
        return this.count;
    }

    public Bases getDispFormat() {
        return this.dispFormat;
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

