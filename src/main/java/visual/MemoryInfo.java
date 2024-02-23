/*
 * Decompiled with CFR 0.152.
 */
package visual;

import java.util.ArrayList;
import java.util.List;
import visual.AddressType;
import visual.MemWord;
import visual.VisualisationInfo;

public class MemoryInfo
extends VisualisationInfo {
    private boolean write = false;
    private boolean bytes = false;
    private int numBytes = 0;
    private int baseByteIndex = 0;
    private int actualByteIndex = 0;
    private int baseAddress = -1;
    private int lineNumber = -1;
    private List<MemWord> words = new ArrayList<MemWord>();
    private List<String> names = new ArrayList<String>();
    private List<AddressType> types = new ArrayList<AddressType>();
    private boolean valid = false;

    public MemoryInfo(boolean write, boolean bytes, int lineNumber) {
        this.write = write;
        this.bytes = bytes;
        this.lineNumber = lineNumber;
        this.valid = true;
    }

    public MemoryInfo(MemoryInfo toCopy) {
        if (toCopy == null || !toCopy.isValid()) {
            this.valid = false;
            return;
        }
        this.valid = true;
        this.write = toCopy.isWrite();
        this.bytes = toCopy.isBytes();
        this.numBytes = toCopy.numBytes;
        this.baseByteIndex = toCopy.getBaseByteIndex();
        this.actualByteIndex = toCopy.getActualByteIndex();
        this.baseAddress = toCopy.getBaseAddress();
        this.lineNumber = toCopy.getLineNumber();
        for (int i = 0; i < toCopy.getWords().size(); ++i) {
            this.words.add(new MemWord(toCopy.getWords().get(i)));
        }
        this.names = new ArrayList<String>(toCopy.getNames());
        this.types = new ArrayList<AddressType>(toCopy.getTypes());
    }

    public void setByteCounts(int numBytes, int baseByteIndex, int actualByteIndex) {
        this.numBytes = numBytes;
        this.baseByteIndex = baseByteIndex;
        this.actualByteIndex = actualByteIndex;
    }

    public void setBaseAddress(int baseAddress) {
        this.baseAddress = baseAddress;
    }

    public int getBaseAddress() {
        return this.baseAddress;
    }

    public int getNumBytes() {
        return this.numBytes;
    }

    public int getBaseByteIndex() {
        return this.baseByteIndex;
    }

    public int getActualByteIndex() {
        return this.actualByteIndex;
    }

    public boolean isWrite() {
        return this.write;
    }

    public boolean isBytes() {
        return this.bytes;
    }

    public List<MemWord> getWords() {
        return this.words;
    }

    public List<String> getNames() {
        return this.names;
    }

    public List<AddressType> getTypes() {
        return this.types;
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

