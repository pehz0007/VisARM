/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.istack.internal.NotNull
 */
package visual;


public class StackEntry
implements Comparable<StackEntry> {
    private int address;
    private int regNumber;
    private int value;

    public StackEntry(int address, int regNumber, int value) {
        this.address = address;
        this.regNumber = regNumber;
        this.value = value;
    }

    public int getAddress() {
        return this.address;
    }

    public int getRegNumber() {
        return this.regNumber;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public int compareTo(StackEntry e) {
        int BEFORE = -1;
        boolean EQUALS = false;
        boolean AFTER = true;
        if (Integer.compareUnsigned(this.address, e.getAddress()) < 0) {
            return -1;
        }
        if (Integer.compareUnsigned(this.address, e.getAddress()) > 0) {
            return 1;
        }
        return 0;
    }
}

