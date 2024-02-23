/*
 * Decompiled with CFR 0.152.
 */
package visual;

import java.util.ArrayList;
import java.util.List;
import visual.MemoryBank;

public class DcdWord {
    private String name = "";
    private int address;
    private int lineNumber;

    public DcdWord(String name, List<Integer> values, MemoryBank memory, int lineNumber, boolean byteArray, boolean deferSceneUpdates) {
        if (byteArray) {
            int wordCount = values.size() % 4 == 0 ? values.size() / 4 : values.size() / 4 + 1;
            ArrayList<Integer> byteValues = new ArrayList<Integer>();
            for (int i = 0; i < wordCount * 4; i += 4) {
                int wordValue = 0;
                for (int j = 0; j < 4 && i + j < values.size(); ++j) {
                    wordValue += (values.get(i + j) & 0xFF) << j * 8;
                }
                byteValues.add(wordValue);
            }
            this.name = name;
            this.address = memory.addWords(byteValues, deferSceneUpdates);
            this.lineNumber = lineNumber;
        } else {
            this.name = name;
            this.address = memory.addWords(values, deferSceneUpdates);
            this.lineNumber = lineNumber;
        }
    }

    public String getName() {
        return this.name;
    }

    public int getAddress() {
        return this.address;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }
}

