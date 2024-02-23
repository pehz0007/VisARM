/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.animation.KeyFrame
 *  javafx.animation.KeyValue
 *  javafx.animation.Timeline
 *  javafx.beans.value.WritableValue
 *  javafx.event.ActionEvent
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.control.Alert
 *  javafx.scene.control.Alert$AlertType
 *  javafx.scene.control.Button
 *  javafx.scene.control.ButtonType
 *  javafx.scene.control.CheckBox
 *  javafx.scene.control.ChoiceBox
 *  javafx.scene.control.Hyperlink
 *  javafx.scene.control.Label
 *  javafx.scene.control.RadioButton
 *  javafx.scene.control.TextField
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.layout.AnchorPane
 *  javafx.stage.DirectoryChooser
 *  javafx.stage.FileChooser
 *  javafx.stage.Stage
 *  javafx.util.Duration
 */
package visual;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.WritableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SettingsPanel
extends AnchorPane {
    @FXML
    private CheckBox checkPointerInfo;
    @FXML
    private CheckBox checkShiftOps;
    @FXML
    private CheckBox checkMemoryAccess;
    @FXML
    private CheckBox checkBranchVis;
    @FXML
    private ChoiceBox choiceRegFormat;
    @FXML
    private ChoiceBox choiceTheme;
    @FXML
    private TextField txtDefaultDirectory;
    @FXML
    private Button btnBrowseDirectory;
    @FXML
    private CheckBox checkSkipFexec;
    @FXML
    private TextField txtFontSize;
    @FXML
    private Button btnRestoreFont;
    @FXML
    private Label lblPointerShortcut;
    @FXML
    private Label lblShiftShortcut;
    @FXML
    private Label lblBranchShortcut;
    @FXML
    private Label lblMemoryShortcut;
    @FXML
    private Button btnResetAll;
    @FXML
    private Label lblDefaultDirectory;
    @FXML
    private Label lblRegFormat;
    @FXML
    private Label lblFontSize;
    @FXML
    private Label lblTheme;
    @FXML
    private Label lblLoopThreshold;
    @FXML
    private Label lblMemInitValue;
    @FXML
    private TextField txtMemInitValue;
    @FXML
    private TextField txtLoopThreshold;
    @FXML
    private Label lblChangesSaved;
    @FXML
    private CheckBox checkEnableLogging;
    @FXML
    private Hyperlink hlinkLoggingGuide;
    @FXML
    private Label lblMode;
    @FXML
    private RadioButton rdioAll;
    @FXML
    private RadioButton rdioCompletion;
    @FXML
    private RadioButton rdioBreakpoints;
    @FXML
    private Label lblLogFile;
    @FXML
    private TextField txtLogFile;
    @FXML
    private Button btnLogFile;
    @FXML
    private Label lblItemsToLog;
    @FXML
    private CheckBox checkRegValues;
    @FXML
    private CheckBox checkStatusBits;
    @FXML
    private CheckBox checkSyntaxErrors;
    @FXML
    private CheckBox checkRuntimeErrors;
    @FXML
    private CheckBox checkMemChanged;
    @FXML
    private CheckBox checkMemCustom;
    @FXML
    private CheckBox checkCycleCount;
    @FXML
    private Button btnMemCustom;
    @FXML
    private Label lblInstMemSize;
    @FXML
    private TextField txtInstMemSize;
    @FXML
    private CheckBox checkAutoInstSize;
    @FXML
    private Label lblMaxProgSize;
    @FXML
    private CheckBox checkStackVis;
    @FXML
    private Label lblStackShortcut;
    @FXML
    private Label lblStackPointerValue;
    @FXML
    private TextField txtStackPointerValue;
    @FXML
    private CheckBox checkAutoIndentCode;
    @FXML
    private CheckBox checkMemAccessMode;
    @FXML
    private CheckBox checkDefaultCycleModel;
    @FXML
    private TextField txtCycleModel;
    @FXML
    private Label lblCycleModel;
    @FXML
    private Button btnBrowseCycleModel;
    private static boolean exists = false;
    private Timeline timeline = new Timeline();

    SettingsPanel() {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/SettingsPanel.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        try {
            fxmlLoader.load();
            this.lblChangesSaved.opacityProperty().set(0.0);
            SettingsManager settingsManager = VisUAL.getSettingsManager();
            if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                String shortcutPrefix = "\u2318+Alt+";
                this.lblPointerShortcut.setText(shortcutPrefix + "P");
                this.lblShiftShortcut.setText(shortcutPrefix + "S");
                this.lblMemoryShortcut.setText(shortcutPrefix + "M");
                this.lblStackShortcut.setText(shortcutPrefix + "K");
                this.lblBranchShortcut.setText(shortcutPrefix + "B");
            }
            this.checkPointerInfo.setSelected(settingsManager.getPointerInfo());
            if (!settingsManager.getPointerInfo()) {
                this.checkPointerInfo.setId("prefs-changed");
            }
            this.checkShiftOps.setSelected(settingsManager.getShiftOps());
            if (!settingsManager.getShiftOps()) {
                this.checkShiftOps.setId("prefs-changed");
            }
            this.checkMemoryAccess.setSelected(settingsManager.getMemoryAccess());
            if (!settingsManager.getMemoryAccess()) {
                this.checkMemoryAccess.setId("prefs-changed");
            }
            this.checkStackVis.setSelected(settingsManager.getStackVis());
            if (!settingsManager.getStackVis()) {
                this.checkStackVis.setId("prefs-changed");
            }
            this.checkBranchVis.setSelected(settingsManager.getBranchVis());
            if (!settingsManager.getBranchVis()) {
                this.checkBranchVis.setId("prefs-changed");
            }
            switch (settingsManager.getRegisterFormat()) {
                case DEC: {
                    this.choiceRegFormat.getSelectionModel().select((Object)"Decimal");
                    break;
                }
                case BIN: {
                    this.choiceRegFormat.getSelectionModel().select((Object)"Binary");
                    this.lblRegFormat.setId("prefs-changed");
                    break;
                }
                case HEX: {
                    this.choiceRegFormat.getSelectionModel().select((Object)"Hexadecimal");
                    this.lblRegFormat.setId("prefs-changed");
                }
            }
            switch (settingsManager.getThemeName()) {
                default: {
                    this.choiceTheme.getSelectionModel().select((Object)"Dark");
                    this.lblTheme.setId("prefs-normal");
                    break;
                }
                case "Light": {
                    this.choiceTheme.getSelectionModel().select((Object)"Light");
                    this.lblTheme.setId("prefs-changed");
                    break;
                }
                case "Solarized Dark": {
                    this.choiceTheme.getSelectionModel().select((Object)"Solarized Dark");
                    this.lblTheme.setId("prefs-changed");
                    break;
                }
                case "Solarized Light": {
                    this.choiceTheme.getSelectionModel().select((Object)"Solarized Light");
                    this.lblTheme.setId("prefs-changed");
                }
            }
            this.txtDefaultDirectory.setText(settingsManager.getDefaultDirectory());
            if (!settingsManager.getDefaultDirectory().equals(System.getProperty("user.home"))) {
                this.lblDefaultDirectory.setId("prefs-changed");
            }
            this.checkAutoIndentCode.setSelected(settingsManager.getAutoIndentCode());
            if (!settingsManager.getAutoIndentCode()) {
                this.checkAutoIndentCode.setId("prefs-changed");
            }
            this.checkSkipFexec.setSelected(settingsManager.getSkipFexec());
            if (settingsManager.getSkipFexec()) {
                this.checkSkipFexec.setId("prefs-changed");
            }
            this.txtFontSize.setText("" + settingsManager.getFontSize());
            if (settingsManager.getFontSize() != 14) {
                this.lblFontSize.setId("prefs-changed");
            }
            this.txtLoopThreshold.setText("" + settingsManager.getLoopThreshold());
            if (settingsManager.getLoopThreshold() != 1000) {
                this.lblLoopThreshold.setId("prefs-changed");
            }
            this.txtMemInitValue.setText("0x" + Integer.toHexString(settingsManager.getMemoryInitValue()).toUpperCase());
            if (settingsManager.getMemoryInitValue() != 0) {
                this.lblMemInitValue.setId("prefs-changed");
            }
            this.txtStackPointerValue.setText("0x" + Integer.toHexString(settingsManager.getStackPointerValue()).toUpperCase());
            if (settingsManager.getStackPointerValue() != -16777216) {
                this.lblStackPointerValue.setId("prefs-changed");
            }
            this.txtInstMemSize.setText("0x" + Integer.toHexString(settingsManager.getInstMemSize()).toUpperCase());
            this.checkAutoInstSize.setSelected(settingsManager.getAutoInstSize());
            if (settingsManager.getInstMemSize() != 65536 || !this.checkAutoInstSize.isSelected()) {
                this.lblInstMemSize.setId("prefs-changed");
            }
            this.txtInstMemSize.setDisable(this.checkAutoInstSize.isSelected());
            this.lblMaxProgSize.setDisable(this.checkAutoInstSize.isSelected());
            this.lblMaxProgSize.setText("" + Integer.divideUnsigned(settingsManager.getInstMemSize(), 4) + " lines");
            switch (settingsManager.getMemAccessMode()) {
                case OPEN: {
                    this.checkMemAccessMode.setSelected(false);
                    this.checkMemAccessMode.setId("prefs-normal");
                    break;
                }
                case STRICT: {
                    this.checkMemAccessMode.setSelected(true);
                    this.checkMemAccessMode.setId("prefs-changed");
                }
            }
            if (settingsManager.getCycleModel().isEmpty()) {
                this.lblCycleModel.setId("prefs-normal");
                this.checkDefaultCycleModel.setSelected(true);
                this.txtCycleModel.setDisable(true);
                this.btnBrowseCycleModel.setDisable(true);
            } else {
                this.lblCycleModel.setId("prefs-changed");
                this.checkDefaultCycleModel.setSelected(false);
                this.txtCycleModel.setDisable(false);
                this.txtCycleModel.setText(settingsManager.getCycleModel());
                this.btnBrowseCycleModel.setDisable(false);
            }
            this.checkEnableLogging.setSelected(settingsManager.getLoggingEnabled());
            this.checkEnableLoggingFired(new ActionEvent());
            if (settingsManager.getLoggingEnabled()) {
                this.checkEnableLogging.setId("prefs-changed");
            }
            EmulatorLogFile.LogMode logMode = settingsManager.getLoggingMode();
            switch (logMode) {
                default: {
                    this.rdioAll.setSelected(true);
                    this.rdioCompletion.setSelected(false);
                    this.rdioBreakpoints.setSelected(false);
                    break;
                }
                case COMPLETION: {
                    this.rdioAll.setSelected(false);
                    this.rdioCompletion.setSelected(true);
                    this.rdioBreakpoints.setSelected(false);
                    this.lblMode.setId("prefs-changed");
                    break;
                }
                case BREAKPOINT: {
                    this.rdioAll.setSelected(false);
                    this.rdioCompletion.setSelected(false);
                    this.rdioBreakpoints.setSelected(true);
                    this.lblMode.setId("prefs-changed");
                }
            }
            this.txtLogFile.setText(settingsManager.getLogFileLocation());
            if (!settingsManager.getLogFileLocation().equals(VisUAL.getSettingsManager().getDefaultDirectory())) {
                this.lblLogFile.setId("prefs-changed");
            }
            this.checkRegValues.setSelected(settingsManager.getLogRegisters());
            if (!settingsManager.getLogRegisters()) {
                this.checkRegValues.setId("prefs-changed");
            }
            this.checkStatusBits.setSelected(settingsManager.getLogStatusBits());
            if (!settingsManager.getLogStatusBits()) {
                this.checkStatusBits.setId("prefs-changed");
            }
            this.checkSyntaxErrors.setSelected(settingsManager.getLogSyntaxErrors());
            if (!settingsManager.getLogSyntaxErrors()) {
                this.checkSyntaxErrors.setId("prefs-changed");
            }
            this.checkRuntimeErrors.setSelected(settingsManager.getLogRuntimeErrors());
            if (!settingsManager.getLogRuntimeErrors()) {
                this.checkRuntimeErrors.setId("prefs-changed");
            }
            this.checkMemChanged.setSelected(settingsManager.getLogMemChanged());
            if (!settingsManager.getLogMemChanged()) {
                this.checkMemChanged.setId("prefs-changed");
            }
            this.checkMemCustom.setSelected(settingsManager.getLogMemCustom());
            if (!settingsManager.getLogCycleCount()) {
                this.checkCycleCount.setId("prefs-changed");
            }
            this.checkCycleCount.setSelected(settingsManager.getLogCycleCount());
            this.btnMemCustom.setDisable(!settingsManager.getLogMemCustom() || !settingsManager.getLoggingEnabled());
            if (settingsManager.getLogMemCustom()) {
                this.checkMemCustom.setId("prefs-changed");
            }
            this.choiceRegFormat.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    switch (newValue.intValue()) {
                        default: {
                            settingsManager.setRegisterFormat(Bases.DEC);
                            this.lblRegFormat.setId("");
                            break;
                        }
                        case 1: {
                            settingsManager.setRegisterFormat(Bases.BIN);
                            this.lblRegFormat.setId("prefs-changed");
                            break;
                        }
                        case 2: {
                            settingsManager.setRegisterFormat(Bases.HEX);
                            this.lblRegFormat.setId("prefs-changed");
                        }
                    }
                    this.flashSavedLabel();
                }
            });
            this.choiceTheme.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                if (!oldValue.equals(newValue)) {
                    String themeName;
                    switch (newValue.intValue()) {
                        default: {
                            settingsManager.setEditorTheme("Dark");
                            themeName = "Dark";
                            this.lblTheme.setId("");
                            break;
                        }
                        case 1: {
                            settingsManager.setEditorTheme("Light");
                            themeName = "Light";
                            this.lblTheme.setId("prefs-changed");
                            break;
                        }
                        case 2: {
                            settingsManager.setEditorTheme("Solarized Dark");
                            themeName = "Solarized Dark";
                            this.lblTheme.setId("prefs-changed");
                            break;
                        }
                        case 3: {
                            settingsManager.setEditorTheme("Solarized Light");
                            themeName = "Solarized Light";
                            this.lblTheme.setId("prefs-changed");
                        }
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Code editor theme changed.");
                    alert.setHeaderText("The '" + themeName + "' theme is now in use.");
                    alert.setContentText("The application will now close. Please relaunch the application for the theme to be applied.");
                    alert.showAndWait();
                    System.exit(0);
                }
            });
            this.txtLoopThreshold.addEventFilter(KeyEvent.KEY_TYPED, event -> {
                String character = event.getCharacter();
                if (!character.matches("\\d")) {
                    event.consume();
                }
            });
            this.txtFontSize.addEventFilter(KeyEvent.KEY_TYPED, event -> {
                String character = event.getCharacter();
                if (!character.matches("\\d")) {
                    event.consume();
                }
            });
            this.txtMemInitValue.addEventFilter(KeyEvent.KEY_TYPED, event -> {
                String textAfter = this.txtMemInitValue.getText() + event.getCharacter();
                if (!textAfter.matches("((0x|0X)[A-Fa-f0-9]{1,8})|(0x|0X)|(0)")) {
                    event.consume();
                }
            });
            exists = true;
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void checkPointerInfoFired(ActionEvent event) {
        VisUAL.getSettingsManager().setPointerInfo(this.checkPointerInfo.isSelected());
        if (!this.checkPointerInfo.isSelected()) {
            this.checkPointerInfo.setId("prefs-changed");
        } else {
            this.checkPointerInfo.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void checkShiftOpsFired(ActionEvent event) {
        VisUAL.getSettingsManager().setShiftOps(this.checkShiftOps.isSelected());
        if (!this.checkShiftOps.isSelected()) {
            this.checkShiftOps.setId("prefs-changed");
        } else {
            this.checkShiftOps.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void checkMemoryAccessFired(ActionEvent event) {
        VisUAL.getSettingsManager().setMemoryAccess(this.checkMemoryAccess.isSelected());
        if (!this.checkMemoryAccess.isSelected()) {
            this.checkMemoryAccess.setId("prefs-changed");
        } else {
            this.checkMemoryAccess.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void btnBrowseDirectoryFired(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Default Working Directory");
        directoryChooser.setInitialDirectory(new File(VisUAL.getSettingsManager().getDefaultDirectory()));
        File file = directoryChooser.showDialog(this.btnBrowseDirectory.getScene().getWindow());
        if (file != null) {
            this.txtDefaultDirectory.setText(file.getAbsolutePath());
            VisUAL.getSettingsManager().setDefaultDirectory(file.getAbsolutePath());
        }
        if (!file.getAbsolutePath().equals(System.getProperty("user.home"))) {
            this.lblDefaultDirectory.setId("prefs-changed");
        } else {
            this.lblDefaultDirectory.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void txtDefaultDirectoryChanged(ActionEvent event) {
        File file = new File(this.txtDefaultDirectory.getText());
        if (file.isDirectory()) {
            VisUAL.getSettingsManager().setDefaultDirectory(this.txtDefaultDirectory.getText());
            if (!this.txtDefaultDirectory.getText().equals(System.getProperty("user.home"))) {
                this.lblDefaultDirectory.setId("prefs-changed");
            } else {
                this.lblDefaultDirectory.setId("");
            }
        } else {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Invalid Default Working Directory Path");
            dialog.getDialogPane().setContentText(this.txtDefaultDirectory.getText() + " is not a valid directory. Please specify a valid directory. Reverting to original path.");
            dialog.getDialogPane().setHeaderText("Invalid Path Specified");
            dialog.show();
            this.txtDefaultDirectory.setText(VisUAL.getSettingsManager().getDefaultDirectory());
            this.lblDefaultDirectory.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void checkAutoIndentCodeFired(ActionEvent event) {
        VisUAL.getSettingsManager().setAutoIndentCode(this.checkAutoIndentCode.isSelected());
        if (!this.checkAutoIndentCode.isSelected()) {
            this.checkAutoIndentCode.setId("prefs-changed");
        } else {
            this.checkAutoIndentCode.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void checkSkipFexecFired(ActionEvent event) {
        VisUAL.getSettingsManager().setSkipFexec(this.checkSkipFexec.isSelected());
        if (this.checkSkipFexec.isSelected()) {
            this.checkSkipFexec.setId("prefs-changed");
        } else {
            this.checkSkipFexec.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void txtFontSizeChanged(ActionEvent event) {
        if (this.txtFontSize.getText().matches("\\d+")) {
            int fontSize = Integer.parseInt(this.txtFontSize.getText());
            VisUAL.getSettingsManager().setFontSize(fontSize);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Code editor font size changed");
            alert.setHeaderText("Code editor font size set to " + fontSize + ".");
            alert.setContentText("The application will now close, please relaunch to continue.");
            alert.setOnCloseRequest(event1 -> System.exit(1));
            alert.showAndWait();
        } else {
            this.txtFontSize.setText("" + VisUAL.getSettingsManager().getFontSize());
        }
        this.flashSavedLabel();
    }

    @FXML
    private void txtLoopThresholdChanged(ActionEvent event) {
        if (this.txtLoopThreshold.getText().matches("\\d+")) {
            VisUAL.getSettingsManager().setLoopThreshold(Integer.parseInt(this.txtLoopThreshold.getText()));
        } else {
            this.txtLoopThreshold.setText("" + VisUAL.getSettingsManager().getLoopThreshold());
        }
        this.flashSavedLabel();
    }

    @FXML
    private void txtMemInitValueChanged(ActionEvent event) {
        if (this.txtMemInitValue.getText().matches("(0x|0X)[A-Fa-f0-9]{1,8}")) {
            VisUAL.getSettingsManager().setMemoryInitValue((int)Long.parseLong(this.txtMemInitValue.getText().substring(2), 16));
        } else {
            this.txtMemInitValue.setText("0x" + Integer.toHexString(VisUAL.getSettingsManager().getMemoryInitValue()).toUpperCase());
        }
        this.flashSavedLabel();
    }

    @FXML
    private void btnRestoreFontFired(ActionEvent event) {
        int fontSize = 14;
        this.txtFontSize.setText("" + fontSize);
        VisUAL.getSettingsManager().setFontSize(fontSize);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Code editor font size set to " + fontSize + ".");
        alert.setContentText("The application will now close, please relaunch to continue.");
        alert.setOnCloseRequest(event1 -> System.exit(1));
        alert.showAndWait();
    }

    @FXML
    private void checkEnableLoggingFired(ActionEvent event) {
        this.lblMode.setDisable(!this.checkEnableLogging.isSelected());
        this.rdioAll.setDisable(!this.checkEnableLogging.isSelected());
        this.rdioCompletion.setDisable(!this.checkEnableLogging.isSelected());
        this.rdioBreakpoints.setDisable(!this.checkEnableLogging.isSelected());
        this.lblLogFile.setDisable(!this.checkEnableLogging.isSelected());
        this.txtLogFile.setDisable(!this.checkEnableLogging.isSelected());
        this.btnLogFile.setDisable(!this.checkEnableLogging.isSelected());
        this.lblItemsToLog.setDisable(!this.checkEnableLogging.isSelected());
        this.checkRegValues.setDisable(!this.checkEnableLogging.isSelected());
        this.checkStatusBits.setDisable(!this.checkEnableLogging.isSelected());
        this.checkSyntaxErrors.setDisable(!this.checkEnableLogging.isSelected());
        this.checkRuntimeErrors.setDisable(!this.checkEnableLogging.isSelected());
        this.checkMemChanged.setDisable(!this.checkEnableLogging.isSelected());
        this.checkMemCustom.setDisable(!this.checkEnableLogging.isSelected());
        this.checkCycleCount.setDisable(!this.checkEnableLogging.isSelected());
        this.btnMemCustom.setDisable(!this.checkEnableLogging.isSelected());
        if (this.checkEnableLogging.isSelected()) {
            this.btnMemCustom.setDisable(!this.checkMemCustom.isSelected());
            this.checkEnableLogging.setId("prefs-changed");
        } else {
            this.checkEnableLogging.setId("");
        }
        VisUAL.getSettingsManager().setLoggingEnabled(this.checkEnableLogging.isSelected());
        this.flashSavedLabel();
    }

    @FXML
    private void btnLogFileFired(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Log File Location");
        directoryChooser.setInitialDirectory(new File(VisUAL.getSettingsManager().getLogFileLocation()));
        File file = directoryChooser.showDialog(this.btnLogFile.getScene().getWindow());
        if (file != null) {
            this.txtLogFile.setText(file.getAbsolutePath());
            VisUAL.getSettingsManager().setLogFileLocation(file.getAbsolutePath());
        }
        if (!file.getAbsolutePath().equals(VisUAL.getSettingsManager().getDefaultDirectory())) {
            this.lblLogFile.setId("prefs-changed");
        } else {
            this.lblLogFile.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void txtLogFileChanged(ActionEvent event) {
        File file = new File(this.txtLogFile.getText());
        if (file.isDirectory()) {
            VisUAL.getSettingsManager().setLogFileLocation(this.txtLogFile.getText());
            if (!this.txtLogFile.getText().equals(VisUAL.getSettingsManager().getDefaultDirectory())) {
                this.lblLogFile.setId("prefs-changed");
            } else {
                this.lblLogFile.setId("");
            }
        } else {
            Alert dialog = new Alert(Alert.AlertType.ERROR);
            dialog.setTitle("Invalid Log File Location");
            dialog.getDialogPane().setContentText(this.txtLogFile.getText() + " is not a valid directory. Please specify a valid directory. Reverting to original path.");
            dialog.getDialogPane().setHeaderText("Invalid Path Specified");
            dialog.show();
            this.txtLogFile.setText(VisUAL.getSettingsManager().getLogFileLocation());
            this.txtLogFile.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void hlinkLoggingGuideFired(ActionEvent event) {
        VisUAL.openWebpageInBrowser("http://salmanarif.bitbucket.org/visual/logging_guide.html");
    }

    @FXML
    private void rdioAllFired(ActionEvent event) {
        this.rdioAll.setSelected(true);
        this.rdioCompletion.setSelected(false);
        this.rdioBreakpoints.setSelected(false);
        VisUAL.getSettingsManager().setLoggingMode("All");
        this.lblMode.setId("");
        this.flashSavedLabel();
    }

    @FXML
    private void rdioCompletionFired(ActionEvent event) {
        this.rdioAll.setSelected(false);
        this.rdioCompletion.setSelected(true);
        this.rdioBreakpoints.setSelected(false);
        VisUAL.getSettingsManager().setLoggingMode("Completion");
        this.lblMode.setId("prefs-changed");
        this.flashSavedLabel();
    }

    @FXML
    private void rdioBreakpointsFired(ActionEvent event) {
        this.rdioAll.setSelected(false);
        this.rdioCompletion.setSelected(false);
        this.rdioBreakpoints.setSelected(true);
        VisUAL.getSettingsManager().setLoggingMode("Breakpoint");
        this.lblMode.setId("prefs-changed");
        this.flashSavedLabel();
    }

    @FXML
    private void checkRegistersFired(ActionEvent event) {
        VisUAL.getSettingsManager().setLogRegisters(this.checkRegValues.isSelected());
        if (!this.checkRegValues.isSelected()) {
            this.checkRegValues.setId("prefs-changed");
        } else {
            this.checkRegValues.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void checkStatusBitsFired(ActionEvent event) {
        VisUAL.getSettingsManager().setLogStatusBits(this.checkStatusBits.isSelected());
        if (!this.checkStatusBits.isSelected()) {
            this.checkStatusBits.setId("prefs-changed");
        } else {
            this.checkStatusBits.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void checkSyntaxErrorsFired(ActionEvent event) {
        VisUAL.getSettingsManager().setLogSyntaxErrors(this.checkSyntaxErrors.isSelected());
        if (!this.checkSyntaxErrors.isSelected()) {
            this.checkSyntaxErrors.setId("prefs-changed");
        } else {
            this.checkSyntaxErrors.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void checkRuntimeErrorsFired(ActionEvent event) {
        VisUAL.getSettingsManager().setLogRuntimeErrors(this.checkRuntimeErrors.isSelected());
        if (!this.checkRuntimeErrors.isSelected()) {
            this.checkRuntimeErrors.setId("prefs-changed");
        } else {
            this.checkRuntimeErrors.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void checkMemChangedFired(ActionEvent event) {
        VisUAL.getSettingsManager().setLogMemChanged(this.checkMemChanged.isSelected());
        if (!this.checkMemChanged.isSelected()) {
            this.checkMemChanged.setId("prefs-changed");
        } else {
            this.checkMemChanged.setId("");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void checkMemCustomFired(ActionEvent event) {
        VisUAL.getSettingsManager().setLogMemCustom(this.checkMemCustom.isSelected());
        if (this.checkMemCustom.isSelected()) {
            this.checkMemCustom.setId("prefs-changed");
        } else {
            this.checkMemCustom.setId("");
        }
        this.btnMemCustom.setDisable(!this.checkMemCustom.isSelected());
        this.flashSavedLabel();
    }

    @FXML
    private void checkCycleCountFired(ActionEvent event) {
        VisUAL.getSettingsManager().setLogCycleCount(this.checkCycleCount.isSelected());
        if (this.checkCycleCount.isSelected()) {
            this.checkCycleCount.setId("");
        } else {
            this.checkCycleCount.setId("prefs-changed");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void btnMemCustomFired(ActionEvent event) {
        this.setDisable(true);
        Stage customMemStage = new Stage();
        MemLocSelPane memLocSelPane = new MemLocSelPane(customMemStage, VisUAL.getSettingsManager().getLogMemCustomLocations());
        customMemStage.setScene(new Scene((Parent)memLocSelPane));
        customMemStage.setTitle("Specify custom memory locations");
        customMemStage.setResizable(false);
        customMemStage.setAlwaysOnTop(true);
        customMemStage.showAndWait();
        List<String> locations = memLocSelPane.getNewLocations();
        if (!locations.isEmpty()) {
            String[] locArray = new String[locations.size()];
            for (int i = 0; i < locArray.length; ++i) {
                locArray[i] = locations.get(i);
            }
            VisUAL.getSettingsManager().setLogMemCustomLocations(locArray);
        } else {
            String[] empty = new String[]{""};
            VisUAL.getSettingsManager().setLogMemCustomLocations(empty);
        }
        this.setDisable(false);
    }

    @FXML
    private void txtInstMemSizeChanged(ActionEvent event) {
        if (!this.txtInstMemSize.getText().matches("(?i:0x([A-F0-9]{0,7})(0|4|8|C))")) {
            this.txtInstMemSize.setText("0x" + Integer.toHexString(VisUAL.getSettingsManager().getInstMemSize()).toUpperCase());
            return;
        }
        int value = (int)Long.parseLong(this.txtInstMemSize.getText().substring(2), 16);
        if (Integer.compareUnsigned(value, 255) <= 0) {
            this.txtInstMemSize.setText("0x" + Integer.toHexString(VisUAL.getSettingsManager().getInstMemSize()).toUpperCase());
            return;
        }
        VisUAL.getSettingsManager().setInstMemSize(value);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Instruction memory size now set to " + this.txtInstMemSize.getText() + ".");
        alert.setContentText("The application will now close, please relaunch to continue.");
        alert.setOnCloseRequest(event1 -> System.exit(1));
        alert.showAndWait();
    }

    @FXML
    private void checkAutoInstSizeFired(ActionEvent event) {
        this.txtInstMemSize.setDisable(this.checkAutoInstSize.isSelected());
        this.lblMaxProgSize.setDisable(this.checkAutoInstSize.isSelected());
        if (this.checkAutoInstSize.isSelected()) {
            this.lblInstMemSize.setId("prefs-normal");
        } else {
            this.lblInstMemSize.setId("prefs-changed");
        }
        VisUAL.getSettingsManager().setAutoInstSize(this.checkAutoInstSize.isSelected());
        this.flashSavedLabel();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Instruction memory size specification changed.");
        alert.setContentText("The application will now close, please relaunch to continue.");
        alert.setOnCloseRequest(event1 -> System.exit(1));
        alert.showAndWait();
    }

    @FXML
    private void checkDefaultCycleModelFired(ActionEvent event) {
        this.btnBrowseCycleModel.setDisable(this.checkDefaultCycleModel.isSelected());
        this.txtCycleModel.setDisable(this.checkDefaultCycleModel.isSelected());
        if (this.checkDefaultCycleModel.isSelected()) {
            this.lblCycleModel.setId("prefs-normal");
            VisUAL.getSettingsManager().setCycleModel("");
            this.flashSavedLabel();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("The cycle model specification has been changed.");
            alert.setContentText("The application will now close, please relaunch to continue.");
            alert.setOnCloseRequest(event1 -> System.exit(1));
            alert.showAndWait();
        } else {
            this.btnBrowseCycleModelFired(event);
        }
    }

    @FXML
    private void txtCycleModelChanged(ActionEvent event) {
        File file = new File(this.txtCycleModel.getText());
        if (!file.isFile()) {
            CycleCounter.showAlert("Unable to access custom cycle model", "The custom cycle model specified could not be opened.\nPlease specify a valid custom cycle model.", "Unable to access custom cycle model. Switching to default model...");
            this.txtCycleModel.setText("");
            this.txtCycleModel.requestFocus();
        } else {
            VisUAL.getSettingsManager().setCycleModel(this.txtCycleModel.getText());
            this.flashSavedLabel();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("The cycle model specification has been changed.");
            alert.setContentText("The application will now close, please relaunch to continue.");
            alert.setOnCloseRequest(event1 -> System.exit(1));
            alert.showAndWait();
        }
    }

    @FXML
    private void btnBrowseCycleModelFired(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select custom cycle model");
        File initialDirectory = new File(VisUAL.getSettingsManager().getDefaultDirectory());
        fileChooser.setInitialDirectory(initialDirectory);
        try {
            File file = fileChooser.showOpenDialog(this.btnBrowseCycleModel.getScene().getWindow());
            if (!file.isFile()) {
                CycleCounter.showAlert("Unable to access custom cycle model", "The custom cycle model specified could not be opened.\nPlease specify a valid custom cycle model.", "Unable to access custom cycle model. Switching to default model...");
                this.btnBrowseCycleModelFired(event);
            } else {
                this.txtCycleModel.setText(file.getAbsolutePath());
                VisUAL.getSettingsManager().setCycleModel(file.getAbsolutePath());
                this.flashSavedLabel();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText("The cycle model specification has been changed.");
                alert.setContentText("The application will now close, please relaunch to continue.");
                alert.setOnCloseRequest(event1 -> System.exit(1));
                alert.showAndWait();
            }
        }
        catch (NullPointerException e) {
            this.txtCycleModel.requestFocus();
        }
        catch (Throwable e) {
            UIController.displayExceptionAndExit(e);
        }
    }

    @FXML
    private void checkMemAccessModeFired(ActionEvent event) {
        if (this.checkMemAccessMode.isSelected()) {
            this.checkMemAccessMode.setId("prefs-changed");
            VisUAL.getSettingsManager().setMemAccessMode(MemoryMode.STRICT);
        } else {
            this.checkMemAccessMode.setId("prefs-normal");
            VisUAL.getSettingsManager().setMemAccessMode(MemoryMode.OPEN);
        }
        this.flashSavedLabel();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Memory access restrictions have been changed.");
        alert.setContentText("The application will now close, please relaunch to continue.");
        alert.setOnCloseRequest(event1 -> System.exit(1));
        alert.showAndWait();
    }

    @FXML
    private void checkStackVisFired(ActionEvent event) {
        VisUAL.getSettingsManager().setStackVisualisation(this.checkStackVis.isSelected());
        if (!this.checkStackVis.isSelected()) {
            this.checkStackVis.setId("prefs-changed");
        } else {
            this.checkStackVis.setId("prefs-normal");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void checkBranchVisFired(ActionEvent event) {
        VisUAL.getSettingsManager().setBranchVisualisation(this.checkBranchVis.isSelected());
        if (!this.checkBranchVis.isSelected()) {
            this.checkBranchVis.setId("prefs-changed");
        } else {
            this.checkBranchVis.setId("prefs-normal");
        }
        this.flashSavedLabel();
    }

    @FXML
    private void txtStackPointerValueChanged(ActionEvent event) {
        if (!this.txtStackPointerValue.getText().matches("(?i:0x([A-F0-9]{0,7})(0|4|8|C))")) {
            this.txtStackPointerValue.setText("0x" + Integer.toHexString(VisUAL.getSettingsManager().getStackPointerValue()).toUpperCase());
            return;
        }
        int value = (int)Long.parseLong(this.txtStackPointerValue.getText().substring(2), 16);
        if (Integer.compareUnsigned(value, 65535) <= 0) {
            this.txtStackPointerValue.setText("0x" + Integer.toHexString(VisUAL.getSettingsManager().getStackPointerValue()).toUpperCase());
            return;
        }
        VisUAL.getSettingsManager().setStackPointerValue(value);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Stack pointer initial value now set to " + this.txtStackPointerValue.getText() + ".");
        alert.setContentText("The application will now close, please relaunch to continue.");
        alert.setOnCloseRequest(event1 -> System.exit(1));
        alert.showAndWait();
    }

    @FXML
    private void btnResetAllFired(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Reset All Settings to Default");
        alert.setContentText("Are you sure you want to reset all settings to their default values? If you continue, the application will close and you must relaunch it.");
        ButtonType btnYes = new ButtonType("Yes");
        ButtonType btnNo = new ButtonType("No");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(new ButtonType[]{btnYes, btnNo});
        Optional result = alert.showAndWait();
        if (result.get() == btnYes) {
            VisUAL.getSettingsManager().resetAll();
            System.exit(0);
        } else if (result.get() == btnNo) {
            alert.close();
        }
    }

    private void flashSavedLabel() {
        if (!exists) {
            return;
        }
        this.timeline.stop();
        this.timeline = new Timeline();
        KeyFrame appear = new KeyFrame(Duration.seconds((double)0.0), new KeyValue[]{new KeyValue((WritableValue)this.lblChangesSaved.opacityProperty(), (Object)1)});
        KeyFrame showing = new KeyFrame(Duration.seconds((double)1.0), new KeyValue[]{new KeyValue((WritableValue)this.lblChangesSaved.opacityProperty(), (Object)1)});
        KeyFrame hidden = new KeyFrame(Duration.seconds((double)2.0), new KeyValue[]{new KeyValue((WritableValue)this.lblChangesSaved.opacityProperty(), (Object)0)});
        this.timeline.getKeyFrames().addAll(new KeyFrame[]{appear, showing, hidden});
        this.timeline.play();
    }

    public static boolean exists() {
        return exists;
    }

    public static void setDoesntExist() {
        exists = false;
    }
}

