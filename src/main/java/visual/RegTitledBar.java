/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.event.ActionEvent
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Button
 *  javafx.scene.control.Label
 *  javafx.scene.control.Tooltip
 *  javafx.scene.layout.HBox
 */
package visual;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import visual.Bases;
import visual.VisUAL;

public class RegTitledBar
extends HBox {
    final int id;
    @FXML
    private Button btnDecimal;
    @FXML
    private Button btnBinary;
    @FXML
    private Button btnHex;
    @FXML
    private Label lblReg;
    @FXML
    private Label lblValue;

    RegTitledBar(int id2) {
        this.id = id2;
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/RegTitleBar.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.lblValue.setStyle("-fx-font-family: Consolas");
            }
            if (id2 == 15) {
                this.lblReg.setText("PC");
            } else if (id2 == 14) {
                this.lblReg.setText("LR");
            } else {
                this.lblReg.setText("R" + id2);
            }
            this.lblValue.setTooltip(new Tooltip("Click to view R" + id2 + " value history."));
            this.btnDecimal.setTooltip(new Tooltip("Display R" + id2 + " in decimal format."));
            this.btnBinary.setTooltip(new Tooltip("Display R" + id2 + " in binary format."));
            this.btnHex.setTooltip(new Tooltip("Display R" + id2 + " in hexadecimal format."));
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void btnDecimalFired(ActionEvent event) {
        VisUAL.getRegAccordion().setDispBase(this.id, Bases.DEC);
        this.btnDecimal.setDisable(true);
        this.btnBinary.setDisable(false);
        this.btnHex.setDisable(false);
    }

    @FXML
    private void btnBinaryFired(ActionEvent event) {
        VisUAL.getRegAccordion().setDispBase(this.id, Bases.BIN);
        this.btnDecimal.setDisable(false);
        this.btnBinary.setDisable(true);
        this.btnHex.setDisable(false);
    }

    @FXML
    private void btnHexFired(ActionEvent event) {
        VisUAL.getRegAccordion().setDispBase(this.id, Bases.HEX);
        this.btnDecimal.setDisable(false);
        this.btnBinary.setDisable(false);
        this.btnHex.setDisable(true);
    }

    public void setValue(String value) {
        this.lblValue.setText(value);
    }

    public void setHighlight(boolean highlight) {
        if (highlight) {
            this.lblValue.setId("titlebar-value-highlighted");
        } else {
            this.lblValue.setId("titlebar-value");
        }
    }

    public void setButtonFormat(Bases base) {
        switch (base) {
            case DEC: {
                this.btnDecimal.setDisable(true);
                this.btnBinary.setDisable(false);
                this.btnHex.setDisable(false);
                break;
            }
            case BIN: {
                this.btnDecimal.setDisable(false);
                this.btnBinary.setDisable(true);
                this.btnHex.setDisable(false);
                break;
            }
            case HEX: {
                this.btnDecimal.setDisable(false);
                this.btnBinary.setDisable(false);
                this.btnHex.setDisable(true);
            }
        }
    }
}

