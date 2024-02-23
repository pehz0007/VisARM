/*
 * Decompiled with CFR 0.152.
 */
package visual;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import visual.Bases;
import visual.EmulatorLogFile;
import visual.MemoryBank;
import visual.MemoryMode;
import visual.VisUAL;

public class SettingsManager {
    private Preferences prefs;
    private final String id0 = "FirstRun";
    private final String id1 = "PointerInfo";
    private final String id2 = "ShiftOps";
    private final String id3 = "MemoryAccess";
    private final String id4 = "RegisterFormat";
    private final String id5 = "DefaultDirectory";
    private final String id6 = "SkipFexec";
    private final String id7 = "FontSize";
    private final String id8 = "EditorTheme";
    private final String id9 = "LoopThreshold";
    private final String id10 = "MemoryInitValue";
    private final String id11 = "LoggingEnabled";
    private final String id12 = "LoggingMode";
    private final String id13 = "LogFileLocation";
    private final String id14 = "LogRegisters";
    private final String id15 = "LogStatusBits";
    private final String id16 = "LogSyntaxErrors";
    private final String id17 = "LogRuntimeErrors";
    private final String id18 = "LogMemChanged";
    private final String id19 = "LogMemCustom";
    private final String id20 = "LogMemCustomLocations";
    private final String id21 = "StackVis";
    private final String id22 = "InstMemSize";
    private final String id23 = "StackPointerValue";
    private final String id24 = "AutoIndentCode";
    private final String id25 = "WarnResetToEdit";
    private final String id26 = "AutoInstSize";
    private final String id27 = "MemAccessMode";
    private final String id28 = "LogCycleCount";
    private final String id29 = "CycleModel";
    private final String id30 = "BranchVis";

    public SettingsManager() {
        this.setDefaults();
    }

    private void setDefaults() {
        this.prefs = Preferences.userRoot().node(this.getClass().getName());
        this.prefs.getBoolean("FirstRun", true);
        this.prefs.getBoolean("PointerInfo", true);
        this.prefs.getBoolean("ShiftOps", true);
        this.prefs.getBoolean("MemoryAccess", true);
        this.prefs.getBoolean("StackVis", true);
        this.prefs.getBoolean("BranchVis", true);
        this.prefs.get("RegisterFormat", "Hexadecimal");
        this.prefs.get("DefaultDirectory", System.getProperty("user.home"));
        this.prefs.getBoolean("SkipFexec", false);
        this.prefs.get("FontSize", "14");
        this.prefs.get("EditorTheme", "Dark");
        this.prefs.get("LoopThreshold", "1000");
        this.prefs.get("MemoryInitValue", "0x00000000");
        this.prefs.get("InstMemSize", "0x10000");
        this.prefs.get("StackPointerValue", "0xFF000000");
        this.prefs.getBoolean("AutoIndentCode", true);
        this.prefs.getBoolean("WarnResetToEdit", true);
        this.prefs.getBoolean("AutoInstSize", true);
        this.prefs.get("MemAccessMode", "OPEN");
        this.prefs.get("CycleModel", "");
        this.prefs.getBoolean("LoggingEnabled", false);
        this.prefs.get("LoggingMode", "All");
        this.prefs.get("LogFileLocation", this.prefs.get("DefaultDirectory", System.getProperty("user.home")));
        this.prefs.getBoolean("LogRegisters", true);
        this.prefs.getBoolean("LogStatusBits", true);
        this.prefs.getBoolean("LogSyntaxErrors", true);
        this.prefs.getBoolean("LogRuntimeErrors", true);
        this.prefs.getBoolean("LogMemChanged", true);
        this.prefs.getBoolean("LogMemCustom", false);
        this.prefs.get("LogMemCustomLocations", "");
        this.prefs.getBoolean("LogCycleCount", true);
    }

    public void setPointerInfo(boolean value) {
        this.prefs.putBoolean("PointerInfo", value);
    }

    public void setShiftOps(boolean value) {
        this.prefs.putBoolean("ShiftOps", value);
    }

    public void setMemoryAccess(boolean value) {
        this.prefs.putBoolean("MemoryAccess", value);
    }

    public void setStackVisualisation(boolean value) {
        this.prefs.putBoolean("StackVis", value);
    }

    public void setBranchVisualisation(boolean value) {
        this.prefs.putBoolean("BranchVis", value);
    }

    public void setRegisterFormat(Bases base) {
        switch (base) {
            case DEC: {
                this.prefs.put("RegisterFormat", "Decimal");
                break;
            }
            case BIN: {
                this.prefs.put("RegisterFormat", "Binary");
                break;
            }
            case HEX: {
                this.prefs.put("RegisterFormat", "Hexadecimal");
            }
        }
        VisUAL.getRegAccordion().resetDispBases(base);
    }

    public void setDefaultDirectory(String path) {
        this.prefs.put("DefaultDirectory", path);
    }

    public void setSkipFexec(boolean value) {
        this.prefs.putBoolean("SkipFexec", value);
    }

    public void setFontSize(int value) {
        this.prefs.put("FontSize", "" + value);
    }

    public void setEditorTheme(String themeName) {
        this.prefs.put("EditorTheme", themeName);
    }

    public void setAutoIndentCode(boolean value) {
        this.prefs.putBoolean("AutoIndentCode", value);
    }

    public void setWarnResetToEdit(boolean value) {
        this.prefs.putBoolean("WarnResetToEdit", value);
    }

    public void setFirstRun(boolean value) {
        this.prefs.putBoolean("FirstRun", value);
    }

    public void setLoopThreshold(int value) {
        this.prefs.put("LoopThreshold", "" + value);
    }

    public void setMemoryInitValue(int value) {
        MemoryBank.setInitValue(value);
        this.prefs.put("MemoryInitValue", "0x" + Integer.toHexString(value).toUpperCase());
    }

    public void setStackPointerValue(int value) {
        this.prefs.put("StackPointerValue", "0x" + Integer.toHexString(value).toUpperCase());
    }

    public void setInstMemSize(int value) {
        this.prefs.put("InstMemSize", "0x" + Integer.toHexString(value).toUpperCase());
    }

    public void setAutoInstSize(boolean value) {
        this.prefs.putBoolean("AutoInstSize", value);
    }

    public void setLoggingEnabled(boolean value) {
        this.prefs.putBoolean("LoggingEnabled", value);
        EmulatorLogFile.reconfigureLogging();
    }

    public void setLoggingMode(String value) {
        this.prefs.put("LoggingMode", value);
        EmulatorLogFile.reconfigureLogging();
    }

    public void setLogFileLocation(String value) {
        this.prefs.put("LogFileLocation", value);
        EmulatorLogFile.reconfigureLogging();
    }

    public void setLogRegisters(boolean value) {
        this.prefs.putBoolean("LogRegisters", value);
        EmulatorLogFile.reconfigureLogging();
    }

    public void setLogStatusBits(boolean value) {
        this.prefs.putBoolean("LogStatusBits", value);
        EmulatorLogFile.reconfigureLogging();
    }

    public void setLogSyntaxErrors(boolean value) {
        this.prefs.putBoolean("LogSyntaxErrors", value);
        EmulatorLogFile.reconfigureLogging();
    }

    public void setLogRuntimeErrors(boolean value) {
        this.prefs.putBoolean("LogRuntimeErrors", value);
        EmulatorLogFile.reconfigureLogging();
    }

    public void setLogMemChanged(boolean value) {
        this.prefs.putBoolean("LogMemChanged", value);
        EmulatorLogFile.reconfigureLogging();
    }

    public void setLogMemCustom(boolean value) {
        this.prefs.putBoolean("LogMemCustom", value);
        EmulatorLogFile.reconfigureLogging();
    }

    public void setLogCycleCount(boolean value) {
        this.prefs.putBoolean("LogCycleCount", value);
        EmulatorLogFile.reconfigureLogging();
    }

    public void setCycleModel(String value) {
        this.prefs.put("CycleModel", value);
    }

    public void setLogMemCustomLocations(String[] locations) {
        String toSave = "";
        for (int i = 0; i < locations.length; ++i) {
            toSave = toSave + locations[i];
            if (i == locations.length - 1) continue;
            toSave = toSave + ",";
        }
        this.prefs.put("LogMemCustomLocations", toSave);
        EmulatorLogFile.reconfigureLogging();
    }

    public void setMemAccessMode(MemoryMode mode) {
        this.prefs.put("MemAccessMode", mode.name());
    }

    public MemoryMode getMemAccessMode() {
        return MemoryMode.valueOf(this.prefs.get("MemAccessMode", "OPEN"));
    }

    public boolean getPointerInfo() {
        return this.prefs.getBoolean("PointerInfo", true);
    }

    public boolean getShiftOps() {
        return this.prefs.getBoolean("ShiftOps", true);
    }

    public boolean getMemoryAccess() {
        return this.prefs.getBoolean("MemoryAccess", true);
    }

    public boolean getStackVis() {
        return this.prefs.getBoolean("StackVis", true);
    }

    public boolean getBranchVis() {
        return this.prefs.getBoolean("BranchVis", true);
    }

    public Bases getRegisterFormat() {
        switch (this.prefs.get("RegisterFormat", "Hexadecimal")) {
            default: {
                return Bases.DEC;
            }
            case "Binary": {
                return Bases.BIN;
            }
            case "Hexadecimal": 
        }
        return Bases.HEX;
    }

    public String getDefaultDirectory() {
        return this.prefs.get("DefaultDirectory", System.getProperty("user.home"));
    }

    public boolean getSkipFexec() {
        return this.prefs.getBoolean("SkipFexec", false);
    }

    public int getFontSize() {
        return Integer.parseInt(this.prefs.get("FontSize", "14"));
    }

    public String getThemeName() {
        return this.prefs.get("EditorTheme", "Dark");
    }

    public boolean isFirstRun() {
        return this.prefs.getBoolean("FirstRun", true);
    }

    public boolean getAutoIndentCode() {
        return this.prefs.getBoolean("AutoIndentCode", true);
    }

    public boolean getWarnResetToEdit() {
        return this.prefs.getBoolean("WarnResetToEdit", true);
    }

    public int getLoopThreshold() {
        return Integer.parseInt(this.prefs.get("LoopThreshold", "1000"));
    }

    public int getMemoryInitValue() {
        return (int)Long.parseLong(this.prefs.get("MemoryInitValue", "0x00000000").substring(2), 16);
    }

    public int getStackPointerValue() {
        return (int)Long.parseLong(this.prefs.get("StackPointerValue", "0xFF000000").substring(2), 16);
    }

    public int getInstMemSize() {
        return (int)Long.parseLong(this.prefs.get("InstMemSize", "0x10000").substring(2), 16);
    }

    public boolean getAutoInstSize() {
        return this.prefs.getBoolean("AutoInstSize", true);
    }

    public boolean getLoggingEnabled() {
        return this.prefs.getBoolean("LoggingEnabled", false);
    }

    public EmulatorLogFile.LogMode getLoggingMode() {
        EmulatorLogFile.LogMode logMode;
        String modeString;
        switch (modeString = this.prefs.get("LoggingMode", "All").toLowerCase()) {
            default: {
                logMode = EmulatorLogFile.LogMode.ALL;
                break;
            }
            case "completion": {
                logMode = EmulatorLogFile.LogMode.COMPLETION;
                break;
            }
            case "breakpoint": {
                logMode = EmulatorLogFile.LogMode.BREAKPOINT;
            }
        }
        return logMode;
    }

    public String getLogFileLocation() {
        return this.prefs.get("LogFileLocation", this.prefs.get("DefaultDirectory", System.getProperty("user.home")));
    }

    public boolean getLogRegisters() {
        return this.prefs.getBoolean("LogRegisters", true);
    }

    public boolean getLogStatusBits() {
        return this.prefs.getBoolean("LogStatusBits", true);
    }

    public boolean getLogSyntaxErrors() {
        return this.prefs.getBoolean("LogSyntaxErrors", true);
    }

    public boolean getLogRuntimeErrors() {
        return this.prefs.getBoolean("LogRuntimeErrors", true);
    }

    public boolean getLogMemChanged() {
        return this.prefs.getBoolean("LogMemChanged", true);
    }

    public boolean getLogMemCustom() {
        return this.prefs.getBoolean("LogMemCustom", true);
    }

    public boolean getLogCycleCount() {
        return this.prefs.getBoolean("LogCycleCount", true);
    }

    public String getCycleModel() {
        return this.prefs.get("CycleModel", "");
    }

    public String[] getLogMemCustomLocations() {
        return this.prefs.get("LogMemCustomLocations", "").split(",");
    }

    public void resetAll() {
        try {
            this.prefs.removeNode();
            this.setDefaults();
        }
        catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }
}

