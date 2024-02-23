/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.event.ActionEvent
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Alert
 *  javafx.scene.control.Alert$AlertType
 *  javafx.scene.control.Button
 *  javafx.scene.control.RadioButton
 *  javafx.scene.control.TextField
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.VBox
 *  javafx.stage.DirectoryChooser
 *  javafx.stage.Stage
 */
package visual;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import visual.VisUAL;

public class InitialSetupPane
extends VBox {
    private Stage stage;
    @FXML
    private Button btnContinue;
    @FXML
    private RadioButton rdoDark;
    @FXML
    private RadioButton rdoLight;
    @FXML
    private VBox vboxDark;
    @FXML
    private VBox vboxLight;
    @FXML
    private TextField txtDefaultDirectory;
    @FXML
    private Button btnBrowse;

    InitialSetupPane(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/InitialSetupPane.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        this.stage = stage;
        try {
            fxmlLoader.load();
            this.txtDefaultDirectory.setText(VisUAL.getSettingsManager().getDefaultDirectory());
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void txtDefaultDirectoryChanged(ActionEvent event) {
        File file = new File(this.txtDefaultDirectory.getText());
        if (file.isDirectory()) {
            VisUAL.getSettingsManager().setDefaultDirectory(this.txtDefaultDirectory.getText());
        } else {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Invalid Default Working Directory Path");
            dialog.getDialogPane().setContentText(this.txtDefaultDirectory.getText() + " is not a valid directory. Please specify a valid directory. Reverting to original path.");
            dialog.getDialogPane().setHeaderText("Invalid Path Specified");
            dialog.show();
            this.txtDefaultDirectory.setText(VisUAL.getSettingsManager().getDefaultDirectory());
        }
    }

    @FXML
    private void btnBrowseFired(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Default Working Directory");
        directoryChooser.setInitialDirectory(new File(VisUAL.getSettingsManager().getDefaultDirectory()));
        File file = directoryChooser.showDialog(this.btnBrowse.getScene().getWindow());
        if (file != null) {
            this.txtDefaultDirectory.setText(file.getAbsolutePath());
            VisUAL.getSettingsManager().setDefaultDirectory(file.getAbsolutePath());
        }
    }

    @FXML
    private void btnContinueFired(ActionEvent event) {
        VisUAL.getSettingsManager().setFirstRun(false);
        this.stage.close();
    }

    @FXML
    private void rdoDarkFired(ActionEvent event) {
        this.rdoDark.setSelected(true);
        this.rdoLight.setSelected(false);
        this.vboxDark.setId("initpane-selected");
        this.vboxLight.setId("initpane-unselected");
        VisUAL.getSettingsManager().setEditorTheme("Dark");
        event.consume();
    }

    @FXML
    private void rdoLightFired(ActionEvent event) {
        this.rdoDark.setSelected(false);
        this.rdoLight.setSelected(true);
        this.vboxDark.setId("initpane-unselected");
        this.vboxLight.setId("initpane-selected");
        VisUAL.getSettingsManager().setEditorTheme("Light");
        event.consume();
    }

    @FXML
    private void imgDarkFired(MouseEvent event) {
        this.rdoDarkFired(new ActionEvent());
        event.consume();
    }

    @FXML
    private void imgLightFired(MouseEvent event) {
        this.rdoLightFired(new ActionEvent());
        event.consume();
    }
}

