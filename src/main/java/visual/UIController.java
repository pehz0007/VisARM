/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.animation.Interpolator
 *  javafx.animation.KeyFrame
 *  javafx.animation.KeyValue
 *  javafx.animation.Timeline
 *  javafx.application.Platform
 *  javafx.beans.value.ObservableValue
 *  javafx.beans.value.WritableValue
 *  javafx.concurrent.Task
 *  javafx.event.ActionEvent
 *  javafx.event.Event
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.geometry.Insets
 *  javafx.geometry.Orientation
 *  javafx.geometry.Pos
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.control.Alert
 *  javafx.scene.control.Alert$AlertType
 *  javafx.scene.control.Button
 *  javafx.scene.control.ButtonType
 *  javafx.scene.control.Label
 *  javafx.scene.control.Menu
 *  javafx.scene.control.MenuBar
 *  javafx.scene.control.MenuButton
 *  javafx.scene.control.MenuItem
 *  javafx.scene.control.SplitPane
 *  javafx.scene.control.TextArea
 *  javafx.scene.control.Tooltip
 *  javafx.scene.image.Image
 *  javafx.scene.image.ImageView
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.GridPane
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.Priority
 *  javafx.scene.layout.StackPane
 *  javafx.scene.layout.VBox
 *  javafx.stage.FileChooser
 *  javafx.stage.Screen
 *  javafx.stage.Stage
 *  javafx.util.Duration
 */
package visual;

import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.text.BadLocationException;
import javax.swing.text.Segment;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.PopOver;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.GutterIconInfo;

public class UIController
extends VBox {
    @FXML
    private HBox hboxEditorPadding;
    @FXML
    private Label lblEditorStatus;
    @FXML
    private Button btnNewFile;
    @FXML
    private Button btnOpenFile;
    @FXML
    private Button btnSaveFile;
    @FXML
    private Button btnStepForwards;
    @FXML
    private Button btnStepBackwards;
    @FXML
    private Button btnExecuteAll;
    @FXML
    private Button btnResetAll;
    @FXML
    private Button btnSettings;
    @FXML
    private MenuButton btnTools;
    @FXML
    private Button btnChangeLayout;
    @FXML
    private StackPane codeStackPane;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Label lblStatus;
    @FXML
    private HBox hboxStatus;
    @FXML
    private HBox hboxUtilityButtons;
    @FXML
    private HBox hboxEmulationButtons;
    @FXML
    private HBox hboxToolbar;
    @FXML
    private VBox vboxToolbar;
    @FXML
    private Label lblLineCaption;
    @FXML
    private Label lblLineNumber;
    @FXML
    private Label lblIssuesCaption;
    @FXML
    private Label lblIssuesCount;
    @FXML
    private ImageView imgStatusIcon;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem menuSaveAs;
    private int currentLine = 0;
    private boolean preprocessed = false;
    private int branchToLine = -1;
    private boolean isBranching = false;
    private boolean resume = false;
    private boolean firstRestore = true;
    private boolean finished = false;
    private boolean paused = false;
    private boolean abort = false;
    private boolean deferSceneUpdates = false;
    private List<GutterIconInfo> bpInfoList = new ArrayList<GutterIconInfo>();
    private PopOver pointerPopOver;
    private PopOver memoryPopOver;
    private PopOver stackPopOver;
    private PopOver shiftPopOver;
    private PopOver branchPopOver;
    private PopOver errorListPopOver;
    private final Timeline timeline = new Timeline();
    private boolean shiftIsValid = false;
    private boolean pointerIsValid = false;
    private boolean memoryIsValid = false;
    private boolean stackIsValid = false;
    private boolean branchIsValid = false;
    private boolean deferAddPointerButton = false;
    private boolean deferAddMemoryButton = false;
    private boolean deferAddShiftButton = false;
    private boolean deferAddStackButton = false;
    private boolean deferAddBranchButton = false;
    private PointerInfo cachedPointerInfo = new PointerInfo(null);
    private MemoryInfo cachedMemoryInfo = new MemoryInfo(null);
    private ShiftInfo cachedShiftInfo = new ShiftInfo(null);
    private StackInfo cachedStackInfo = new StackInfo();
    private BranchInfo cachedBranchInfo = new BranchInfo(null);
    private Button ptrButton;
    private Button memButton;
    private Button shiftButton;
    private Button stackButton;
    private Button branchButton;
    private VisButtonBar visButtonBar = new VisButtonBar();
    private AdjustmentListener visButtonAdjuster;
    private List<Instruction> instructions = new ArrayList<Instruction>();
    private List<GutterIconInfo> errorIconInfo = new ArrayList<GutterIconInfo>();
    private List<SyntaxError> syntaxErrors = new ArrayList<SyntaxError>();
    private int lineHeight = 15;
    private String toProcess;
    private String[] lineArray;
    private List<EmulatorState> savedStates = new ArrayList<EmulatorState>();
    private List<Integer> linesToMark = new ArrayList<Integer>();
    private Stage settingsStage;
    private EmulatorLogFile logFile;
    private EmulatorLogFile.LogMode logMode;
    private Stage memoryWindowStage;
    private MemoryWindow memoryWindow = new MemoryWindow();
    private Stage symbolWindowStage;
    private SymbolWindow symbolWindow = new SymbolWindow();
    private Stage loopThresholdStage;
    private SourceFile openedFile = new SourceFile(new File(VisUAL.getSettingsManager().getDefaultDirectory(), "untitled.S"), true);
    private Task<Boolean> scanSyntax = new Task<Boolean>(){

        protected Boolean call() throws Exception {
            return null;
        }
    };
    private Task executeAll = new Task<Void>(){

        protected Void call() throws Exception {
            return null;
        }
    };

    UIController() {
        FXMLLoader fxmlLoader = new FXMLLoader(((this)).getClass().getResource("/VisUAL.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            Platform.runLater(() -> {
                memoryWindowStage = new Stage();
                symbolWindowStage = new Stage();
            });
            this.hboxEditorPadding.getChildren().remove(this.lblEditorStatus);
            Tooltip.install((Node)this.btnNewFile, (Tooltip)new Tooltip("Create a new file"));
            Tooltip.install((Node)this.btnOpenFile, (Tooltip)new Tooltip("Open existing file"));
            Tooltip.install((Node)this.btnSaveFile, (Tooltip)new Tooltip("Save changes to current file"));
            Tooltip.install((Node)this.btnSettings, (Tooltip)new Tooltip("View program settings"));
            Tooltip.install((Node)this.btnTools, (Tooltip)new Tooltip("View additional tools"));
            Tooltip.install((Node)this.btnChangeLayout, (Tooltip)new Tooltip("Toggle between landscape and portrait layouts"));
            Tooltip.install((Node)this.btnExecuteAll, (Tooltip)new Tooltip("Execute until next breakpoint / end of file"));
            Tooltip.install((Node)this.btnResetAll, (Tooltip)new Tooltip("Stop and reset emulator"));
            Tooltip.install((Node)this.btnStepBackwards, (Tooltip)new Tooltip("Step backwards by one instruction"));
            Tooltip.install((Node)this.btnStepForwards, (Tooltip)new Tooltip("Step forwards by one instruction"));
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setMacMenuBar() {
//        NSMenuBarAdapter menuBarAdapter = new NSMenuBarAdapter();
//        Menu appNameMenu = new Menu("VisUAL");
//        this.menuBar.getMenus().add(0, appNameMenu);
//        menuBarAdapter.setMenuBar(this.menuBar);
//        this.getChildren().remove(this.menuBar);
    }

    public void setEditorPaddingId(String id2) {
        this.hboxEditorPadding.setId(id2);
    }

    @FXML
    private void btnOpenFileFired(ActionEvent event) {
        if (!this.checkForUnsavedChanges()) {
            return;
        }
        this.resetAllFired(event);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open ARM Assembly File");
        File initialDirectory = new File(VisUAL.getSettingsManager().getDefaultDirectory());
        fileChooser.setInitialDirectory(initialDirectory);
        File file = fileChooser.showOpenDialog(this.btnOpenFile.getScene().getWindow());
        if (file != null) {
            this.openedFile = new SourceFile(file, false);
            this.showFile(this.openedFile);
            VisUAL.setStageTitle(file.getName());
        }
    }

    private boolean checkForUnsavedChanges() {
        if (this.openedFile.hasUnsavedChanges()) {
            Alert unsavedAlert = new Alert(Alert.AlertType.NONE);
            unsavedAlert.setTitle("Unsaved changes");
            unsavedAlert.setHeaderText("Save changes to existing file?");
            unsavedAlert.setContentText("Would you like to save the changes you have made to the currently opened file?");
            ButtonType btnCancel = new ButtonType("Cancel");
            ButtonType btnNo = new ButtonType("No");
            ButtonType btnYes = new ButtonType("Yes");
            unsavedAlert.getButtonTypes().addAll(new ButtonType[]{btnCancel, btnNo, btnYes});
            Optional response = unsavedAlert.showAndWait();
            if (response.get() == btnCancel) {
                return false;
            }
            if (response.get() == btnNo) {
                return true;
            }
            if (response.get() == btnYes) {
                this.saveFileKeyboard();
                return true;
            }
            return false;
        }
        return true;
    }

    @FXML
    private void btnNewFileFired(ActionEvent event) {
        this.resetAllFired(event);
        if (!this.checkForUnsavedChanges()) {
            return;
        }
        VisUAL.removeAllBreakpoints();
        this.bpInfoList.clear();
        this.openedFile = new SourceFile(new File(VisUAL.getSettingsManager().getDefaultDirectory(), "untitled.S"), true);
        VisUAL.setStageTitle(this.openedFile.getStageTitleForFile());
        VisUAL.getRSyntaxTextArea().setText("");
    }

    @FXML
    public boolean menuQuitFired(ActionEvent event) {
        if (!this.checkForUnsavedChanges()) {
            return false;
        }
        Alert quitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        quitAlert.setHeaderText("Confirm exiting application");
        quitAlert.setTitle("Confirm exiting application");
        quitAlert.setContentText("Are you sure you want to exit VisUAL?");
        Optional response = quitAlert.showAndWait();
        if (!((ButtonType)response.get()).equals(ButtonType.OK)) {
            return false;
        }
        System.exit(0);
        return true;
    }

    @FXML
    private void menuSupInstFired(ActionEvent event) {
        VisUAL.openWebpageInBrowser("http://salmanarif.bitbucket.org/visual/supported_instructions.html");
    }

    @FXML
    private void menuLogGuideFired(ActionEvent event) {
        VisUAL.openWebpageInBrowser("http://salmanarif.bitbucket.org/visual/logging_guide.html");
    }

    @FXML
    private void menuMemMapFired(ActionEvent event) {
        VisUAL.openWebpageInBrowser("http://salmanarif.bitbucket.org/visual/memory_map.html");
    }

    @FXML
    private void menuKeyShortFired(ActionEvent event) {
        if (System.getProperty("os.name").contains("Mac")) {
            VisUAL.openWebpageInBrowser("http://salmanarif.bitbucket.org/visual/keyboard_shortcuts_osx.html");
        } else {
            VisUAL.openWebpageInBrowser("http://salmanarif.bitbucket.org/visual/keyboard_shortcuts_win_linux.html");
        }
    }

    @FXML
    private void menuAboutFired(ActionEvent event) {
        Stage aboutStage = new Stage();
        aboutStage.setScene(new Scene((Parent)new AboutPane()));
        aboutStage.setResizable(false);
        aboutStage.setAlwaysOnTop(true);
        aboutStage.setTitle("About VisUAL");
        aboutStage.showAndWait();
    }

    @FXML
    private void statusBarClicked(MouseEvent event) {
        if (this.abort) {
            if (this.errorListPopOver == null || !this.errorListPopOver.isShowing()) {
                ErrorListPopOverPane errorListPopOverPane = new ErrorListPopOverPane(this.syntaxErrors);
                this.errorListPopOver = new PopOver((Node)errorListPopOverPane);
                this.errorListPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                this.errorListPopOver.setDetachable(false);
                this.errorListPopOver.show((Node)this.hboxStatus);
                this.errorListPopOver.setOnHiding(event1 -> errorListPopOverPane.hideDetailPopOver());
                event.consume();
            } else if (this.errorListPopOver.isShowing()) {
                this.errorListPopOver.hide();
            }
        }
    }

    @FXML
    private void btnAutoIndentFired(Event event) {
        if (this.abort || this.finished) {
            return;
        }
        this.lineArray = VisUAL.getRSyntaxTextArea().getText().split("\\r?\\n");
        String indentedCode = SyntaxScanner.autoIndentCode(this.lineArray);
        Platform.runLater(() -> {
            RSyntaxTextArea rsta = VisUAL.getRSyntaxTextArea();
            ArrayList<Integer> oldBreakpoints = new ArrayList<Integer>();
            VisUAL.pauseRepositionBreakpointsTimeline(true);
            for (GutterIconInfo bp : this.bpInfoList) {
                try {
                    oldBreakpoints.add(rsta.getLineOfOffset(bp.getMarkedOffset()));
                }
                catch (BadLocationException e) {
                    System.out.println("Breakpoint offset out of range!");
                }
            }
            VisUAL.removeAllBreakpoints();
            this.bpInfoList.clear();
            rsta.setText(indentedCode);
            for (Integer i : oldBreakpoints) {
                this.bpInfoList.add(VisUAL.addBreakpoint(i));
            }
            VisUAL.pauseRepositionBreakpointsTimeline(false);
        });
        this.lineArray = indentedCode.split("\\r?\\n");
        this.updateFileSavedStatus();
    }

    @FXML
    private void btnShowMemoryFired(Event event) {
        if (this.memoryWindowStage.getScene() == null) {
            this.memoryWindow.setMemoryBank(VisUAL.getEmulator().memory);
            this.memoryWindowStage = new Stage();
            this.memoryWindowStage.setTitle("View Memory Contents");
            this.memoryWindowStage.setScene(new Scene((Parent)this.memoryWindow));
        }
        this.memoryWindow.updateContents(VisUAL.getEmulator().memory, false);
        try {
            Emulator emulator = VisUAL.getEmulator();
            this.symbolWindow.updateContents(emulator.labels, emulator.symbols, emulator.dcdWords, false);
        }
        catch (MemoryBank.MemAccessException e) {
            this.handleException(e, this.currentLine);
        }
        this.memoryWindowStage.setResizable(false);
        this.memoryWindowStage.setAlwaysOnTop(true);
        this.memoryWindowStage.show();
        this.memoryWindowStage.requestFocus();
    }

    @FXML
    private void btnShowSymbolFired(Event event) {
        Emulator emulator = VisUAL.getEmulator();
        if (this.symbolWindowStage.getScene() == null) {
            this.symbolWindowStage = new Stage();
            this.symbolWindowStage.setTitle("View Symbols");
            this.symbolWindowStage.setScene(new Scene((Parent)this.symbolWindow));
        }
        try {
            this.symbolWindow.updateContents(emulator.labels, emulator.symbols, emulator.dcdWords, false);
        }
        catch (MemoryBank.MemAccessException e) {
            this.handleException(e, this.currentLine);
        }
        this.symbolWindowStage.setAlwaysOnTop(true);
        this.symbolWindowStage.show();
        this.symbolWindowStage.requestFocus();
    }

    public MemoryWindow getMemoryWindow() {
        return this.memoryWindow;
    }

    public SymbolWindow getSymbolWindow() {
        return this.symbolWindow;
    }

    public void hideErrorListPopOver() {
        if (this.errorListPopOver != null) {
            this.errorListPopOver.hide();
        }
    }

    public void updateFileSavedStatus() {
        this.openedFile.updateSavedStatus(VisUAL.getRSyntaxTextArea().getText());
        VisUAL.setStageTitle(this.openedFile.getStageTitleForFile());
    }

    @FXML
    private void btnSaveFileFired(ActionEvent event) {
        this.saveFileKeyboard();
    }

    @FXML
    private void btnSaveAsFired(ActionEvent event) {
        this.saveAsKeyboard();
    }

    public void saveFileKeyboard() {
        if (this.openedFile.isNeverSaved()) {
            this.saveAsKeyboard();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        File initialDirectory = new File(VisUAL.getSettingsManager().getDefaultDirectory());
        fileChooser.setInitialDirectory(initialDirectory);
        RSyntaxTextArea textArea = VisUAL.getRSyntaxTextArea();
        try {
            FileUtils.writeStringToFile(this.openedFile.getRawFile(), textArea.getText());
            this.openedFile.updateSavedStatus(textArea.getText());
            VisUAL.setStageTitle(this.openedFile.getStageTitleForFile());
        }
        catch (IOException e) {
            System.out.println("File not found");
            UIController.displayExceptionAndExit(e);
        }
        catch (NullPointerException e) {
            File file = fileChooser.showSaveDialog(this.btnSaveFile.getScene().getWindow());
            try {
                FileUtils.writeStringToFile(file, textArea.getText());
                this.openedFile = new SourceFile(file, false);
                VisUAL.setStageTitle(this.openedFile.getStageTitleForFile());
            }
            catch (IOException e1) {
                System.out.println("File not found");
                UIController.displayExceptionAndExit(e1);
            }
        }
    }

    public void saveAsKeyboard() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        File initialDirectory = new File(VisUAL.getSettingsManager().getDefaultDirectory());
        fileChooser.setInitialDirectory(initialDirectory);
        RSyntaxTextArea textArea = VisUAL.getRSyntaxTextArea();
        File file = fileChooser.showSaveDialog(this.btnSaveFile.getScene().getWindow());
        if (file == null) {
            return;
        }
        try {
            FileUtils.writeStringToFile(file, textArea.getText());
            this.openedFile = new SourceFile(file, false);
            VisUAL.setStageTitle(this.openedFile.getStageTitleForFile());
        }
        catch (IOException e) {
            System.out.println("File not found.");
            UIController.displayExceptionAndExit(e);
        }
        catch (NullPointerException e) {
            System.out.println("File not saved.");
            UIController.displayExceptionAndExit(e);
        }
    }

    @FXML
    private void btnSettingsFired(ActionEvent event) {
        if (SettingsPanel.exists()) {
            this.settingsStage.requestFocus();
            return;
        }
        SettingsPanel sPanel = new SettingsPanel();
        this.settingsStage = new Stage();
        this.settingsStage.setScene(new Scene((Parent)sPanel));
        this.settingsStage.setTitle("Settings");
        this.settingsStage.setResizable(false);
        this.settingsStage.show();
        this.settingsStage.setOnCloseRequest(wEvent -> SettingsPanel.setDoesntExist());
    }

    @FXML
    public void executeAllFired(ActionEvent event) {
        if (this.btnExecuteAll.isDisabled()) {
            return;
        }
        if (EmulatorState.getStatePointer() < EmulatorState.getStateCount() && !this.resume) {
            this.restoreEmulator(EmulatorState.getStateCount() - 1);
        }
        this.btnStepBackwards.setDisable(EmulatorState.getStatePointer() == 0 || EmulatorState.getStateCount() == 0);
        if (this.finished) {
            return;
        }
        if (!this.preprocessed) {
            try {
                this.preProcess();
            }
            catch (RuntimeError e) {
                this.handleException(e, this.currentLine);
                this.logFile.logRuntimeError(this.currentLine, e.getMessage());
                this.logFile.saveXmlToFile();
            }
            this.preprocessed = true;
            if (this.syntaxErrors.size() > 0) {
                this.abort = true;
            }
        }
        if (this.abort) {
            return;
        }
        this.paused = false;
        VisUAL.disableEditor(false);
        final CodeCanvas currentLineCanvas = VisUAL.getCurrentLineCanvas();
        final CodeCanvas branchDestCanvas = VisUAL.getBranchDestCanvas();
        final CodeCanvas branchArrowCanvas = VisUAL.getBranchArrowCanvas();
        final CodeCanvas linkRegisterCanvas = VisUAL.getLinkRegisterCanvas();
        branchDestCanvas.setHidden(true, true);
        branchArrowCanvas.setHidden(true, true);
        this.linesToMark = new ArrayList<Integer>();
        this.deleteOldButtons();
        this.deferSceneUpdates = true;
        final int lineCount = this.lineArray.length;
        final ExecStatus[] status = new ExecStatus[1];
        VisUAL.disableEditor(false);
        this.executeAll = new Task<Void>(){

            protected Void call() throws Exception {
                while (UIController.this.currentLine < lineCount || UIController.this.currentLine >= lineCount && UIController.this.isBranching) {
                    if (this.isCancelled()) {
                        return null;
                    }
                    if (UIController.this.currentLine > 0) {
                        if (UIController.this.breakpointExists(UIController.this.currentLine - 1) && !UIController.this.resume) {
                            UIController.this.resume = true;
                            System.out.println("EXECUTE BREAKPOINT REACHED!");
                            if (UIController.this.logMode == EmulatorLogFile.LogMode.BREAKPOINT) {
                                UIController.this.logFile.logState(UIController.this.currentLine - 1, UIController.this.getStringForLine(UIController.this.currentLine - 1), VisUAL.getEmulator());
                            }
                            return null;
                        }
                        if (UIController.this.resume) {
                            UIController.this.resume = false;
                        }
                    }
                    if (UIController.this.resume) {
                        return null;
                    }
                    if (UIController.this.isBranching) {
                        UIController.this.isBranching = false;
                        UIController.this.currentLine = UIController.this.branchToLine;
                    }
                    branchDestCanvas.setHidden(true, false);
                    branchArrowCanvas.setHidden(true, false);
                    if (UIController.this.currentLine == linkRegisterCanvas.getLineNumber()) {
                        VisUAL.removeLinkRegisterIcon();
                        linkRegisterCanvas.setHidden(true, false);
                    }
                    UIController.this.deferAddPointerButton = false;
                    UIController.this.deferAddMemoryButton = false;
                    UIController.this.deferAddShiftButton = false;
                    UIController.this.deferAddStackButton = false;
                    UIController.this.deferAddBranchButton = false;
                    UIController.this.cachedPointerInfo = new PointerInfo(null);
                    UIController.this.cachedMemoryInfo = new MemoryInfo(null);
                    UIController.this.cachedShiftInfo = new ShiftInfo(null);
                    UIController.this.cachedStackInfo = new StackInfo();
                    UIController.this.cachedBranchInfo = new BranchInfo(null);
                    VisUAL.getEmulator().resetInstrCycleCount();
                    Instruction nextInstruction = UIController.this.getNextAvailableInstruction(UIController.this.currentLine);
                    VisUAL.getEmulator().setProgramCounter(nextInstruction.getAddress(), true);
                    VisUAL.getRegAccordion().resetRegHighlight(true);
                    status[0] = UIController.this.processLineUsingEmulator(nextInstruction.getLineNumber());
                    VisUAL.getClockCycleBar().setCurrentCycles(VisUAL.getEmulator().getInstrCycleCount(), true);
                    currentLineCanvas.setHidden(false, false);
                    currentLineCanvas.setColour(UIController.this.calcHighlightColour(status[0]), false);
                    currentLineCanvas.setLineNumber(nextInstruction.getLineNumber(), true);
                    if (status[0] != ExecStatus.SKIP) {
                        UIController.this.savedStates.add(new EmulatorState(VisUAL.getEmulator()));
                        if (UIController.this.logMode == EmulatorLogFile.LogMode.ALL) {
                            UIController.this.logFile.logState(nextInstruction.getLineNumber(), UIController.this.getStringForLine(nextInstruction.getLineNumber()), VisUAL.getEmulator());
                        }
                    }
                    UIController.this.linesToMark.add(nextInstruction.getLineNumber());
                    if (UIController.this.abort) {
                        UIController.this.deferSceneUpdates = false;
                        return null;
                    }
                    if (UIController.this.paused) {
                        UIController.this.deferSceneUpdates = false;
                        return null;
                    }
                    if (UIController.this.finished) {
                        UIController.this.deferSceneUpdates = false;
                        if (UIController.this.logMode == EmulatorLogFile.LogMode.COMPLETION) {
                            UIController.this.logFile.logState(nextInstruction.getLineNumber(), UIController.this.getStringForLine(nextInstruction.getLineNumber()), VisUAL.getEmulator());
                        }
                        return null;
                    }
                    UIController.this.currentLine = nextInstruction.getLineNumber() + 1;
                }
                if (!UIController.this.resume) {
                    Platform.runLater(() -> {
                        UIController.this.setDispEmuStatus("alert", "Emulation Complete", true);
                        UIController.this.disableEmulation(true);
                    });
                    UIController.this.finished = true;
                }
                UIController.this.deferSceneUpdates = false;
                if (UIController.this.logMode == EmulatorLogFile.LogMode.COMPLETION) {
                    UIController.this.logFile.logState(UIController.this.currentLine - 1, UIController.this.getStringForLine(UIController.this.currentLine - 1), VisUAL.getEmulator());
                }
                return null;
            }
        };
        this.executeAll.setOnFailed(event1 -> {
            RuntimeError r;
            System.out.println("EXECUTE TASK FAILED!");
            boolean ignoreError = false;
            if (this.executeAll.getException() instanceof RuntimeError && (r = (RuntimeError)this.executeAll.getException()).getMessage().startsWith("There is no instruction to process on or after")) {
                ignoreError = true;
                this.finished = true;
                Platform.runLater(() -> {
                    this.setDispEmuStatus("alert", "Emulation Complete", true);
                    this.disableEmulation(true);
                });
                if (this.logMode == EmulatorLogFile.LogMode.COMPLETION) {
                    try {
                        this.logFile.logState(this.currentLine - 1, this.getStringForLine(this.currentLine - 1), VisUAL.getEmulator());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (!ignoreError) {
                this.logFile.logRuntimeError(this.currentLine, this.executeAll.getException().getMessage());
                this.handleException(this.executeAll.getException(), this.currentLine);
            }
            this.logFile.saveXmlToFile();
            this.updateUIAfterExecuteAll();
        });
        this.executeAll.setOnSucceeded(event1 -> {
            System.out.println("EXECUTE TASK COMPLETED!");
            this.logFile.saveXmlToFile();
            this.updateUIAfterExecuteAll();
            if (this.abort) {
                Platform.runLater(() -> this.setDispEmuStatus("error", "Runtime Error", true));
            } else if (this.finished) {
                Platform.runLater(() -> this.setDispEmuStatus("alert", "Emulation Complete", false));
            } else if (this.resume) {
                Platform.runLater(() -> this.setDispEmuStatus("alert", "Breakpoint Reached", false));
            }
        });
        this.executeAll.setOnScheduled(event1 -> {
            System.out.println("EXECUTE TASK STARTED!");
            Platform.runLater(() -> {
                if (!this.resume) {
                    this.setDispEmuStatus("on", "Emulation Running", false);
                }
            });
        });
        this.executeAll.setOnCancelled(event1 -> System.out.println("EXECUTE TASK CANCELLED!"));
        new Thread((Runnable)this.executeAll).start();
    }

    public void handleException(Throwable e, int lineNumber) {
        if (e instanceof MemoryBank.MemAccessException) {
            MemoryBank.MemAccessException m = (MemoryBank.MemAccessException)e;
            Platform.runLater(() -> VisUAL.addRuntimeError(m.getLineNumber(), m.getMessage(), m.getDetails()));
        } else if (e instanceof ImmediateFormatException) {
            ImmediateFormatException i = (ImmediateFormatException)e;
            Platform.runLater(() -> VisUAL.addRuntimeError(i.getLineNumber(), i.getMessage(), i.getDetails()));
        } else if (e instanceof RuntimeError) {
            RuntimeError r = (RuntimeError)e;
            Platform.runLater(() -> VisUAL.addRuntimeError(r.getLineNumber(), r.getMessage(), r.getDetails()));
        } else {
            UIController.displayExceptionAndExit(e);
        }
        Platform.runLater(() -> this.setDispEmuStatus("error", "Runtime Error", false));
    }

    public static void displayExceptionAndExit(Throwable e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unknown Exception");
        alert.setHeaderText("An unknown exception has occurred.\nThe application cannot continue and will now quit.\nPlease save the following stack trace and a screenshot of the application window to help resolve the issue.");
        alert.setContentText(e.getMessage());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();
        Label label = new Label("The exception stacktrace is:");
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow((Node)textArea, (Priority)Priority.ALWAYS);
        GridPane.setHgrow((Node)textArea, (Priority)Priority.ALWAYS);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add((Node)label, 0, 0);
        expContent.add((Node)textArea, 0, 1);
        alert.getDialogPane().setExpandableContent((Node)expContent);
        if (e instanceof Error) {
            alert.setOnCloseRequest(event1 -> System.exit(0));
        }
        alert.setResizable(false);
        alert.showAndWait();
        System.exit(1);
    }

    private void updateUIAfterExecuteAll() {
        boolean[] statusBits = VisUAL.getEmulator().getStatusBits();
        VisUAL.getRegAccordion().getStatusRegBar().setBitValueOnly(StatusBit.N, statusBits[0]);
        VisUAL.getRegAccordion().getStatusRegBar().setBitValueOnly(StatusBit.Z, statusBits[1]);
        VisUAL.getRegAccordion().getStatusRegBar().setBitValueOnly(StatusBit.C, statusBits[2]);
        VisUAL.getRegAccordion().getStatusRegBar().setBitValueOnly(StatusBit.V, statusBits[3]);
        Register[] registers = VisUAL.getEmulator().getRegisters();
        for (int i = 0; i < 16; ++i) {
            VisUAL.getRegAccordion().setRegValueOnly(registers[i].getType().getRegName(), registers[i].getValue());
        }
        VisUAL.getRegAccordion().applySavedHighlights();
        VisUAL.getRegAccordion().addDeferredEntries();
        VisUAL.getRegAccordion().getStatusRegBar().applySavedStyles();
        VisUAL.getClockCycleBar().applySavedCycles();
        if (VisUAL.getHasRuntimeError() && !this.paused) {
            this.disableEmulation(true);
        }
        this.deferSceneUpdates = false;
        if (this.deferAddPointerButton) {
            this.setPointerButton(this.cachedPointerInfo);
        }
        if (this.deferAddMemoryButton) {
            this.setMemoryButton(this.cachedMemoryInfo);
        }
        if (this.deferAddShiftButton) {
            this.setShiftButton(this.cachedShiftInfo);
        }
        if (this.deferAddStackButton) {
            this.setStackButton(this.cachedStackInfo);
        }
        if (this.deferAddBranchButton) {
            this.setBranchButton(this.cachedBranchInfo);
        }
        this.deferAddPointerButton = false;
        this.deferAddMemoryButton = false;
        this.deferAddShiftButton = false;
        this.deferAddStackButton = false;
        this.deferAddBranchButton = false;
        this.memoryWindow.updateContents(VisUAL.getEmulator().memory, this.deferSceneUpdates);
        try {
            Emulator emulator = VisUAL.getEmulator();
            this.symbolWindow.updateContents(emulator.labels, emulator.symbols, emulator.dcdWords, this.deferSceneUpdates);
        }
        catch (MemoryBank.MemAccessException e) {
            this.handleException(e, this.currentLine);
        }
        VisUAL.disableEditor(true);
        VisUAL.scrollToLine(this.currentLine - 1);
        VisUAL.getCurrentLineCanvas().draw();
        VisUAL.getBranchDestCanvas().draw();
        VisUAL.getBranchArrowCanvas().draw();
        VisUAL.getLinkRegisterCanvas().draw();
        int scrollPosition = VisUAL.getCodeScrollPane().getVerticalScrollBar().getValue();
        VisUAL.getCurrentLineCanvas().reposition(scrollPosition);
        VisUAL.getBranchDestCanvas().reposition(scrollPosition);
        VisUAL.getBranchArrowCanvas().reposition(scrollPosition);
        VisUAL.getLinkRegisterCanvas().reposition(scrollPosition);
        for (int i = 0; i < this.linesToMark.size(); ++i) {
            VisUAL.addCoverageMarker(new CoverageMarker(this.linesToMark.get(i)));
        }
        this.linesToMark = new ArrayList<Integer>();
        if (VisUAL.getSettingsManager().getAutoIndentCode()) {
            String indentedCode = SyntaxScanner.autoIndentCode(this.lineArray);
            RSyntaxTextArea rsta = VisUAL.getRSyntaxTextArea();
            ArrayList<Integer> oldBreakpoints = new ArrayList<Integer>();
            VisUAL.pauseRepositionBreakpointsTimeline(true);
            for (GutterIconInfo bp : this.bpInfoList) {
                try {
                    oldBreakpoints.add(rsta.getLineOfOffset(bp.getMarkedOffset()));
                }
                catch (BadLocationException e) {
                    System.out.println("Breakpoint offset out of range!");
                }
            }
            VisUAL.removeAllBreakpoints();
            this.bpInfoList.clear();
            rsta.setText(indentedCode);
            for (Integer i : oldBreakpoints) {
                this.bpInfoList.add(VisUAL.addBreakpoint(i));
            }
            VisUAL.pauseRepositionBreakpointsTimeline(false);
            this.lineArray = indentedCode.split("\\r?\\n");
            this.updateFileSavedStatus();
        }
    }

    @FXML
    public void stepForwardFired(ActionEvent event) {
        if (this.btnStepForwards.isDisabled()) {
            return;
        }
        VisUAL.goToNextEasterEggFrame();
        if (EmulatorState.getStatePointer() + 1 < EmulatorState.getStateCount() && !this.resume) {
            EmulatorState.setStatePointer(1);
            this.restoreEmulator(EmulatorState.getStatePointer());
            this.btnStepBackwards.setDisable(EmulatorState.getStatePointer() == 0 || EmulatorState.getStateCount() == 0);
            return;
        }
        if (this.finished) {
            return;
        }
        if (!this.preprocessed) {
            try {
                this.preProcess();
                try {
                    Emulator emulator = VisUAL.getEmulator();
                    this.symbolWindow.updateContents(emulator.labels, emulator.symbols, emulator.dcdWords, false);
                }
                catch (MemoryBank.MemAccessException e) {
                    this.handleException(e, this.currentLine);
                }
                if (VisUAL.getSettingsManager().getAutoIndentCode()) {
                    this.btnAutoIndentFired((Event)event);
                }
            }
            catch (RuntimeError e) {
                this.handleException(e, this.currentLine);
                this.logFile.logRuntimeError(e.getLineNumber(), e.getMessage());
                this.logFile.saveXmlToFile();
                return;
            }
            this.preprocessed = true;
            if (this.syntaxErrors.size() > 0) {
                this.abort = true;
            }
        }
        if (this.abort) {
            return;
        }
        VisUAL.disableEditor(false);
        if (this.isBranching) {
            this.isBranching = false;
            this.currentLine = this.branchToLine;
        }
        CodeCanvas currentLineCanvas = VisUAL.getCurrentLineCanvas();
        CodeCanvas branchDestCanvas = VisUAL.getBranchDestCanvas();
        CodeCanvas branchArrowCanvas = VisUAL.getBranchArrowCanvas();
        CodeCanvas linkRegisterCanvas = VisUAL.getLinkRegisterCanvas();
        branchDestCanvas.setHidden(true, true);
        branchArrowCanvas.setHidden(true, true);
        if (this.currentLine == linkRegisterCanvas.getLineNumber()) {
            VisUAL.removeLinkRegisterIcon();
            linkRegisterCanvas.setHidden(true, true);
        }
        this.deleteOldButtons();
        this.setDispEmuStatus("on", "Emulation Running", true);
        int lineCount = this.lineArray.length;
        if (this.currentLine < lineCount || this.currentLine >= lineCount && this.isBranching) {
            ExecStatus status;
            Instruction nextInstruction;
            try {
                VisUAL.getEmulator().resetInstrCycleCount();
                nextInstruction = this.getNextAvailableInstruction(this.currentLine);
                VisUAL.getEmulator().setProgramCounter(nextInstruction.getAddress(), false);
                status = this.processLineUsingEmulator(nextInstruction.getLineNumber());
            }
            catch (Exception e) {
                boolean ignoreError = false;
                if (e instanceof RuntimeError) {
                    RuntimeError r = (RuntimeError)e;
                    if (e.getMessage().startsWith("There is no instruction to process on or after")) {
                        ignoreError = true;
                        this.finished = true;
                        this.setDispEmuStatus("alert", "Emulation Complete", false);
                        this.disableEmulation(true);
                        VisUAL.disableEditor(true);
                        if (this.logMode == EmulatorLogFile.LogMode.COMPLETION) {
                            try {
                                this.logFile.logState(this.currentLine - 1, this.getStringForLine(this.currentLine - 1), VisUAL.getEmulator());
                            }
                            catch (Exception e1) {
                                e.printStackTrace();
                            }
                        }
                        this.logFile.saveXmlToFile();
                    }
                }
                if (!ignoreError) {
                    this.handleException(e, this.currentLine);
                    this.logFile.logRuntimeError(this.currentLine, e.getMessage());
                }
                return;
            }
            VisUAL.getClockCycleBar().setCurrentCycles(VisUAL.getEmulator().getInstrCycleCount(), false);
            boolean jumpToNextLine = status == ExecStatus.FEXEC;
            currentLineCanvas.setHidden(false, false);
            currentLineCanvas.setColour(this.calcHighlightColour(status), true);
            currentLineCanvas.setLineNumber(nextInstruction.getLineNumber(), false);
            if (status != ExecStatus.SKIP) {
                this.savedStates.add(new EmulatorState(VisUAL.getEmulator()));
                if (this.logMode == EmulatorLogFile.LogMode.ALL) {
                    try {
                        this.logFile.logState(nextInstruction.getLineNumber(), this.getStringForLine(nextInstruction.getLineNumber()), VisUAL.getEmulator());
                    }
                    catch (Exception e) {
                        this.handleException(e, nextInstruction.getLineNumber());
                    }
                }
            }
            VisUAL.addCoverageMarker(new CoverageMarker(nextInstruction.getLineNumber()));
            VisUAL.scrollToLine(nextInstruction.getLineNumber());
            if (this.abort) {
                Platform.runLater(() -> {
                    this.updateUIAfterExecuteAll();
                    this.setDispEmuStatus("error", "Runtime Error", true);
                });
                return;
            }
            if (this.finished) {
                this.setDispEmuStatus("alert", "Emulation Complete", false);
                this.disableEmulation(true);
                VisUAL.disableEditor(true);
                this.logFile.saveXmlToFile();
                return;
            }
            this.currentLine = nextInstruction.getLineNumber() + 1;
            if (jumpToNextLine && VisUAL.getSettingsManager().getSkipFexec() || status == ExecStatus.SKIP) {
                this.stepForwardFired(new ActionEvent());
            }
        } else {
            this.finished = true;
            this.disableEmulation(true);
            this.setDispEmuStatus("alert", "Emulation Complete", true);
            this.logFile.saveXmlToFile();
        }
        this.btnStepBackwards.setDisable(EmulatorState.getStatePointer() == 0 || EmulatorState.getStateCount() == 0);
        VisUAL.disableEditor(true);
        this.updateUIAfterExecuteAll();
    }

    private void setDispEmuStatus(String status, String message, boolean incrementLine) {
        this.hboxStatus.setId("statusbox-" + status);
        this.lblStatus.setId("statusbox-text-" + status);
        this.lblStatus.setText(message);
        this.lblIssuesCaption.setId("statusbox-text-" + status);
        this.lblIssuesCount.setId("statusbox-text-" + status);
        this.lblLineCaption.setId("statusbox-text-" + status);
        this.lblLineNumber.setId("statusbox-text-" + status);
        if (status.equals("off")) {
            this.lblLineNumber.setText("--");
            this.lblIssuesCount.setText("0");
        } else {
            if (incrementLine) {
                this.lblLineNumber.setText("" + (this.currentLine + 1));
            } else {
                this.lblLineNumber.setText("" + this.currentLine);
            }
            this.lblIssuesCount.setText("" + (this.syntaxErrors.size() + (VisUAL.getHasRuntimeError() ? 1 : 0)));
        }
        if (status.equals("alert") && message.startsWith("Breakpoint")) {
            this.setStatusIcon("status_breakpoint.png");
        } else if (status.equals("alert") && message.endsWith("Complete")) {
            this.setStatusIcon("status_complete.png");
        } else if (status.equals("error")) {
            this.setStatusIcon("status_error.png");
        } else if (status.equals("on") && message.startsWith("Browsing")) {
            this.setStatusIcon("status_browsing.png");
        } else if (status.equals("on") && message.endsWith("Running")) {
            this.setStatusIcon("status_on.gif");
        } else if (status.equals("off")) {
            this.setStatusIcon("status_off.png");
        } else if (status.equals("on") && (message.startsWith("Analysing") || message.startsWith("Resetting"))) {
            this.setStatusIcon("loading.gif");
        }
    }

    @FXML
    public void stepBackwardFired(ActionEvent event) {
        if (this.btnStepBackwards.isDisabled()) {
            return;
        }
        if (this.firstRestore) {
            this.firstRestore = false;
            this.restoreEmulator();
        }
        this.restoreEmulator();
        this.disableEmulation(false);
    }

    @FXML
    public void resetAllFired(ActionEvent event) {
        this.scanSyntax.cancel();
        this.executeAll.cancel();
        this.setDispEmuStatus("on", "Resetting Emulator", false);
        Task<Void> clearLists = new Task<Void>(){

            protected Void call() throws Exception {
                UIController.this.syntaxErrors = new ArrayList();
                UIController.this.savedStates = new ArrayList();
                return null;
            }
        };
        clearLists.setOnFailed(event1 -> System.out.println("CLEAR LISTS TASK FAILED!"));
        clearLists.setOnScheduled(event1 -> {
            System.out.println("CLEAR LISTS TASK STARTED!");
            this.disableEmulation(true);
        });
        clearLists.setOnSucceeded(event1 -> {
            System.out.println("CLEAR LISTS TASK SUCCEEDED!");
            VisUAL.getRegAccordion().resetAll(true);
            VisUAL.clearCoverageMarkers();
            VisUAL.resetAll();
            this.memoryWindow.updateContents(VisUAL.getEmulator().memory, false);
            try {
                Emulator emulator = VisUAL.getEmulator();
                this.symbolWindow.updateContents(emulator.labels, emulator.symbols, emulator.dcdWords, false);
            }
            catch (MemoryBank.MemAccessException e) {
                this.handleException(e, this.currentLine);
            }
            this.abort = false;
            this.deferSceneUpdates = false;
            this.deferAddShiftButton = false;
            this.deferAddMemoryButton = false;
            this.deferAddPointerButton = false;
            this.deferAddStackButton = false;
            this.deferAddBranchButton = false;
            this.preprocessed = false;
            this.isBranching = false;
            this.finished = false;
            this.paused = false;
            this.currentLine = 0;
            this.setDispEmuStatus("off", "Emulator Off", false);
            this.deleteOldButtons();
            if (this.pointerPopOver != null) {
                this.pointerPopOver.hide();
            }
            if (this.shiftPopOver != null) {
                this.shiftPopOver.hide();
            }
            if (this.memoryPopOver != null) {
                this.memoryPopOver.hide();
            }
            if (this.stackPopOver != null) {
                this.stackPopOver.hide();
            }
            if (this.branchPopOver != null) {
                this.branchPopOver.hide();
            }
            if (this.errorListPopOver != null) {
                this.errorListPopOver.hide();
            }
            if (this.loopThresholdStage != null) {
                this.loopThresholdStage.close();
            }
            this.shiftIsValid = false;
            this.firstRestore = true;
            this.resume = false;
            EmulatorState.resetStateCount();
            this.hboxEditorPadding.getChildren().remove(this.lblEditorStatus);
            VisUAL.getClockCycleBar().resetAll(0);
            VisUAL.getCurrentLineCanvas().setHidden(true, true);
            VisUAL.getBranchDestCanvas().setHidden(true, true);
            VisUAL.getBranchArrowCanvas().setHidden(true, true);
            VisUAL.getLinkRegisterCanvas().setHidden(true, true);
            VisUAL.disableEditor(false);
            VisUAL.hidePopOvers();
            Gutter gutter = VisUAL.getCodeScrollPane().getGutter();
            for (int i = 0; i < this.errorIconInfo.size(); ++i) {
                gutter.removeTrackingIcon(this.errorIconInfo.get(i));
            }
            this.errorIconInfo = new ArrayList<GutterIconInfo>();
            VisUAL.removeLinkRegisterIcon();
            VisUAL.removeRuntimeErrorIcon();
            this.disableEmulation(false);
        });
        new Thread((Runnable)clearLists).start();
    }

    @FXML
    private void btnChangeLayoutFired(ActionEvent event) {
        if (this.splitPane.getOrientation().equals(Orientation.HORIZONTAL)) {
            System.out.println("Switch to portrait mode!");
            this.hboxToolbar.getChildren().removeAll(new Node[]{this.hboxStatus, this.hboxEmulationButtons});
            this.vboxToolbar.getChildren().addAll(new Node[]{this.hboxEmulationButtons, this.hboxStatus});
            this.hboxUtilityButtons.setAlignment(Pos.CENTER);
            this.hboxEmulationButtons.setAlignment(Pos.CENTER);
            this.hboxStatus.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow((Node)this.btnNewFile, (Priority)Priority.ALWAYS);
            HBox.setHgrow((Node)this.btnOpenFile, (Priority)Priority.ALWAYS);
            HBox.setHgrow((Node)this.btnSaveFile, (Priority)Priority.ALWAYS);
            HBox.setHgrow((Node)this.btnSettings, (Priority)Priority.ALWAYS);
            HBox.setHgrow((Node)this.btnTools, (Priority)Priority.ALWAYS);
            HBox.setHgrow((Node)this.btnExecuteAll, (Priority)Priority.ALWAYS);
            HBox.setHgrow((Node)this.btnResetAll, (Priority)Priority.ALWAYS);
            HBox.setHgrow((Node)this.btnStepBackwards, (Priority)Priority.ALWAYS);
            HBox.setHgrow((Node)this.btnStepForwards, (Priority)Priority.ALWAYS);
            this.hboxUtilityButtons.setStyle("-fx-padding: 10 10 0 10");
            this.hboxEmulationButtons.setStyle("-fx-padding: 10 10 0 10");
            this.hboxStatus.setStyle("-fx-padding: 10 10 10 10");
            VBox.setMargin((Node)this.hboxStatus, (Insets)new Insets(0.0, 2.0, 0.0, 2.0));
            this.splitPane.setOrientation(Orientation.VERTICAL);
            Stage primaryStage = VisUAL.getPrimaryStage();
            primaryStage.setMinWidth(495.0);
            primaryStage.setWidth(495.0);
            primaryStage.setMaxWidth(640.0);
            VisUAL.getFindAndReplacePane().changeLayout(Orientation.HORIZONTAL);
        } else {
            System.out.println("Switch to landscape mode!");
            this.vboxToolbar.getChildren().removeAll(new Node[]{this.hboxStatus, this.hboxEmulationButtons});
            this.hboxToolbar.getChildren().addAll(new Node[]{this.hboxStatus, this.hboxEmulationButtons});
            this.hboxUtilityButtons.setAlignment(Pos.CENTER_LEFT);
            this.hboxEmulationButtons.setAlignment(Pos.CENTER_RIGHT);
            this.hboxStatus.setMaxWidth(225.0);
            HBox.setHgrow((Node)this.btnNewFile, (Priority)Priority.NEVER);
            HBox.setHgrow((Node)this.btnOpenFile, (Priority)Priority.NEVER);
            HBox.setHgrow((Node)this.btnSaveFile, (Priority)Priority.NEVER);
            HBox.setHgrow((Node)this.btnSettings, (Priority)Priority.NEVER);
            HBox.setHgrow((Node)this.btnTools, (Priority)Priority.NEVER);
            HBox.setHgrow((Node)this.btnExecuteAll, (Priority)Priority.NEVER);
            HBox.setHgrow((Node)this.btnResetAll, (Priority)Priority.NEVER);
            HBox.setHgrow((Node)this.btnStepBackwards, (Priority)Priority.NEVER);
            HBox.setHgrow((Node)this.btnStepForwards, (Priority)Priority.NEVER);
            this.hboxUtilityButtons.setStyle("-fx-padding: 10 10 10 10");
            this.hboxEmulationButtons.setStyle("-fx-padding: 10 10 10 10");
            this.hboxStatus.setStyle("-fx-padding: 8 8 8 8");
            HBox.setMargin((Node)this.hboxStatus, (Insets)new Insets(0.0, 0.0, 0.0, 0.0));
            this.splitPane.setOrientation(Orientation.HORIZONTAL);
            Stage primaryStage = VisUAL.getPrimaryStage();
            primaryStage.setMaxWidth(Double.MAX_VALUE);
            primaryStage.setWidth(1024.0);
            primaryStage.setMinWidth(1024.0);
            VisUAL.getFindAndReplacePane().changeLayout(Orientation.VERTICAL);
        }
    }

    public void showFile(SourceFile sourceFile) {
        try {
            VisUAL.removeAllBreakpoints();
            this.bpInfoList.clear();
            String asString = sourceFile.getFileAsString();
            RSyntaxTextArea textArea = VisUAL.getRSyntaxTextArea();
            textArea.setText(asString.replace("\r", ""));
            this.updateFileSavedStatus();
        }
        catch (Throwable e) {
            this.handleException(e, 0);
        }
    }

    public void deleteOldButtons() {
        this.visButtonBar.getChildren().remove(this.ptrButton);
        this.visButtonBar.getChildren().remove(this.memButton);
        this.visButtonBar.getChildren().remove(this.stackButton);
        this.visButtonBar.getChildren().remove(this.shiftButton);
        this.visButtonBar.getChildren().remove(this.branchButton);
        try {
            this.codeStackPane.getChildren().remove(this.visButtonBar);
        }
        catch (NullPointerException e) {
            System.out.println("UI not yet initialised.");
        }
        this.pointerIsValid = false;
        this.memoryIsValid = false;
        this.stackIsValid = false;
        this.shiftIsValid = false;
        this.branchIsValid = false;
        this.cachedPointerInfo = new PointerInfo(null);
        this.cachedMemoryInfo = new MemoryInfo(null);
        this.cachedStackInfo = new StackInfo();
        this.cachedShiftInfo = new ShiftInfo(null);
        this.cachedBranchInfo = new BranchInfo(null);
        if (this.pointerPopOver != null) {
            this.pointerPopOver.hide();
        }
        if (this.shiftPopOver != null) {
            this.shiftPopOver.hide();
        }
        if (this.memoryPopOver != null) {
            this.memoryPopOver.hide();
        }
        if (this.stackPopOver != null) {
            this.stackPopOver.hide();
        }
        if (this.branchPopOver != null) {
            this.branchPopOver.hide();
        }
        if (this.errorListPopOver != null) {
            this.errorListPopOver.hide();
        }
    }

    private void restoreEmulator() {
        if (EmulatorState.getStatePointer() >= 0) {
            Emulator emulator = VisUAL.getEmulator();
            EmulatorState state = this.savedStates.get(EmulatorState.getStatePointer());
            VisUAL.setCurrentLineCanvas(state.getCurrentLineState());
            VisUAL.setBranchDestCanvas(state.getBranchDestState());
            VisUAL.setBranchArrowCanvas(state.getBranchArrowState());
            VisUAL.setLinkRegisterCanvas(state.getLinkRegisterState());
            VisUAL.getRegAccordion().resetAll(false);
            VisUAL.getRegAccordion().resetDispBases(state.getRegDispFormats());
            emulator.setRegisters(state.getRegisters(), state.getRegHighlights());
            StatusBitState[] statusBitStates = state.getStatusBitStates();
            StatusRegBar statusRegBar = VisUAL.getRegAccordion().getStatusRegBar();
            boolean[] statusBitValues = new boolean[4];
            for (int i = 0; i < 4; ++i) {
                statusRegBar.setBitValueOnly(statusBitStates[i].getBit(), statusBitStates[i].getValue());
                statusRegBar.setBitStyle(statusBitStates[i].getBit(), statusBitStates[i].getStyle(), false);
                statusBitValues[i] = statusBitStates[i].getValue();
            }
            emulator.setStatusBits(statusBitValues);
            this.currentLine = state.getLineNumber() + 1;
            VisUAL.getClockCycleBar().resetAll(state.getTotalCycles() - state.getCurrentCycles());
            VisUAL.getClockCycleBar().setCurrentCycles(state.getCurrentCycles(), false);
            VisUAL.scrollToLine(state.getLineNumber());
            this.deleteOldButtons();
            if (state.getPointerInfo().isValid()) {
                this.cachedPointerInfo = new PointerInfo(state.getPointerInfo());
                this.setPointerButton(this.cachedPointerInfo);
            }
            if (state.getMemoryInfo().isValid()) {
                this.cachedMemoryInfo = new MemoryInfo(state.getMemoryInfo());
                this.setMemoryButton(this.cachedMemoryInfo);
            }
            if (state.getShiftInfo().isValid()) {
                this.cachedShiftInfo = new ShiftInfo(state.getShiftInfo());
                this.setShiftButton(this.cachedShiftInfo);
            }
            if (state.getStackInfo().isValid()) {
                this.cachedStackInfo = new StackInfo(state.getStackInfo());
                this.setStackButton(this.cachedStackInfo);
            }
            if (state.getBranchInfo().isValid()) {
                this.cachedBranchInfo = new BranchInfo(state.getBranchInfo());
                this.setBranchButton(this.cachedBranchInfo);
            }
            this.setDispEmuStatus("on", "Browsing History", false);
            if (EmulatorState.getStatePointer() != 0) {
                EmulatorState.setStatePointer(-1);
            } else {
                this.btnStepBackwards.setDisable(true);
            }
        } else {
            this.btnStepBackwards.setDisable(true);
        }
    }

    public void restoreEmulator(int statePointer) {
        if (statePointer >= 0 && statePointer < EmulatorState.getStateCount()) {
            Emulator emulator = VisUAL.getEmulator();
            EmulatorState state = this.savedStates.get(statePointer);
            VisUAL.setCurrentLineCanvas(state.getCurrentLineState());
            VisUAL.setBranchDestCanvas(state.getBranchDestState());
            VisUAL.setBranchArrowCanvas(state.getBranchArrowState());
            VisUAL.setLinkRegisterCanvas(state.getLinkRegisterState());
            VisUAL.getRegAccordion().resetAll(false);
            VisUAL.getRegAccordion().resetDispBases(state.getRegDispFormats());
            emulator.setRegisters(state.getRegisters(), state.getRegHighlights());
            StatusBitState[] statusBitStates = state.getStatusBitStates();
            StatusRegBar statusRegBar = VisUAL.getRegAccordion().getStatusRegBar();
            boolean[] statusBitValues = new boolean[4];
            for (int i = 0; i < 4; ++i) {
                statusRegBar.setBitValueOnly(statusBitStates[i].getBit(), statusBitStates[i].getValue());
                statusRegBar.setBitStyle(statusBitStates[i].getBit(), statusBitStates[i].getStyle(), false);
                statusBitValues[i] = statusBitStates[i].getValue();
            }
            emulator.setStatusBits(statusBitValues);
            EmulatorState.overrideStatePointer(statePointer);
            this.currentLine = state.getLineNumber() + 1;
            VisUAL.getClockCycleBar().resetAll(state.getTotalCycles() - state.getCurrentCycles());
            VisUAL.getClockCycleBar().setCurrentCycles(state.getCurrentCycles(), false);
            VisUAL.scrollToLine(state.getLineNumber());
            this.deleteOldButtons();
            if (state.getPointerInfo().isValid()) {
                this.cachedPointerInfo = new PointerInfo(state.getPointerInfo());
                this.setPointerButton(this.cachedPointerInfo);
            }
            if (state.getMemoryInfo().isValid()) {
                this.cachedMemoryInfo = new MemoryInfo(state.getMemoryInfo());
                this.setMemoryButton(this.cachedMemoryInfo);
            }
            if (state.getShiftInfo().isValid()) {
                this.cachedShiftInfo = new ShiftInfo(state.getShiftInfo());
                this.setShiftButton(this.cachedShiftInfo);
            }
            if (state.getStackInfo().isValid()) {
                this.cachedStackInfo = new StackInfo(state.getStackInfo());
                this.setStackButton(this.cachedStackInfo);
            }
            if (state.getBranchInfo().isValid()) {
                this.cachedBranchInfo = new BranchInfo(state.getBranchInfo());
                this.setBranchButton(this.cachedBranchInfo);
            }
            this.setDispEmuStatus("on", "Browsing History", false);
        }
    }

    private ExecStatus processLineUsingEmulator(int lineNumber) throws Exception {
        ExecStatus status = ExecStatus.SKIP;
        for (Instruction i : this.instructions) {
            if (i == null) break;
            if (i.getLineNumber() != lineNumber) continue;
            status = VisUAL.getEmulator().process(i, this.deferSceneUpdates);
        }
        return status;
    }

    public String calcHighlightColour(ExecStatus status) {
        EditorTheme currentTheme = VisUAL.getCurrentTheme();
        switch (status) {
            case NORM: {
                return currentTheme.getNormHighColour();
            }
            case FEXEC: {
                return currentTheme.getFexecHighColour();
            }
            case TEXEC: {
                return currentTheme.getTexecHighColour();
            }
        }
        return currentTheme.getHoverHighColour();
    }

    private Token getTokensForLine(String line) {
        ArmV7TokenMaker tokenMaker = new ArmV7TokenMaker();
        char[] charArray = line.toCharArray();
        Segment segment = new Segment(charArray, 0, charArray.length);
        return tokenMaker.getTokenList(segment, 0, 0);
    }

    private String getStringForLine(int lineNumber) {
        Token t = this.getTokensForLine(this.lineArray[lineNumber]);
        String lineString = t.getLexeme();
        t = t.getNextToken();
        while (t.getType() != 0 && t.getType() != 1) {
            lineString = lineString + t.getLexeme();
            t = t.getNextToken();
        }
        return lineString.trim();
    }

    public Instruction getNextAvailableInstruction(int sourceLine) throws RuntimeError {
        for (Instruction i : this.instructions) {
            if (i == null) break;
            if (i.getLineNumber() < sourceLine) continue;
            return i;
        }
        throw new RuntimeError(sourceLine, "There is no instruction to process on or after line " + (sourceLine + 1), "This error commonly occurs when a branch-with-link instruction is the last instruction or when the program only contains directives.");
    }

    public Instruction getInstructionForAddress(int sourceLine, int destAddress) throws RuntimeError {
        for (Instruction i : this.instructions) {
            if (i == null) break;
            if (i.getAddress() != destAddress) continue;
            return i;
        }
        throw new RuntimeError(sourceLine, "There is no instruction with the address " + destAddress, "Check that the instruction address is specified correctly.");
    }

    public boolean instructionExistsOnLine(int lineNumber) {
        Token t = VisUAL.getRSyntaxTextArea().getTokenListForLine(lineNumber);
        while (t.getType() != 0) {
            if (t.getType() == 6) {
                return true;
            }
            t = t.getNextToken();
        }
        return false;
    }

    public Instruction getInstructionForLabel(int sourceLine, LineLabel destLabel) throws RuntimeError {
        int destLine = destLabel.getLineNo();
        try {
            return this.getNextAvailableInstruction(destLine);
        }
        catch (RuntimeError e) {
            throw new RuntimeError(sourceLine, "There is no instruction with the label '" + destLabel.getId() + "'", "Check that the branch destination is specified correctly.");
        }
    }

    public Instruction getLinkedInstruction(int sourceLine) throws RuntimeError {
        for (Instruction i : this.instructions) {
            if (i == null) break;
            if (i.getLineNumber() <= sourceLine) continue;
            return i;
        }
        throw new RuntimeError(sourceLine, "This is the last instruction.", "No valid branch-with-link operation possible.");
    }

    private void preProcess() throws RuntimeError {
        this.logFile = new EmulatorLogFile("visual_log.xml");
        this.logMode = VisUAL.getSettingsManager().getLoggingMode();
        EmulatorLogFile.reconfigureLogging();
        this.toProcess = VisUAL.getRSyntaxTextArea().getText();
        this.lineArray = this.toProcess.split("\\r?\\n");
        if (VisUAL.getSettingsManager().getAutoInstSize()) {
            double instSize = 4 * this.lineArray.length;
            int roundedMemSize = (int)(256.0 * Math.ceil(Math.abs(instSize / 256.0)));
            MemoryBank.setBaseAddress(roundedMemSize);
            VisUAL.getSettingsManager().setInstMemSize(roundedMemSize);
        } else {
            MemoryBank.setBaseAddress(VisUAL.getSettingsManager().getInstMemSize());
            if (this.lineArray.length > Integer.divideUnsigned(VisUAL.getSettingsManager().getInstMemSize(), 4)) {
                this.logFile.logRuntimeError(1, "Number of lines of code > Instruction memory size.");
                throw new RuntimeError(1, "Number of lines of code > Instruction memory size.", "Press F2 for details.");
            }
        }
        MemoryBank.setInitValue(VisUAL.getSettingsManager().getMemoryInitValue());
        this.scanSyntax = new Task<Boolean>(){

            protected Boolean call() throws Exception {
                UIController.this.instructions.clear();
                UIController.this.instructions.addAll(SyntaxScanner.createEmulatorFile(UIController.this.lineArray, VisUAL.getEmulator(), UIController.this.syntaxErrors, UIController.this.deferSceneUpdates));
                return UIController.this.syntaxErrors.size() == 0;
            }
        };
        this.scanSyntax.setOnSucceeded(event1 -> {
            System.out.println("PRE-PROCESSING TASK SUCCEEDED!");
            if (!((Boolean)this.scanSyntax.getValue()).booleanValue()) {
                for (SyntaxError error : this.syntaxErrors) {
                    if (error == null) break;
                    this.errorIconInfo.add(VisUAL.addSyntaxError(error.getLineNumber()));
                }
                VisUAL.disableEditor(true);
                this.disableEmulation(true);
                Platform.runLater(() -> this.setDispEmuStatus("error", "Syntax Error(s) Found", false));
            } else {
                Platform.runLater(() -> this.setDispEmuStatus("on", "Emulation Running", false));
            }
            this.hboxEditorPadding.getChildren().add(this.lblEditorStatus);
        });
        this.scanSyntax.setOnScheduled(event1 -> {
            System.out.println("PRE-PROCESSING TASK STARTED!");
            Platform.runLater(() -> this.setDispEmuStatus("on", "Analysing Code", false));
        });
        this.scanSyntax.setOnCancelled(event1 -> System.out.println("PRE-PROCESSING TASK CANCELLED!"));
        new Thread((Runnable)this.scanSyntax).run();
    }

    public void disableEmulation(boolean value) {
        this.btnExecuteAll.setDisable(value);
        this.btnStepForwards.setDisable(value);
        this.btnStepBackwards.setDisable(EmulatorState.getStatePointer() == 0 || EmulatorState.getStateCount() == 0);
    }

    private void setStatusIcon(String imageName) {
        this.imgStatusIcon.setImage(new Image(VisUAL.class.getResourceAsStream("/images/" + imageName)));
    }

    public void setBranchToLine(int i) throws RuntimeError {
        if (i >= this.lineArray.length) {
            throw new RuntimeError(this.currentLine, "Invalid branch destination address.", "The branch destination address " + i * 4 + " is not a valid instruction address.");
        }
        this.branchToLine = i;
        this.isBranching = true;
    }

    public int getCurrentLine() {
        return this.currentLine;
    }

    public void addBreakpointIndicator(GutterIconInfo info) {
        this.bpInfoList.add(info);
    }

    public GutterIconInfo getBreakpointIndicator(int lineNumber) {
        RSyntaxTextArea rsta = VisUAL.getRSyntaxTextArea();
        for (GutterIconInfo i : this.bpInfoList) {
            try {
                if (rsta.getLineOfOffset(i.getMarkedOffset()) != lineNumber) continue;
                return i;
            }
            catch (BadLocationException e) {
                System.out.println("Invalid offset location for breakpoint icon.");
            }
        }
        return null;
    }

    public void removeBreakpointIndicator(int lineNumber) {
        RSyntaxTextArea rsta = VisUAL.getRSyntaxTextArea();
        for (int i = 0; i < this.bpInfoList.size(); ++i) {
            try {
                if (rsta.getLineOfOffset(this.bpInfoList.get(i).getMarkedOffset()) != lineNumber) continue;
                this.bpInfoList.remove(i);
                return;
            }
            catch (BadLocationException e) {
                System.out.println("Invalid offset location for breakpoint icon.");
            }
        }
    }

    public void clearBreakpoints() {
        VisUAL.getCodeScrollPane().getGutter().removeAllTrackingIcons();
        this.bpInfoList.clear();
    }

    public List<GutterIconInfo> getBpInfoList() {
        return this.bpInfoList;
    }

    public boolean breakpointExists(int lineNumber) {
        RSyntaxTextArea rsta = VisUAL.getRSyntaxTextArea();
        for (GutterIconInfo i : this.bpInfoList) {
            if (i == null) break;
            try {
                if (rsta.getLineOfOffset(i.getMarkedOffset()) != lineNumber) continue;
                return true;
            }
            catch (BadLocationException e) {
                System.out.println("Invalid offset location for breakpoint icon.");
            }
        }
        return false;
    }

    public Timeline getTimeline() {
        return this.timeline;
    }

    private void addButtonToButtonBar(Button button, VisualisationInfo info) {
        if (!this.codeStackPane.getChildren().contains(this.visButtonBar)) {
            this.codeStackPane.getChildren().add(this.visButtonBar);
        }
        this.visButtonBar.addButton(button);
        this.visButtonBar.translateXProperty().bind((ObservableValue)VisUAL.getCurrentLineCanvas().visBtnXOffsetProperty);
        double finalYPos = (double)(info.getLineNumber() * VisUAL.getRSyntaxTextArea().getLineHeight() - VisUAL.getCodeScrollPane().getVerticalScrollBar().getValue()) - this.codeStackPane.getHeight() / 2.0 + (double)((this.lineHeight + 8) / 2) - 5.0;
        this.visButtonBar.translateYProperty().set(finalYPos);
        VisUAL.getCodeScrollPane().getVerticalScrollBar().removeAdjustmentListener(this.visButtonAdjuster);
        this.visButtonAdjuster = adjustmentEvent -> Platform.runLater(() -> {
            double newYPos = (double)(info.getLineNumber() * VisUAL.getRSyntaxTextArea().getLineHeight() - VisUAL.getCodeScrollPane().getVerticalScrollBar().getValue()) - this.codeStackPane.getHeight() / 2.0 + this.visButtonBar.getHeight() / 2.0 - 5.0;
            this.visButtonBar.translateYProperty().set(newYPos);
            if (this.pointerPopOver != null && this.pointerPopOver.isShowing()) {
                this.pointerPopOver.hide();
            }
            if (this.memoryPopOver != null && this.memoryPopOver.isShowing()) {
                this.memoryPopOver.hide();
            }
            if (this.shiftPopOver != null && this.shiftPopOver.isShowing()) {
                this.shiftPopOver.hide();
            }
            if (this.stackPopOver != null && this.stackPopOver.isShowing()) {
                this.stackPopOver.hide();
            }
            if (this.branchPopOver != null && this.branchPopOver.isShowing()) {
                this.branchPopOver.hide();
            }
        });
        VisUAL.getCodeScrollPane().getVerticalScrollBar().addAdjustmentListener(this.visButtonAdjuster);
    }

    private void stylePopOver(PopOver popOver, Button button) {
        boolean bottom;
        double xPosition = button.localToScreen(button.getBoundsInLocal()).getMinX() + popOver.getWidth();
        double yPosition = button.localToScreen(button.getBoundsInLocal()).getMinY() + popOver.getHeight();
        boolean left = xPosition > 0.0 && xPosition < Screen.getPrimary().getBounds().getMaxX();
        boolean bl = bottom = yPosition > 0.0 && yPosition < Screen.getPrimary().getBounds().getMaxY();
        if (left && bottom) {
            popOver.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
        } else if (left) {
            popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_LEFT);
        } else if (bottom) {
            popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        } else {
            popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
        }
        popOver.setAutoFix(false);
        popOver.setAnchorX(0.0);
    }

    public void setPointerButton(PointerInfo info) {
        this.cachedPointerInfo = info;
        if (this.deferSceneUpdates) {
            this.deferAddPointerButton = true;
            return;
        }
        this.deferAddPointerButton = false;
        this.pointerPopOver = new PopOver();
        PointerPopOverPane contentPane = new PointerPopOverPane(this.pointerPopOver, info);
        this.pointerPopOver.setContentNode((Node)contentPane);
        this.pointerPopOver.setDetachable(false);
        this.pointerIsValid = true;
        if (VisUAL.getSettingsManager().getPointerInfo()) {
            this.ptrButton = new Button();
            this.addButtonToButtonBar(this.ptrButton, info);
            this.ptrButton.setText("Pointer");
            Tooltip.install((Node)this.ptrButton, (Tooltip)new Tooltip("Click to show pointer information"));
            if (VisUAL.getCurrentTheme().getName().equals("Dark")) {
                this.ptrButton.setId("visbuttonbar-pointer");
            } else if (VisUAL.getCurrentTheme().getName().equals("Light")) {
                this.ptrButton.setId("visbuttonbar-pointer-light");
            }
            this.ptrButton.setOnAction(e -> {
                if (!this.pointerPopOver.isShowing()) {
                    this.stylePopOver(this.pointerPopOver, this.ptrButton);
                    this.pointerPopOver.show((Node)this.ptrButton);
                } else {
                    this.pointerPopOver.hide();
                }
            });
        }
    }

    public void setMemoryButton(MemoryInfo info) {
        this.cachedMemoryInfo = info;
        if (this.deferSceneUpdates) {
            this.deferAddMemoryButton = true;
            return;
        }
        this.deferAddMemoryButton = false;
        this.memoryPopOver = new PopOver();
        this.memoryPopOver.setDetachable(false);
        this.memoryPopOver.setContentNode((Node)new MemoryPopOverPane(this.memoryPopOver, info));
        this.memoryIsValid = true;
        if (VisUAL.getSettingsManager().getMemoryAccess()) {
            this.memButton = new Button();
            this.addButtonToButtonBar(this.memButton, info);
            this.memButton.setText("Memory");
            Tooltip.install((Node)this.memButton, (Tooltip)new Tooltip("Click to show memory access information"));
            if (VisUAL.getCurrentTheme().getName().equals("Dark")) {
                this.memButton.setId("visbuttonbar-memory");
            } else if (VisUAL.getCurrentTheme().getName().equals("Light")) {
                this.memButton.setId("visbuttonbar-memory-light");
            }
            this.memButton.setOnAction(e -> {
                if (!this.memoryPopOver.isShowing()) {
                    this.stylePopOver(this.memoryPopOver, this.memButton);
                    this.memoryPopOver.show((Node)this.memButton);
                } else {
                    this.memoryPopOver.hide();
                }
            });
        }
    }

    public void setStackButton(StackInfo info) {
        this.cachedStackInfo = info;
        if (this.deferSceneUpdates) {
            this.deferAddStackButton = true;
            return;
        }
        this.stackPopOver = new PopOver();
        this.stackPopOver.setDetachable(false);
        this.stackPopOver.setContentNode((Node)new StackPopOverPane(this.stackPopOver, info));
        this.stackIsValid = true;
        if (VisUAL.getSettingsManager().getStackVis()) {
            this.stackButton = new Button();
            this.addButtonToButtonBar(this.stackButton, info);
            this.stackButton.setText("Stack");
            Tooltip.install((Node)this.stackButton, (Tooltip)new Tooltip("Click to show stack information"));
            if (VisUAL.getCurrentTheme().getName().equals("Dark")) {
                this.stackButton.setId("visbuttonbar-stack");
            } else if (VisUAL.getCurrentTheme().getName().equals("Light")) {
                this.stackButton.setId("visbuttonbar-stack-light");
            }
            this.stackButton.setOnAction(e -> {
                if (!this.stackPopOver.isShowing()) {
                    this.stylePopOver(this.stackPopOver, this.stackButton);
                    this.stackPopOver.show((Node)this.stackButton);
                } else {
                    this.stackPopOver.hide();
                }
            });
        }
    }

    public void setBranchButton(BranchInfo info) {
        this.cachedBranchInfo = info;
        if (this.deferSceneUpdates) {
            this.deferAddBranchButton = true;
            return;
        }
        this.branchPopOver = new PopOver();
        this.branchPopOver.setDetachable(false);
        this.branchPopOver.setContentNode((Node)new BranchPopOverPane(this.branchPopOver, info));
        this.branchIsValid = true;
        if (VisUAL.getSettingsManager().getBranchVis()) {
            this.branchButton = new Button();
            this.addButtonToButtonBar(this.branchButton, info);
            this.branchButton.setText("Branch");
            Tooltip.install((Node)this.branchButton, (Tooltip)new Tooltip("Click to show branch information"));
            if (VisUAL.getCurrentTheme().getName().equals("Dark")) {
                this.branchButton.setId("visbuttonbar-shift");
            } else if (VisUAL.getCurrentTheme().getName().equals("Light")) {
                this.branchButton.setId("visbuttonbar-shift-light");
            }
            this.branchButton.setOnAction(e -> {
                if (!this.branchPopOver.isShowing()) {
                    this.stylePopOver(this.branchPopOver, this.branchButton);
                    this.branchPopOver.show((Node)this.branchButton);
                } else {
                    this.branchPopOver.hide();
                }
            });
        }
    }

    public void setShiftButton(ShiftInfo info) {
        this.cachedShiftInfo = info;
        if (this.deferSceneUpdates) {
            this.deferAddShiftButton = true;
            return;
        }
        this.shiftPopOver = new PopOver();
        ShiftPopOverPane shiftPopOverPane = new ShiftPopOverPane(this.shiftPopOver, 0);
        this.shiftPopOver.setContentNode((Node)shiftPopOverPane);
        this.shiftPopOver.setDetachable(false);
        this.shiftIsValid = true;
        if (VisUAL.getSettingsManager().getShiftOps()) {
            this.shiftButton = new Button();
            this.addButtonToButtonBar(this.shiftButton, info);
            this.shiftButton.setText("Shift");
            Tooltip.install((Node)this.shiftButton, (Tooltip)new Tooltip("Click to show shift operation information"));
            if (VisUAL.getCurrentTheme().getName().equals("Dark")) {
                this.shiftButton.setId("visbuttonbar-shift");
            } else if (VisUAL.getCurrentTheme().getName().equals("Light")) {
                this.shiftButton.setId("visbuttonbar-shift-light");
            }
            this.shiftButton.setOnAction(e -> {
                if (!this.shiftPopOver.isShowing()) {
                    this.stylePopOver(this.shiftPopOver, this.shiftButton);
                    this.shiftPopOver.show((Node)this.shiftButton);
                    this.timeline.play();
                } else {
                    this.shiftPopOver.hide();
                }
            });
        }
        this.timeline.setCycleCount(1);
        this.timeline.setAutoReverse(false);
        ArrayList<KeyFrame> keyFrames = new ArrayList<KeyFrame>();
        List<String> values = info.getValues();
        int barOffset = 22;
        if (System.getProperty("os.name").contains("nix") || System.getProperty("os.name").contains("nux")) {
            barOffset += 2;
        }
        int translateOffset = 22;
        int translateInit = -717;
        boolean translateToLeft = false;
        if (info.getType() != ShiftType.LSL) {
            if (System.getProperty("os.name").contains("nix") || System.getProperty("os.name").contains("nux")) {
                translateInit -= 65;
                translateOffset += 2;
            }
            shiftPopOverPane.getRect().setTranslateX((double)translateInit);
            translateToLeft = true;
        }
        shiftPopOverPane.setOriginalValue(values.get(0).replace(" ", ""), info.getDispFormat());
        shiftPopOverPane.setNewValue(values.get(values.size() - 1).replace(" ", ""), info.getDispFormat());
        String appendToSummary = "";
        if (!info.useShiftCarry()) {
            shiftPopOverPane.getCarry().setDisable(true);
            shiftPopOverPane.getCarry().setText("-");
            appendToSummary = "\nThe carry from the parent instruction takes precedence over the shift carry.";
        }
        switch (info.getType()) {
            case ASR: {
                shiftPopOverPane.setSummary("Arithmetic shift right by " + info.getCount() + " bits. " + info.getCount() + " LSBs are shifted out. Signed extension is used to fill spaces on the left." + appendToSummary);
                break;
            }
            case LSL: {
                shiftPopOverPane.setSummary("Logical shift left by " + info.getCount() + " bits. " + info.getCount() + " MSBs are shifted out. Zeros are used to fill spaces on the right." + appendToSummary);
                break;
            }
            case LSR: {
                shiftPopOverPane.setSummary("Logical shift right by " + info.getCount() + " bits. " + info.getCount() + " LSBs are shifted out. Zeros are used to fill spaces on the left." + appendToSummary);
                break;
            }
            case ROR: {
                shiftPopOverPane.setKeyNew("Rotated Bits");
                shiftPopOverPane.setSummary("Rotate right by " + info.getCount() + " bits. " + info.getCount() + " LSBs are shifted out and re-inserted as MSBs on the left." + appendToSummary);
                break;
            }
            case RRX: {
                shiftPopOverPane.setKeyNew("Carry Bit Before");
                shiftPopOverPane.setSummary("Rotate right by 1 bit and extend. Bit 0 is shifted out. Bit 31 is set to the carry bit before execution." + appendToSummary);
            }
        }
        for (int i = 0; i < values.size(); ++i) {
            keyFrames.add(new KeyFrame(Duration.millis((double)(i * 500)), new KeyValue[]{new KeyValue((WritableValue)shiftPopOverPane.getLabel().textProperty(), values.get(i))}));
            if (i != 0) {
                if (info.useShiftCarry()) {
                    keyFrames.add(new KeyFrame(Duration.millis((double)(i * 500)), new KeyValue[]{new KeyValue((WritableValue)shiftPopOverPane.getCarry().textProperty(), info.getCarrys().get(i))}));
                }
                keyFrames.add(new KeyFrame(Duration.millis((double)(i * 500)), new KeyValue[]{new KeyValue((WritableValue)shiftPopOverPane.getRect().widthProperty(), (10 + barOffset * i), Interpolator.DISCRETE)}));
                if (!translateToLeft) continue;
                keyFrames.add(new KeyFrame(Duration.millis((double)(i * 500)), new KeyValue[]{new KeyValue((WritableValue)shiftPopOverPane.getRect().translateXProperty(), (translateInit + 10 + translateOffset * i), Interpolator.DISCRETE)}));
                continue;
            }
            keyFrames.add(new KeyFrame(Duration.millis((double)(i * 500)), new KeyValue[]{new KeyValue((WritableValue)shiftPopOverPane.getCarry().textProperty(), VisUAL.getEmulator().getCarryAsText())}));
            keyFrames.add(new KeyFrame(Duration.millis((double)(i * 500)), new KeyValue[]{new KeyValue((WritableValue)shiftPopOverPane.getRect().widthProperty(), 0, Interpolator.DISCRETE)}));
        }
        this.timeline.getKeyFrames().addAll(keyFrames);
    }

    public List<SyntaxError> getSyntaxErrors() {
        return this.syntaxErrors;
    }

    public void setAbort(boolean value) {
        this.abort = value;
    }

    public void setLineHeight(int value) {
        this.lineHeight = value;
        this.deleteOldButtons();
    }

    public void setLoopThresholdStage(Stage stage) {
        this.loopThresholdStage = stage;
    }

    public void showShiftPopOver() {
        if (!this.shiftIsValid) {
            return;
        }
        this.shiftPopOver.show((Node)this.codeStackPane);
        this.shiftPopOver.setAnchorX(VisUAL.getCodeScrollPane().getLocationOnScreen().getX() + (double)(VisUAL.getCodeScrollPane().getWidth() / 2));
        this.shiftPopOver.setAnchorY(VisUAL.getCodeScrollPane().getLocationOnScreen().getY() + (double)(VisUAL.getCodeScrollPane().getHeight() / 2) - this.shiftPopOver.getHeight());
        this.timeline.play();
    }

    public void showPointerPopOver() {
        if (!this.pointerIsValid) {
            return;
        }
        this.pointerPopOver.show((Node)this.codeStackPane);
        this.pointerPopOver.setAnchorX(VisUAL.getCodeScrollPane().getLocationOnScreen().getX() + (double)(VisUAL.getCodeScrollPane().getWidth() / 2));
        this.pointerPopOver.setAnchorY(VisUAL.getCodeScrollPane().getLocationOnScreen().getY() + (double)(VisUAL.getCodeScrollPane().getHeight() / 2) - this.pointerPopOver.getHeight());
    }

    public void showMemoryPopOver() {
        if (!this.memoryIsValid) {
            return;
        }
        this.memoryPopOver.show((Node)this.codeStackPane);
        this.memoryPopOver.setAnchorX(VisUAL.getCodeScrollPane().getLocationOnScreen().getX() + (double)(VisUAL.getCodeScrollPane().getWidth() / 2));
        this.memoryPopOver.setAnchorY(VisUAL.getCodeScrollPane().getLocationOnScreen().getY() + (double)(VisUAL.getCodeScrollPane().getHeight() / 2) - this.memoryPopOver.getHeight());
    }

    public void showStackPopOver() {
        if (!this.stackIsValid) {
            return;
        }
        this.stackPopOver.show((Node)this.codeStackPane);
        this.stackPopOver.setAnchorX(VisUAL.getCodeScrollPane().getLocationOnScreen().getX() + (double)(VisUAL.getCodeScrollPane().getWidth() / 2));
        this.stackPopOver.setAnchorY(VisUAL.getCodeScrollPane().getLocationOnScreen().getY() + (double)(VisUAL.getCodeScrollPane().getHeight() / 2) - this.stackPopOver.getHeight());
    }

    public void showBranchPopOver() {
        if (!this.branchIsValid) {
            return;
        }
        this.branchPopOver.show((Node)this.codeStackPane);
        this.branchPopOver.setAnchorX(VisUAL.getCodeScrollPane().getLocationOnScreen().getX() + (double)(VisUAL.getCodeScrollPane().getWidth() / 2));
        this.branchPopOver.setAnchorY(VisUAL.getCodeScrollPane().getLocationOnScreen().getY() + (double)(VisUAL.getCodeScrollPane().getHeight() / 2) - this.branchPopOver.getHeight());
    }

    public PointerInfo getCachedPointerInfo() {
        return this.cachedPointerInfo;
    }

    public MemoryInfo getCachedMemoryInfo() {
        return this.cachedMemoryInfo;
    }

    public StackInfo getCachedStackInfo() {
        return this.cachedStackInfo;
    }

    public ShiftInfo getCachedShiftInfo() {
        return this.cachedShiftInfo;
    }

    public BranchInfo getCachedBranchInfo() {
        return this.cachedBranchInfo;
    }

    public boolean isAbort() {
        return this.abort;
    }

    public void setPaused(boolean value) {
        this.paused = value;
    }

    public void setFinished(boolean value) {
        this.finished = value;
    }

    public StackPane getCodeStackPane() {
        return this.codeStackPane;
    }

    public EmulatorLogFile getLogFile() {
        return this.logFile;
    }

    private class ImmediateFormatException
    extends Exception {
        private int lineNumber;
        private String details;

        public ImmediateFormatException(int lineNumber, String message, String details) {
            super(message);
            this.details = details;
            this.lineNumber = lineNumber;
        }

        public int getLineNumber() {
            return this.lineNumber;
        }

        public String getDetails() {
            return this.details;
        }
    }
}

