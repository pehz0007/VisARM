/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.scene.control.Alert
 *  javafx.scene.control.Alert$AlertType
 *  javafx.stage.StageStyle
 */
package visual;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import org.apache.commons.io.FileUtils;
import visual.CycleCount;
import visual.RuntimeError;
import visual.VisUAL;

public class CycleCounter {
    private List<CycleCount> cycleCounts = new ArrayList<CycleCount>();

    public static CycleCounter createInstance(String fileName) {
        if (fileName.trim().isEmpty()) {
            return new CycleCounter(false);
        }
        try {
            CycleCounter cycleCounter = new CycleCounter(true);
            List<String> cycleModel = FileUtils.readLines(new File(fileName));
            Pattern p = Pattern.compile("\\s*(?i:([A-Z]+)\\s*,\\s*([0-9]+))\\s*");
            for (String line : cycleModel) {
                Matcher m = p.matcher(line);
                if (!m.find()) continue;
                String baseOpcode = m.group(1);
                int cycles = Integer.parseInt(m.group(2));
                cycleCounter.addCycleCount(new CycleCount(baseOpcode.toUpperCase(), cycles));
            }
            if (cycleCounter.isValid()) {
                return cycleCounter;
            }
            CycleCounter.showAlert("Invalid / incomplete custom cycle model", "The custom cycle model specified is either invalid or incomplete.\nThe application will use the default model instead. Please view the cycle model specification guide in the user manual for details.", "Invalid / incomplete custom cycle model. Switching to default model...");
            return new CycleCounter(false);
        }
        catch (IOException e) {
            CycleCounter.showAlert("Unable to access custom cycle model", "The custom cycle model specified could not be opened.\nThe application will use the default model instead. Please view the cycle model specification guide in the user manual for details.", "Unable to access custom cycle model. Switching to default model...");
            return new CycleCounter(false);
        }
    }

    public static void showAlert(String headerText, String contentText, String errorMessage) {
        Alert invalidCycleModelAlert = new Alert(Alert.AlertType.ERROR);
        invalidCycleModelAlert.setTitle("Custom cycle model");
        invalidCycleModelAlert.setHeaderText(headerText);
        invalidCycleModelAlert.setContentText(contentText);
        invalidCycleModelAlert.initStyle(StageStyle.UTILITY);
        invalidCycleModelAlert.show();
        System.out.println(errorMessage);
    }

    public CycleCounter(boolean empty) {
        if (!empty) {
            this.cycleCounts.add(new CycleCount("MOV", 1));
            this.cycleCounts.add(new CycleCount("MVN", 1));
            this.cycleCounts.add(new CycleCount("ADD", 1));
            this.cycleCounts.add(new CycleCount("ADC", 1));
            this.cycleCounts.add(new CycleCount("SUB", 1));
            this.cycleCounts.add(new CycleCount("SBC", 1));
            this.cycleCounts.add(new CycleCount("RSB", 1));
            this.cycleCounts.add(new CycleCount("RSC", 1));
            this.cycleCounts.add(new CycleCount("AND", 1));
            this.cycleCounts.add(new CycleCount("EOR", 1));
            this.cycleCounts.add(new CycleCount("BIC", 1));
            this.cycleCounts.add(new CycleCount("ORR", 1));
            this.cycleCounts.add(new CycleCount("ORN", 1));
            this.cycleCounts.add(new CycleCount("ASR", 1));
            this.cycleCounts.add(new CycleCount("LSL", 1));
            this.cycleCounts.add(new CycleCount("LSR", 1));
            this.cycleCounts.add(new CycleCount("ROR", 1));
            this.cycleCounts.add(new CycleCount("RRX", 1));
            this.cycleCounts.add(new CycleCount("CMP", 1));
            this.cycleCounts.add(new CycleCount("CMN", 1));
            this.cycleCounts.add(new CycleCount("TST", 1));
            this.cycleCounts.add(new CycleCount("TEQ", 1));
            this.cycleCounts.add(new CycleCount("ADR", 1));
            this.cycleCounts.add(new CycleCount("LDR", 2));
            this.cycleCounts.add(new CycleCount("STR", 2));
            this.cycleCounts.add(new CycleCount("LDRB", 2));
            this.cycleCounts.add(new CycleCount("STRB", 2));
            this.cycleCounts.add(new CycleCount("LDM", 2));
            this.cycleCounts.add(new CycleCount("STM", 2));
            this.cycleCounts.add(new CycleCount("B", 3));
            this.cycleCounts.add(new CycleCount("BL", 3));
            this.cycleCounts.add(new CycleCount("NOP", 1));
        }
    }

    public int getCycleCount(String baseOpcode) throws RuntimeError {
        for (CycleCount c : this.cycleCounts) {
            if (!c.getBaseOpcode().toUpperCase().equals(baseOpcode.toUpperCase())) continue;
            return c.getCycleCount();
        }
        throw new RuntimeError(VisUAL.getEmulator().getCurrentInstLine(), "Could not find instruction cycle count for given instruction.", "Please check that you have specified valid instruction counts for all supported instructions if you are using custom instruction counts.");
    }

    private void addCycleCount(CycleCount c) {
        this.cycleCounts.add(c);
    }

    private boolean isValid() {
        ArrayList<String> includedOpcodes = new ArrayList<String>();
        for (CycleCount c : this.cycleCounts) {
            includedOpcodes.add(c.getBaseOpcode());
        }
        boolean isValid = includedOpcodes.contains("MOV");
        isValid &= includedOpcodes.contains("MVN");
        isValid &= includedOpcodes.contains("ADD");
        isValid &= includedOpcodes.contains("ADC");
        isValid &= includedOpcodes.contains("SUB");
        isValid &= includedOpcodes.contains("SBC");
        isValid &= includedOpcodes.contains("RSB");
        isValid &= includedOpcodes.contains("RSC");
        isValid &= includedOpcodes.contains("AND");
        isValid &= includedOpcodes.contains("EOR");
        isValid &= includedOpcodes.contains("BIC");
        isValid &= includedOpcodes.contains("ORR");
        isValid &= includedOpcodes.contains("ORN");
        isValid &= includedOpcodes.contains("ASR");
        isValid &= includedOpcodes.contains("LSL");
        isValid &= includedOpcodes.contains("LSR");
        isValid &= includedOpcodes.contains("ROR");
        isValid &= includedOpcodes.contains("RRX");
        isValid &= includedOpcodes.contains("CMP");
        isValid &= includedOpcodes.contains("CMN");
        isValid &= includedOpcodes.contains("TST");
        isValid &= includedOpcodes.contains("TEQ");
        isValid &= includedOpcodes.contains("ADR");
        isValid &= includedOpcodes.contains("LDR");
        isValid &= includedOpcodes.contains("STR");
        isValid &= includedOpcodes.contains("LDRB");
        isValid &= includedOpcodes.contains("STRB");
        isValid &= includedOpcodes.contains("LDM");
        isValid &= includedOpcodes.contains("STM");
        isValid &= includedOpcodes.contains("B");
        isValid &= includedOpcodes.contains("BL");
        return isValid &= includedOpcodes.contains("NOP");
    }
}

