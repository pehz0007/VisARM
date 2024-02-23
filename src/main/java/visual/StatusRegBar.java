/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Label
 *  javafx.scene.layout.HBox
 */
package visual;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StatusRegBar
extends HBox {
    @FXML
    private Label lblNeg;
    @FXML
    private Label lblZero;
    @FXML
    private Label lblCarry;
    @FXML
    private Label lblOverflow;
    private StatusBitStyle[] styleArray = new StatusBitStyle[4];

    StatusRegBar() {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/StatusRegBar.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.lblNeg.setStyle("-fx-font-family: Consolas");
                this.lblZero.setStyle("-fx-font-family: Consolas");
                this.lblCarry.setStyle("-fx-font-family: Consolas");
                this.lblOverflow.setStyle("-fx-font-family: Consolas");
            }
            this.styleArray[0] = StatusBitStyle.NORM;
            this.styleArray[1] = StatusBitStyle.NORM;
            this.styleArray[2] = StatusBitStyle.NORM;
            this.styleArray[3] = StatusBitStyle.NORM;
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setBitStyle(StatusBit bit, StatusBitStyle style, boolean deferSceneUpdates) {
        String styleString = "";
        switch (style) {
            default: {
                styleString = "statusreg-normal";
                break;
            }
            case HIGH: {
                styleString = "statusreg-highlighted";
                break;
            }
            case TRUE: {
                styleString = "statusreg-true";
                break;
            }
            case FALSE: {
                styleString = "statusreg-false";
            }
        }
        switch (bit) {
            case N: {
                if (!deferSceneUpdates) {
                    this.lblNeg.setId(styleString);
                }
                this.styleArray[0] = style;
                break;
            }
            case Z: {
                if (!deferSceneUpdates) {
                    this.lblZero.setId(styleString);
                }
                this.styleArray[1] = style;
                break;
            }
            case C: {
                if (!deferSceneUpdates) {
                    this.lblCarry.setId(styleString);
                }
                this.styleArray[2] = style;
                break;
            }
            case V: {
                if (!deferSceneUpdates) {
                    this.lblOverflow.setId(styleString);
                }
                this.styleArray[3] = style;
            }
        }
    }

    public void resetBitStyle(StatusBitStyle style, boolean deferSceneUpdates) {
        String styleString = "";
        switch (style) {
            default: {
                styleString = "statusreg-normal";
                break;
            }
            case HIGH: {
                styleString = "statusreg-highlighted";
                break;
            }
            case TRUE: {
                styleString = "statusreg-true";
                break;
            }
            case FALSE: {
                styleString = "statusreg-false";
            }
        }
        if (!deferSceneUpdates) {
            this.lblNeg.setId(styleString);
            this.lblZero.setId(styleString);
            this.lblCarry.setId(styleString);
            this.lblOverflow.setId(styleString);
        }
        this.styleArray[0] = style;
        this.styleArray[1] = style;
        this.styleArray[2] = style;
        this.styleArray[3] = style;
    }

    public void setBitValue(StatusBit bit, boolean value, boolean deferSceneUpdates) {
        String valueString = "";
        valueString = value ? valueString + "1" : "0";
        switch (bit) {
            case N: {
                if (!deferSceneUpdates) {
                    this.lblNeg.setText(valueString);
                    this.lblNeg.setId("statusreg-highlighted");
                }
                this.styleArray[0] = StatusBitStyle.HIGH;
                break;
            }
            case Z: {
                if (!deferSceneUpdates) {
                    this.lblZero.setText(valueString);
                    this.lblZero.setId("statusreg-highlighted");
                }
                this.styleArray[1] = StatusBitStyle.HIGH;
                break;
            }
            case C: {
                if (!deferSceneUpdates) {
                    this.lblCarry.setText(valueString);
                    this.lblCarry.setId("statusreg-highlighted");
                }
                this.styleArray[2] = StatusBitStyle.HIGH;
                break;
            }
            case V: {
                if (!deferSceneUpdates) {
                    this.lblCarry.setText(valueString);
                    this.lblCarry.setId("statusreg-highlighted");
                }
                this.styleArray[3] = StatusBitStyle.HIGH;
            }
        }
    }

    public void setBitValueOnly(StatusBit bit, boolean value) {
        String valueString = "";
        valueString = value ? valueString + "1" : "0";
        switch (bit) {
            case N: {
                this.lblNeg.setText(valueString);
                break;
            }
            case Z: {
                this.lblZero.setText(valueString);
                break;
            }
            case C: {
                this.lblCarry.setText(valueString);
                break;
            }
            case V: {
                this.lblOverflow.setText(valueString);
            }
        }
    }

    public void resetBitValues(boolean value) {
        String valueString = "";
        valueString = value ? valueString + "1" : "0";
        this.lblNeg.setText(valueString);
        this.lblZero.setText(valueString);
        this.lblCarry.setText(valueString);
        this.lblOverflow.setText(valueString);
    }

    public StatusBitStyle getBitStyle(StatusBit bit) {
        switch (bit) {
            default: {
                return this.styleArray[0];
            }
            case Z: {
                return this.styleArray[1];
            }
            case C: {
                return this.styleArray[2];
            }
            case V: 
        }
        return this.styleArray[3];
    }

    public void applySavedStyles() {
        this.setBitStyle(StatusBit.N, this.styleArray[0], false);
        this.setBitStyle(StatusBit.Z, this.styleArray[1], false);
        this.setBitStyle(StatusBit.C, this.styleArray[2], false);
        this.setBitStyle(StatusBit.V, this.styleArray[3], false);
    }
}

