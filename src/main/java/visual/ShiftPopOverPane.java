/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.event.ActionEvent
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Button
 *  javafx.scene.control.Label
 *  javafx.scene.layout.VBox
 *  javafx.scene.shape.Rectangle
 */
package visual;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.PopOver;

public class ShiftPopOverPane
extends VBox {
    final int id;
    private UIController uiController = VisUAL.getUIController();
    private PopOver parentStage;
    @FXML
    private Label shiftLabel;
    @FXML
    private Label lblSummary;
    @FXML
    private Label lblOriginalValue;
    @FXML
    private Label lblNewValue;
    @FXML
    private Label lblKeyNew;
    @FXML
    private Label lblCarry;
    @FXML
    private Rectangle shiftRect;
    @FXML
    private Button btnReplay;
    @FXML
    private Button btnClose;

    ShiftPopOverPane(PopOver parentStage, int id2) {
        this.id = id2;
        this.parentStage = parentStage;
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/ShiftPopOverPane.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.shiftLabel.setStyle("-fx-font-family: Consolas");
                this.lblOriginalValue.setStyle("-fx-font-family: Consolas");
                this.lblNewValue.setStyle("-fx-font-family: Consolas");
                this.lblCarry.setStyle("-fx-font-family: Consolas");
            }
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void btnReplayFired(ActionEvent e) {
        this.uiController.getTimeline().playFromStart();
    }

    @FXML
    private void btnCloseFired(ActionEvent e) {
        this.parentStage.hide();
    }

    public void setLabel(String s) {
        this.shiftLabel.setText(s);
    }

    public Label getLabel() {
        return this.shiftLabel;
    }

    public Label getCarry() {
        return this.lblCarry;
    }

    public void setKeyNew(String s) {
        this.lblKeyNew.setText(s);
    }

    public Label getKeyNew() {
        return this.lblKeyNew;
    }

    public void setOriginalValue(String v, Bases base) {
        String strValue = "";
        switch (base) {
            case BIN: {
                strValue = "0b" + Long.toBinaryString(Long.parseLong(v, 2));
                break;
            }
            case DEC: {
                strValue = Long.toString(Long.parseLong(v, 2));
                break;
            }
            case HEX: {
                strValue = "0x" + Long.toHexString(Long.parseLong(v, 2)).toUpperCase();
            }
        }
        this.lblOriginalValue.setText(strValue);
    }

    public void setNewValue(String v, Bases base) {
        String strValue = "";
        switch (base) {
            case BIN: {
                strValue = "0b" + Long.toBinaryString(Long.parseLong(v, 2));
                break;
            }
            case DEC: {
                strValue = Long.toString(Long.parseLong(v, 2));
                break;
            }
            case HEX: {
                strValue = "0x" + Long.toHexString(Long.parseLong(v, 2)).toUpperCase();
            }
        }
        this.lblNewValue.setText(strValue);
    }

    public Rectangle getRect() {
        return this.shiftRect;
    }

    public void setSummary(String s) {
        this.lblSummary.setText(s);
    }
}

