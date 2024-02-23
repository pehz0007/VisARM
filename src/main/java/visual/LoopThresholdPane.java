/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.application.Platform
 *  javafx.event.ActionEvent
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Button
 *  javafx.scene.control.CheckBox
 *  javafx.scene.control.Label
 *  javafx.scene.control.TextInputDialog
 *  javafx.scene.layout.VBox
 *  javafx.stage.Stage
 */
package visual;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import visual.Instruction;
import visual.UIController;
import visual.VisUAL;

public class LoopThresholdPane
extends VBox {
    private static UIController uiController = VisUAL.getUIController();
    @FXML
    private Label lblDestCode;
    @FXML
    private Label lblSourceCode;
    @FXML
    private Label lblDetails;
    @FXML
    private Button btnAbort;
    @FXML
    private Button btnIgnore;
    @FXML
    private Button btnThresholdChanged;
    @FXML
    private Button btnGoToDest;
    @FXML
    private Button btnGoToSource;
    @FXML
    private CheckBox checkDoNotWarn;
    private Stage stage;
    private Instruction inst;
    private int branchDestLine;
    private int branchSourceLine;

    LoopThresholdPane(Stage stage, Instruction inst, int branchDestLine, int branchSourceLine) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/LoopThresholdPane.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.lblDestCode.setStyle("-fx-font-family: Consolas");
                this.lblSourceCode.setStyle("-fx-font-family: Consolas");
            }
            this.stage = stage;
            this.inst = inst;
            this.branchDestLine = branchDestLine;
            this.branchSourceLine = branchSourceLine;
            this.lblDestCode.setText("Line " + (branchDestLine + 1) + ": " + VisUAL.getStringForLine(branchDestLine));
            this.lblSourceCode.setText("Line " + (branchSourceLine + 1) + ": " + VisUAL.getStringForLine(branchSourceLine));
            this.lblDetails.setText("The instruction at line " + (branchDestLine + 1) + " has been branched to " + inst.getBranchToCount(true) + " times. This may indicate unwanted behaviour, such as an infinite loop.");
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void btnGoToDestFired(ActionEvent event) {
        VisUAL.scrollToLine(this.branchDestLine);
    }

    @FXML
    private void btnGoToSourceFired(ActionEvent event) {
        VisUAL.scrollToLine(this.branchSourceLine);
    }

    @FXML
    private void btnIgnoreFired(ActionEvent event) {
        this.stage.close();
        this.inst.resetBranchToCount();
        uiController.executeAllFired(event);
    }

    @FXML
    private void btnChangeThresholdFired(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("" + VisUAL.getSettingsManager().getLoopThreshold());
        dialog.setTitle("");
        dialog.setHeaderText("Change warning threshold...");
        dialog.setContentText("Enter a threshold value > 0:");
        Optional<String> response = dialog.showAndWait();
        response.ifPresent(result -> {
            if (!result.matches("\\d+") || result.matches("0")) {
                this.btnChangeThresholdFired(event);
                return;
            }
            int threshold = Integer.parseInt(result);
            VisUAL.getSettingsManager().setLoopThreshold(threshold);
        });
    }

    @FXML
    private void btnAbortFired(ActionEvent event) {
        this.stage.close();
        Platform.runLater(() -> uiController.resetAllFired(event));
    }

    @FXML
    private void checkDoNotWarnFired(ActionEvent event) {
        this.inst.setIgnoreThreshold(this.checkDoNotWarn.isSelected());
    }
}

