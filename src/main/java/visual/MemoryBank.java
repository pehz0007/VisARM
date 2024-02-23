/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 */
package visual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javafx.application.Platform;
import visual.Emulator;
import visual.MemWord;
import visual.MemoryMode;
import visual.MemoryWindow;
import visual.MemoryWindowPopOver;
import visual.SymbolWindow;
import visual.VisUAL;

public class MemoryBank {
    private static int baseAddress = 0;
    private static int initValue = 0;
    private List<MemWord> words = new ArrayList<MemWord>();
    private List<MemWord> changed = new ArrayList<MemWord>();
    private MemoryWindow memoryWindow;
    private SymbolWindow symbolWindow;

    public MemoryBank(MemoryWindow memoryWindow, SymbolWindow symbolWindow) {
        this.memoryWindow = memoryWindow;
        this.symbolWindow = symbolWindow;
    }

    public int addWords(List<Integer> l, boolean deferSceneUpdates) {
        int baseIndex = this.words.size() * 4 + baseAddress;
        int wordOffset = 0;
        for (Integer i : l) {
            if (i == null) {
                return baseIndex;
            }
            this.words.add(new MemWord(i, baseIndex + wordOffset));
            wordOffset += 4;
        }
        return baseIndex;
    }

    public void addWordAtAddress(int a, int v) throws MemAccessException {
        if (Integer.compareUnsigned(a, baseAddress) < 0) {
            throw new MemAccessException(VisUAL.getEmulator().getCurrentInstLine(), "Attempting to add word in instruction memory space at address 0x" + Integer.toHexString(a).toUpperCase(), "Memory access to instruction memory space is not allowed. Press F2 to view the memory map guide for details.");
        }
        MemWord w = new MemWord(v, a);
        this.words.add(w);
        this.changed.add(w);
    }

    public int getWord(int a, boolean b) throws MemAccessException {
        if (Integer.compareUnsigned(a, baseAddress) < 0) {
            throw new MemAccessException(VisUAL.getEmulator().getCurrentInstLine(), "Attempting to read word from instruction memory space at address 0x" + Integer.toHexString(a).toUpperCase(), "Memory access to instruction memory space is not allowed. Press F2 to view the memory map guide for details.");
        }
        if (Integer.remainderUnsigned(a, 4) != 0 && !b) {
            throw new MemAccessException(VisUAL.getEmulator().getCurrentInstLine(), "Attempting to perform unaligned word access", "In older ARM architectures, unaligned word access to memory cannot be made. Word addresses for LDR and STR instructions must be multiples of 4.");
        }
        for (MemWord w : this.words) {
            if (w == null) break;
            if (w.getAddress() != a) continue;
            return w.getValue();
        }
        return initValue;
    }

    public MemWord getMemWord(int a, boolean b, boolean read) throws MemAccessException {
        if (Integer.compareUnsigned(a, baseAddress) < 0) {
            throw new MemAccessException(VisUAL.getEmulator().getCurrentInstLine(), "Attempting to read word from instruction memory space at address 0x" + Integer.toHexString(a).toUpperCase(), "Memory access to instruction memory space is not allowed. Press F2 to view the memory map guide for details.");
        }
        if (Integer.remainderUnsigned(a, 4) != 0 && !b) {
            throw new MemAccessException(VisUAL.getEmulator().getCurrentInstLine(), "Attempting to perform unaligned word access", "In older ARM architectures, unaligned word access to memory cannot be made. Word addresses for LDR and STR instructions must be multiples of 4.");
        }
        for (MemWord w : this.words) {
            if (w == null) break;
            if (w.getAddress() != a) continue;
            return w;
        }
        MemoryMode memAccessMode = VisUAL.getSettingsManager().getMemAccessMode();
        if (read || memAccessMode == MemoryMode.OPEN) {
            MemWord w;
            w = new MemWord(initValue, a);
            this.words.add(w);
            return w;
        }
        return null;
    }

    public MemWord getMemWordForByte(int a, boolean read) throws MemAccessException {
        int byteOffset = (int)(Long.parseLong(Integer.toHexString(a), 16) % 4L);
        int wordIndex = a - byteOffset;
        if (Integer.compareUnsigned(wordIndex, baseAddress) < 0) {
            throw new MemAccessException(VisUAL.getEmulator().getCurrentInstLine(), "Attempting to read word from instruction memory space at address 0x" + Integer.toHexString(wordIndex).toUpperCase(), "Memory access to instruction memory space is not allowed. Press F2 to view the memory map guide for details.");
        }
        return this.getMemWord(wordIndex, true, read);
    }

    public int getByte(int i) throws MemAccessException {
        int byteOffset = (int)(Long.parseLong(Integer.toHexString(i), 16) % 4L);
        int wordIndex = i - byteOffset;
        if (Integer.compareUnsigned(wordIndex, baseAddress) < 0) {
            throw new MemAccessException(VisUAL.getEmulator().getCurrentInstLine(), "Attempting to read byte from instruction memory space at address 0x" + Integer.toHexString(wordIndex).toUpperCase(), "Memory access to instruction memory space is not allowed. Press F2 to view the memory map guide for details.");
        }
        int word = this.getWord(wordIndex, true);
        byte b = (byte)(word >> 8 * byteOffset);
        return b & 0xFF;
    }

    public void setWord(int a, int v, boolean b, boolean deferSceneUpdates) throws MemAccessException {
        if (Integer.compareUnsigned(a, baseAddress) < 0) {
            throw new MemAccessException(VisUAL.getEmulator().getCurrentInstLine(), "Attempting to set word in instruction memory space at address 0x" + Integer.toHexString(a).toUpperCase(), "Memory access to instruction memory space is not allowed. Press F2 to view the memory map guide for details.");
        }
        if (Integer.remainderUnsigned(a, 4) != 0 && !b) {
            throw new MemAccessException(VisUAL.getEmulator().getCurrentInstLine(), "Attempting to perform unaligned word access", "In older ARM architectures, unaligned word access to memory cannot be made. Word addresses for LDR and STR instructions must be multiples of 4.");
        }
        MemWord w = this.getMemWord(a, b, false);
        if (w != null) {
            w.setValue(v);
            this.changed.add(w);
        } else {
            System.out.println("Attempting to write to undefined address in memory!");
            System.out.println(VisUAL.getSettingsManager().getMemAccessMode().name());
            switch (VisUAL.getSettingsManager().getMemAccessMode()) {
                case OPEN: {
                    this.addWordAtAddress(a, v);
                    break;
                }
                case STRICT: {
                    throw new MemAccessException(VisUAL.getEmulator().getCurrentInstLine(), "Attempting to write to undefined address at 0x " + Integer.toHexString(a).toUpperCase() + " in restricted memory access mode", "In restricted memory access mode, write access is limited to addresses pre-defined using the DCD, DCB, and FILL directives. Press F2 to view the memory map for details.");
                }
            }
        }
    }

    public void setByte(int a, byte v, boolean deferSceneUpdates) throws MemAccessException {
        int byteOffset = (int)(Long.parseLong(Integer.toHexString(a), 16) % 4L);
        int wordIndex = a - byteOffset;
        if (Integer.compareUnsigned(wordIndex, baseAddress) < 0) {
            throw new MemAccessException(VisUAL.getEmulator().getCurrentInstLine(), "Attempting to set byte in instruction memory space at address 0x" + Integer.toHexString(wordIndex).toUpperCase(), "Memory access to instruction memory space is not allowed. Press F2 to view the memory map guide for details.");
        }
        int word = this.getWord(wordIndex, true);
        byte[] bytes = new byte[]{(byte)word, (byte)(word >>> 8), (byte)(word >>> 16), (byte)(word >>> 24)};
        bytes[byteOffset] = v;
        word = bytes[0] & 0xFF | (bytes[1] & 0xFF) << 8 | (bytes[2] & 0xFF) << 16 | bytes[3] << 24;
        this.setWord(wordIndex, word, deferSceneUpdates, true);
    }

    private void updateSymbolMemoryViews(boolean deferSceneUpdates) {
        Platform.runLater(() -> {
            try {
                Emulator emulator = VisUAL.getEmulator();
                this.memoryWindow.updateContents(this, deferSceneUpdates);
                this.symbolWindow.updateContents(emulator.labels, emulator.symbols, emulator.dcdWords, deferSceneUpdates);
            }
            catch (MemAccessException e) {
                VisUAL.getUIController().handleException(e, e.getLineNumber());
            }
        });
    }

    public void resetChanged() {
        this.changed.clear();
    }

    public List<MemWord> getChanged() {
        return this.changed;
    }

    public static void setBaseAddress(int value) {
        baseAddress = value;
    }

    public static void setInitValue(int value) {
        initValue = value;
    }

    public List<MemoryWindowPopOver> getDataMarkers() {
        ArrayList<MemoryWindowPopOver> dataMarkers = new ArrayList<MemoryWindowPopOver>();
        int lastI = -67108865;
        int i = 0;
        while (Integer.compareUnsigned(i, -67108865) < 0) {
            int startI = i;
            int endI = i + 0x4000000;
            List wordsInRange = this.words.parallelStream().filter(w -> Integer.compareUnsigned(w.getAddress(), startI) >= 0 && Integer.compareUnsigned(w.getAddress(), endI) < 0).collect(Collectors.toList());
            if (!wordsInRange.isEmpty()) {
                if (i == 0) {
                    dataMarkers.add(new MemoryWindowPopOver(baseAddress, endI, wordsInRange.size()));
                } else {
                    dataMarkers.add(new MemoryWindowPopOver(startI, endI, wordsInRange.size()));
                }
            }
            i += 0x4000000;
        }
        List wordsInRange = this.words.parallelStream().filter(w -> Integer.compareUnsigned(w.getAddress(), -67108865) >= 0).collect(Collectors.toList());
        if (!wordsInRange.isEmpty()) {
            dataMarkers.add(new MemoryWindowPopOver(-67108865, -1, wordsInRange.size()));
        }
        return dataMarkers;
    }

    public List<MemWord> getWordsInRange(int startAddress, int endAddress) {
        List<MemWord> wordsInRange = this.words.parallelStream().filter(w -> Integer.compareUnsigned(w.getAddress(), startAddress) >= 0 && Integer.compareUnsigned(w.getAddress(), endAddress) <= 0).collect(Collectors.toList());
        Collections.sort(wordsInRange);
        return wordsInRange;
    }

    public class MemAccessException
    extends Exception {
        private String details;
        private int lineNumber;

        public MemAccessException(int lineNumber, String message, String details) {
            super(message);
            this.lineNumber = lineNumber;
            this.details = details;
        }

        public int getLineNumber() {
            return this.lineNumber;
        }

        public String getDetails() {
            return this.details;
        }
    }
}

