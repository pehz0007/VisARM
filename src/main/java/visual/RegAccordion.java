/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.control.Accordion
 *  javafx.scene.control.ContentDisplay
 *  javafx.scene.control.TitledPane
 */
package visual;

import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TitledPane;

public class RegAccordion {
    private static UIController uiController = VisUAL.getUIController();
    private final TitledPane[] panes = new TitledPane[16];
    private final RegTitledPane[] regPanes = new RegTitledPane[16];
    private final RegTitledBar[] regBars = new RegTitledBar[16];
    private int[] dispValues = new int[16];
    private boolean[] highlighted = new boolean[16];
    private Bases[] dispBases = new Bases[16];
    private Accordion acc = new Accordion();
    private StatusRegBar statusRegBar = new StatusRegBar();

    RegAccordion() {
        Bases defaultBase = VisUAL.getSettingsManager().getRegisterFormat();
        for (int i = 0; i < 16; ++i) {
            this.regPanes[i] = new RegTitledPane(i);
            this.regBars[i] = new RegTitledBar(i);
            this.panes[i] = new TitledPane("R" + i + ": 0", (Node)this.regPanes[i]);
            this.panes[i].setGraphic((Node)this.regBars[i]);
            this.panes[i].setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.panes[i].getStylesheets().add("/visual/titledpane_normal.css");
            this.dispValues[i] = i != 13 ? 0 : VisUAL.getSettingsManager().getStackPointerValue();
            this.setDispBase(i, defaultBase);
        }
        this.acc.getPanes().addAll(this.panes);
        this.acc.setStyle("-fx-box-border: transparent;");
    }

    public Accordion getAccordion() {
        return this.acc;
    }

    public StatusRegBar getStatusRegBar() {
        return this.statusRegBar;
    }

    public void setRegValue(int regNo, int value, boolean highlight, boolean deferSceneUpdates, boolean restoring) {
        this.dispValues[regNo] = value;
        String strValue = "";
        switch (this.dispBases[regNo]) {
            case DEC: {
                strValue = "" + value;
                break;
            }
            case BIN: {
                strValue = "0b" + Integer.toBinaryString(value);
                break;
            }
            case HEX: {
                strValue = "0x" + Integer.toHexString(value).toUpperCase();
                break;
            }
        }
        if (!deferSceneUpdates) {
            this.regBars[regNo].setValue(strValue);
        }
        if (highlight) {
            if (!deferSceneUpdates) {
                this.regBars[regNo].setHighlight(true);
            }
            if (!restoring) {
                this.regPanes[regNo].addTableEntry(uiController.getCurrentLine(), value, deferSceneUpdates);
            }
            this.highlighted[regNo] = true;
        }
        if (regNo == 15 && !restoring) {
            this.regPanes[regNo].addTableEntry(uiController.getCurrentLine(), value, deferSceneUpdates);
        }
    }

    public void setRegValueOnly(int regNo, int value) {
        this.dispValues[regNo] = value;
        String strValue = "";
        switch (this.dispBases[regNo]) {
            case DEC: {
                strValue = "" + value;
                break;
            }
            case BIN: {
                strValue = "0b" + Integer.toBinaryString(value);
                break;
            }
            case HEX: {
                strValue = "0x" + Integer.toHexString(value).toUpperCase();
                break;
            }
        }
        this.regBars[regNo].setValue(strValue);
    }

    public void resetRegHighlight(boolean deferSceneUpdates) {
        for (int i = 0; i < 16; ++i) {
            if (!deferSceneUpdates) {
                this.regBars[i].setHighlight(false);
            }
            this.highlighted[i] = false;
        }
    }

    public boolean[] getHighlighted() {
        return this.highlighted;
    }

    public void setDispBase(int regNo, Bases base) {
        this.dispBases[regNo] = base;
        String strValue = "";
        switch (base) {
            case DEC: {
                strValue = "" + this.dispValues[regNo];
                break;
            }
            case BIN: {
                strValue = "0b" + Integer.toBinaryString(this.dispValues[regNo]);
                break;
            }
            case HEX: {
                strValue = "0x" + Integer.toHexString(this.dispValues[regNo]).toUpperCase();
                break;
            }
        }
        this.regBars[regNo].setValue(strValue);
        this.regBars[regNo].setButtonFormat(base);
        this.regPanes[regNo].setDispFormat(base);
    }

    public void resetAll(boolean deleteEntries) {
        for (int i = 0; i < 16; ++i) {
            this.dispValues[i] = i != 13 ? 0 : VisUAL.getSettingsManager().getStackPointerValue();
            this.setDispBase(i, VisUAL.getSettingsManager().getRegisterFormat());
            this.regPanes[i].setDispFormat(VisUAL.getSettingsManager().getRegisterFormat());
            if (deleteEntries) {
                this.regPanes[i].deleteEntriesAfterLine(-1);
            }
            if (deleteEntries) {
                this.regPanes[i].clearSavedStatePointers();
            }
            this.regBars[i].setHighlight(false);
        }
        this.statusRegBar.resetBitStyle(StatusBitStyle.NORM, false);
        this.statusRegBar.resetBitValues(false);
    }

    public void resetDispBases(Bases base) {
        for (int i = 0; i < 16; ++i) {
            this.setDispBase(i, base);
        }
    }

    public void resetDispBases(Bases[] bases) {
        for (int i = 0; i < 16; ++i) {
            this.setDispBase(i, bases[i]);
        }
    }

    public Bases getDispBase(int i) {
        return this.dispBases[i];
    }

    public void applySavedHighlights() {
        for (int i = 0; i < 16; ++i) {
            this.regBars[i].setHighlight(this.highlighted[i]);
        }
    }

    public void addDeferredEntries() {
        for (int i = 0; i < 16; ++i) {
            this.regPanes[i].addDeferredEntries();
        }
    }
}

