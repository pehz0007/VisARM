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
 */
package visual;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import visual.BranchInfo;
import visual.VisUAL;

public class BranchPopOverPane
extends VBox {
    @FXML
    private Label lblBranchCondition;
    @FXML
    private Label lblSourceAddr;
    @FXML
    private Label lblDestAddr;
    @FXML
    private Label lblDestLineNumber;
    @FXML
    private Button btnShowDest;
    @FXML
    private Button btnClose;
    private boolean changeFont = false;
    private PopOver parentStage;
    private int destLineNumber;

    BranchPopOverPane(PopOver parentStage, BranchInfo info) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/BranchPopOverPane.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        this.parentStage = parentStage;
        try {
            String branchCondition;
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.changeFont = true;
                this.lblBranchCondition.setStyle("-fx-font-family: Consolas");
                this.lblSourceAddr.setStyle("-fx-font-family: Consolas");
                this.lblDestAddr.setStyle("-fx-font-family: Consolas");
                this.lblDestLineNumber.setStyle("-fx-font-family: Consolas");
            }
            if ((branchCondition = info.getBranchCondition()).isEmpty()) {
                this.lblBranchCondition.setText("N/A");
            } else {
                this.lblBranchCondition.setText(branchCondition);
            }
            this.lblSourceAddr.setText("0x" + Integer.toHexString(info.getSourceAddress()).toUpperCase());
            this.lblDestAddr.setText("0x" + Integer.toHexString(info.getDestAddress()).toUpperCase());
            this.destLineNumber = info.getDestLineNumber();
            this.lblDestLineNumber.setText("" + (this.destLineNumber + 1));
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void btnCloseFired(ActionEvent event) {
        this.parentStage.hide();
    }

    @FXML
    private void btnShowDestFired(ActionEvent event) {
        VisUAL.scrollToLine(this.destLineNumber);
    }
}

