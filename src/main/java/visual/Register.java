/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.istack.internal.NotNull
 */
package visual;

import visual.RegType;

public class Register
implements Comparable<Register> {
    private int value;
    private final RegType type;

    public Register(Register oldRegister) {
        this.value = oldRegister.value;
        this.type = oldRegister.type;
    }

    public Register(int value, RegType type) {
        this.value = value;
        this.type = type;
    }

    public int getValue() {
        if (this.type == RegType.R15) {
            return this.value + 8;
        }
        return this.value;
    }

    public RegType getType() {
        return this.type;
    }

    public void setValue(int value) throws WriteToProgramCounterException {
        this.value = value;
        if (this.type == RegType.R15) {
            throw new WriteToProgramCounterException(value);
        }
    }

    @Override
    public int compareTo(Register r) {
        int BEFORE = -1;
        boolean EQUALS = false;
        boolean AFTER = true;
        if (this.type.ordinal() < r.type.ordinal()) {
            return -1;
        }
        if (this.type.ordinal() > r.type.ordinal()) {
            return 1;
        }
        return 0;
    }

    public class WriteToProgramCounterException
    extends Exception {
        private int branchToAddress;

        public WriteToProgramCounterException(int branchToAddress) {
            this.branchToAddress = branchToAddress;
        }

        public int getBranchToAddress() {
            return this.branchToAddress;
        }
    }
}

