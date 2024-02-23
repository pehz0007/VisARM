/*
 * Decompiled with CFR 0.152.
 */
package visual;

public class MemWord
implements Comparable<MemWord> {
    private int value;
    private int address;

    public MemWord(MemWord w) {
        this.value = w.value;
        this.address = w.address;
    }

    public MemWord(int value, int address) {
        this.value = value;
        this.address = address;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public int getAddress() {
        return this.address;
    }

    @Override
    public int compareTo(MemWord w) {
        int BEFORE = -1;
        boolean EQUALS = false;
        boolean AFTER = true;
        if (Integer.compareUnsigned(this.address, w.getAddress()) == -1) {
            return -1;
        }
        if (Integer.compareUnsigned(this.address, w.getAddress()) == 1) {
            return 1;
        }
        return 0;
    }
}

