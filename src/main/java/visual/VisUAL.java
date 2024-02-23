/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.animation.KeyFrame
 *  javafx.animation.KeyValue
 *  javafx.animation.Timeline
 *  javafx.application.Application
 *  javafx.application.Platform
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.SimpleDoubleProperty
 *  javafx.beans.value.ObservableValue
 *  javafx.embed.swing.SwingNode
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.geometry.Side
 *  javafx.scene.Node
 *  javafx.scene.Parent
 *  javafx.scene.Scene
 *  javafx.scene.canvas.Canvas
 *  javafx.scene.control.Accordion
 *  javafx.scene.control.Alert
 *  javafx.scene.control.Alert$AlertType
 *  javafx.scene.control.Button
 *  javafx.scene.control.ButtonType
 *  javafx.scene.control.ContextMenu
 *  javafx.scene.control.MenuItem
 *  javafx.scene.control.ScrollPane
 *  javafx.scene.effect.BlendMode
 *  javafx.scene.image.Image
 *  javafx.scene.image.ImageView
 *  javafx.scene.input.Dragboard
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyCodeCombination
 *  javafx.scene.input.KeyCombination
 *  javafx.scene.input.KeyCombination$Modifier
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.input.TransferMode
 *  javafx.scene.layout.AnchorPane
 *  javafx.scene.layout.StackPane
 *  javafx.scene.text.Font
 *  javafx.stage.Screen
 *  javafx.stage.Stage
 *  javafx.stage.StageStyle
 *  javafx.util.Duration
 */
package visual;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.BadLocationException;
import org.controlsfx.control.PopOver;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.GutterIconInfo;
import org.fife.ui.rtextarea.RTextScrollPane;
import visual.CanvasType;
import visual.ClockCycleBar;
import visual.CodeCanvas;
import visual.CodeCanvasState;
import visual.CoverageMarker;
import visual.CycleCounter;
import visual.EditorTheme;
import visual.Emulator;
import visual.EmulatorLogFile;
import visual.ErrorPopOverPane;
import visual.FindAndReplacePane;
import visual.InitialSetupPane;
import visual.ManualResizeSwingNode;
import visual.RegAccordion;
import visual.SettingsManager;
import visual.SourceFile;
import visual.StatusRegBar;
import visual.SyntaxError;
import visual.TooltipPopOverPane;
import visual.UIController;

public class VisUAL extends Application {
    private static SettingsManager settingsManager = new SettingsManager();
    private static UIController uiController = new UIController();
    private static EditorTheme darkTheme = new EditorTheme("Dark", "#272282", "#807146", "#406d6a", "#6a3a3f", "#535353", "#4A3973", "#535353");
    private static EditorTheme lightTheme = new EditorTheme("Light", "#fafafa", "#f6e0ae", "#b5e4dc", "#f2c9d6", "#e2e2e2", "#E8D5FE", "#e2e2e2");
    private static EditorTheme solarizedDarkTheme = new EditorTheme("Dark", "#002b36", "#807146", "#5b6834", "#6a3a3f", "#073642", "#313360", "#073642");
    private static EditorTheme solarizedLightTheme = new EditorTheme("Light", "#fdf6e3", "#f6e0ae", "#d3df84", "#ffdedd", "#eee8d5", "#d0d3ff", "#eee8d5");
    private static RegAccordion regAcc = new RegAccordion();
    private static Emulator emulator = new Emulator();
    private static CodeCanvas hoverCanvas = new CodeCanvas(CanvasType.HIGHLIGHT);
    private static CodeCanvas currentLineCanvas = new CodeCanvas(CanvasType.HIGHLIGHT);
    private static CodeCanvas branchDestCanvas = new CodeCanvas(CanvasType.HIGHLIGHT);
    private static CodeCanvas branchArrowCanvas = new CodeCanvas(CanvasType.ARROW);
    private static CodeCanvas linkRegisterCanvas = new CodeCanvas(CanvasType.HIGHLIGHT);
    private static List<CodeCanvas> breakpointCanvases = new ArrayList<CodeCanvas>();
    private static List<CodeCanvas> syntaxErrorCanvases = new ArrayList<CodeCanvas>();
    private static List<CoverageMarker> coverageMarkers = new ArrayList<CoverageMarker>();
    private static Timeline repositionBreakpoints;
    private static RSyntaxTextArea textArea;
    private static RTextScrollPane codeScrollPane;
    private static ManualResizeSwingNode swingNode;
    private static Icon breakpoint;
    private static Icon syntaxError;
    private static Icon syntaxErrorWithBreakpoint;
    private static Icon linkRegister;
    private static GutterIconInfo linkRegisterIconInfo;
    private static GutterIconInfo runtimeErrorIconInfo;
    private static List<GutterIconInfo> pendingBreakpointsIconInfo;
    private static StackPane codeStackPane;
    private static Scene scene;
    private static Stage primaryStage;
    private static PopOver tooltipPopOver;
    private static PopOver errorPopOver;
    private static PopOver runtimePopOver;
    private static FindAndReplacePane findAndReplacePane;
    private static boolean hasRuntimeError;
    private static int runtimeErrorLineNumber;
    private static int syntaxErrorLineNumber;
    private static int lineHeight;
    private static ClockCycleBar clockCycleBar;
    private static ImageView easterEgg;
    private static int easterEggIndex;
    private static Image frameZero;
    private static Image frameOne;
    private static Image frameTwo;
    private static Image frameThree;
    private static double oldScrollPosition;
    private static CycleCounter cycleCounter;

    public void start(Stage stage) throws Exception {
        if (settingsManager.isFirstRun()) {
            Stage initStage = new Stage();
            InitialSetupPane initPane = new InitialSetupPane(initStage);
            initStage.setScene(new Scene((Parent)initPane));
            initStage.setTitle("Initial Setup");
            initStage.initStyle(StageStyle.UNDECORATED);
            initStage.showAndWait();
        }
        javafx.scene.text.Font.loadFont((String)VisUAL.class.getResource("/fonts/OpenSans-Regular.ttf").toExternalForm(), (double)10.0);
        javafx.scene.text.Font.loadFont((String)VisUAL.class.getResource("/fonts/OpenSans-Bold.ttf").toExternalForm(), (double)10.0);
        javafx.scene.text.Font.loadFont((String)VisUAL.class.getResource("/fonts/OpenSans-Italic.ttf").toExternalForm(), (double)10.0);
        javafx.scene.text.Font.loadFont((String)VisUAL.class.getResource("/fonts/OpenSans-BoldItalic.ttf").toExternalForm(), (double)10.0);
        primaryStage = stage;
        UIController root = uiController;
        codeStackPane = uiController.getCodeStackPane();
        swingNode = new ManualResizeSwingNode();
        codeScrollPane.setIconRowHeaderEnabled(true);
        VisUAL.createSwingContent(swingNode);
        Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        codeScrollPane.setViewportBorder(border);
        codeScrollPane.setBorder(border);
        scene = new Scene((Parent)root);
        stage.setScene(scene);
        stage.getIcons().add(new Image(VisUAL.class.getResource("/images/icon.png").toExternalForm()));
        stage.setTitle("untitled.S - VisUAL");
        stage.setMinWidth(1024.0);
        stage.setMinHeight(480.0);
        stage.show();
        scene.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(new TransferMode[]{TransferMode.COPY});
            } else {
                event.consume();
            }
        });
        scene.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;
                Iterator iterator = db.getFiles().iterator();
                if (iterator.hasNext()) {
                    File file = (File)iterator.next();
                    uiController.resetAllFired(new ActionEvent());
                    uiController.showFile(new SourceFile(file, false));
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
        stage.setOnCloseRequest(event1 -> {
            if (!uiController.menuQuitFired(new ActionEvent())) {
                event1.consume();
            }
        });
        if (System.getProperty("os.name").contains("Mac")) {
            uiController.setMacMenuBar();
        }
        findAndReplacePane = new FindAndReplacePane(textArea);
        codeStackPane.getChildren().add(easterEgg);
        codeStackPane.getChildren().add(hoverCanvas);
        codeStackPane.getChildren().add(branchArrowCanvas);
        codeStackPane.getChildren().add(branchDestCanvas);
        codeStackPane.getChildren().add(currentLineCanvas);
        codeStackPane.getChildren().add(linkRegisterCanvas);
        codeStackPane.getChildren().add(swingNode);
        repositionBreakpoints = new Timeline(new KeyFrame[]{new KeyFrame(Duration.seconds((double)0.25), event -> {
            List<GutterIconInfo> bpInfo = uiController.getBpInfoList();
            ArrayList<Integer> toRemove = new ArrayList<Integer>();
            for (int i = 0; i < breakpointCanvases.size(); ++i) {
                try {
                    if (textArea.getLineOfOffset(bpInfo.get(i).getMarkedOffset()) != breakpointCanvases.get(i).getLineNumber()) {
                        breakpointCanvases.get(i).setLineNumber(textArea.getLineOfOffset(bpInfo.get(i).getMarkedOffset()), false);
                    }
                    if (uiController.instructionExistsOnLine(textArea.getLineOfOffset(bpInfo.get(i).getMarkedOffset()))) continue;
                    toRemove.add(textArea.getLineOfOffset(bpInfo.get(i).getMarkedOffset()));
                    continue;
                }
                catch (BadLocationException e) {
                    System.out.println("Invalid offset for breakpoint icon.");
                }
            }
            for (Integer i : toRemove) {
                VisUAL.removeBreakpoint(uiController.getBreakpointIndicator(i));
                uiController.removeBreakpointIndicator(i);
            }
            if (!uiController.isAbort()) {
                ArrayList<GutterIconInfo> removeFromPending = new ArrayList<GutterIconInfo>();
                for (GutterIconInfo i : pendingBreakpointsIconInfo) {
                    try {
                        int lineNumber = textArea.getLineOfOffset(i.getMarkedOffset());
                        GutterIconInfo newIcon = codeScrollPane.getGutter().addLineTrackingIcon(lineNumber, breakpoint);
                        uiController.getBpInfoList().remove(uiController.getBreakpointIndicator(lineNumber));
                        uiController.getBpInfoList().add(newIcon);
                        removeFromPending.add(i);
                    }
                    catch (BadLocationException e) {
                        System.out.println("Invalid offset for breakpoint icon.");
                    }
                }
                pendingBreakpointsIconInfo.removeAll(removeFromPending);
            }
        }, new KeyValue[0])});
        repositionBreakpoints.setCycleCount(-1);
        repositionBreakpoints.play();
        root.setOnKeyReleased((EventHandler)new EventHandler<KeyEvent>(){
            final KeyCombination showShiftOps = new KeyCodeCombination(KeyCode.S, new KeyCombination.Modifier[]{KeyCombination.SHORTCUT_DOWN, KeyCombination.ALT_DOWN});
            final KeyCombination showPointerInfo = new KeyCodeCombination(KeyCode.P, new KeyCombination.Modifier[]{KeyCombination.SHORTCUT_DOWN, KeyCombination.ALT_DOWN});
            final KeyCombination showMemoryAccess = new KeyCodeCombination(KeyCode.M, new KeyCombination.Modifier[]{KeyCombination.SHORTCUT_DOWN, KeyCombination.ALT_DOWN});
            final KeyCombination showStackVis = new KeyCodeCombination(KeyCode.K, new KeyCombination.Modifier[]{KeyCombination.SHORTCUT_DOWN, KeyCombination.ALT_DOWN});
            final KeyCombination showBranchVis = new KeyCodeCombination(KeyCode.B, new KeyCombination.Modifier[]{KeyCombination.SHORTCUT_DOWN, KeyCombination.ALT_DOWN});
            final KeyCombination showHelp = new KeyCodeCombination(KeyCode.F1, new KeyCombination.Modifier[0]);
            final KeyCombination showMemoryMap = new KeyCodeCombination(KeyCode.F2, new KeyCombination.Modifier[0]);
            final KeyCombination execute = new KeyCodeCombination(KeyCode.F5, new KeyCombination.Modifier[0]);
            final KeyCombination reset = new KeyCodeCombination(KeyCode.F6, new KeyCombination.Modifier[0]);
            final KeyCombination stepBackwards = new KeyCodeCombination(KeyCode.F7, new KeyCombination.Modifier[0]);
            final KeyCombination stepForwards = new KeyCodeCombination(KeyCode.F8, new KeyCombination.Modifier[0]);
            final KeyCombination escape = new KeyCodeCombination(KeyCode.ESCAPE, new KeyCombination.Modifier[0]);
            final KeyCombination find = new KeyCodeCombination(KeyCode.F, new KeyCombination.Modifier[]{KeyCombination.SHORTCUT_DOWN});

            public void handle(KeyEvent t) {
                if (this.showShiftOps.match(t)) {
                    uiController.showShiftPopOver();
                } else if (this.showPointerInfo.match(t)) {
                    uiController.showPointerPopOver();
                } else if (this.showMemoryAccess.match(t)) {
                    uiController.showMemoryPopOver();
                } else if (this.showStackVis.match(t)) {
                    uiController.showStackPopOver();
                } else if (this.showBranchVis.match(t)) {
                    uiController.showBranchPopOver();
                } else if (this.execute.match(t)) {
                    uiController.executeAllFired(new ActionEvent());
                } else if (this.reset.match(t)) {
                    uiController.resetAllFired(new ActionEvent());
                } else if (this.stepBackwards.match(t)) {
                    uiController.stepBackwardFired(new ActionEvent());
                } else if (this.stepForwards.match(t)) {
                    uiController.stepForwardFired(new ActionEvent());
                } else if (this.escape.match(t)) {
                    uiController.hideErrorListPopOver();
                } else if (this.find.match(t)) {
                    if (!uiController.getChildren().contains(findAndReplacePane)) {
                        uiController.getChildren().add(2, findAndReplacePane);
                        findAndReplacePane.focusFindTextField();
                    } else {
                        VisUAL.removeFindAndReplacePane();
                    }
                } else if (this.showHelp.match(t)) {
                    VisUAL.openWebpageInBrowser("http://salmanarif.bitbucket.org/visual/supported_instructions.html");
                } else if (this.showMemoryMap.match(t)) {
                    VisUAL.openWebpageInBrowser("http://salmanarif.bitbucket.org/visual/memory_map.html");
                }
            }
        });
        swingNode.setOnKeyReleased((EventHandler)new EventHandler<KeyEvent>(){
            final KeyCodeCombination breakpoint = new KeyCodeCombination(KeyCode.B, new KeyCombination.Modifier[]{KeyCombination.SHORTCUT_DOWN});
            final KeyCodeCombination upKey = new KeyCodeCombination(KeyCode.UP, new KeyCombination.Modifier[0]);
            final KeyCodeCombination downKey = new KeyCodeCombination(KeyCode.DOWN, new KeyCombination.Modifier[0]);
            final KeyCodeCombination leftKey = new KeyCodeCombination(KeyCode.LEFT, new KeyCombination.Modifier[0]);
            final KeyCodeCombination rightKey = new KeyCodeCombination(KeyCode.RIGHT, new KeyCombination.Modifier[0]);
            final KeyCodeCombination aKey = new KeyCodeCombination(KeyCode.A, new KeyCombination.Modifier[0]);
            final KeyCodeCombination bKey = new KeyCodeCombination(KeyCode.B, new KeyCombination.Modifier[0]);
            final KeyCodeCombination tooltips = new KeyCodeCombination(KeyCode.SPACE, new KeyCombination.Modifier[]{KeyCombination.CONTROL_DOWN});
            String easterCombo = "";

            public void handle(KeyEvent t) {
                uiController.updateFileSavedStatus();
                if (this.breakpoint.match(t)) {
                    int lineNumber = textArea.getCaretLineNumber();
                    if (uiController.instructionExistsOnLine(lineNumber)) {
                        if (uiController.breakpointExists(lineNumber)) {
                            VisUAL.removeBreakpoint(uiController.getBreakpointIndicator(lineNumber));
                        } else {
                            GutterIconInfo info = VisUAL.addBreakpoint(textArea.getCaretLineNumber());
                            uiController.addBreakpointIndicator(info);
                        }
                    }
                } else if (this.tooltips.match(t)) {
                    VisUAL.showToolTip();
                } else {
                    this.easterCombo = this.upKey.match(t) ? (this.easterCombo.equals("U") || this.easterCombo.isEmpty() ? this.easterCombo + "U" : "U") : (this.downKey.match(t) ? (this.easterCombo.matches("UU|UUD") ? this.easterCombo + "D" : "") : (this.leftKey.match(t) ? (this.easterCombo.matches("UUDD|UUDDLR") ? this.easterCombo + "L" : "") : (this.rightKey.match(t) ? (this.easterCombo.matches("UUDDL|UUDDLRL") ? this.easterCombo + "R" : "") : (this.bKey.match(t) ? (this.easterCombo.matches("UUDDLRLR") ? this.easterCombo + "B" : "") : (this.aKey.match(t) ? (this.easterCombo.matches("UUDDLRLRB") ? this.easterCombo + "A" : "") : "")))));
                }
                if (this.easterCombo.matches("UUDDLRLRBA")) {
                    Button stepForwards = (Button)scene.lookup("#forwards-button");
                    if (easterEgg.getImage() == null) {
                        easterEgg.setImage(frameZero);
                        easterEgg.setTranslateX(-codeStackPane.getWidth() / 2.0 + easterEgg.getFitWidth() / 2.0 + 100.0);
                        easterEggIndex = 0;
                        stepForwards.setText("NYAN");
                    } else {
                        easterEgg.setImage(null);
                        stepForwards.setText("Step Forwards");
                    }
                }
            }
        });
        hoverCanvas.widthProperty().bind((ObservableValue)codeStackPane.widthProperty());
        hoverCanvas.heightProperty().set((double)lineHeight);
        hoverCanvas.setHidden(true, true);
        currentLineCanvas.widthProperty().bind((ObservableValue)codeStackPane.widthProperty());
        currentLineCanvas.heightProperty().set((double)lineHeight);
        currentLineCanvas.setHidden(true, true);
        branchDestCanvas.widthProperty().bind((ObservableValue)codeStackPane.widthProperty());
        branchDestCanvas.heightProperty().set((double)lineHeight);
        branchDestCanvas.setHidden(true, true);
        SimpleDoubleProperty arrowOffsetProperty = new SimpleDoubleProperty();
        arrowOffsetProperty.set(codeStackPane.widthProperty().doubleValue() / 2.0 - 35.0);
        codeStackPane.widthProperty().addListener((arg_0, arg_1, arg_2) -> VisUAL.lambda$start$73((DoubleProperty)arrowOffsetProperty, arg_0, arg_1, arg_2));
        branchArrowCanvas.widthProperty().set((double)lineHeight);
        branchArrowCanvas.translateXProperty().bind((ObservableValue)arrowOffsetProperty);
        branchArrowCanvas.setHidden(true, true);
        linkRegisterCanvas.setColour(VisUAL.getCurrentTheme().getLinkHighColour(), false);
        linkRegisterCanvas.widthProperty().bind((ObservableValue)codeStackPane.widthProperty());
        linkRegisterCanvas.heightProperty().set((double)lineHeight);
        linkRegisterCanvas.setHidden(true, true);
        AdjustmentListener adjustmentListener = adjustmentEvent -> Platform.runLater(() -> {
            CoverageMarker cm;
            currentLineCanvas.reposition(adjustmentEvent.getValue());
            branchDestCanvas.reposition(adjustmentEvent.getValue());
            branchArrowCanvas.reposition(adjustmentEvent.getValue());
            linkRegisterCanvas.reposition(adjustmentEvent.getValue());
            if (tooltipPopOver.isShowing()) {
                tooltipPopOver.hide();
            }
            for (CodeCanvas c : breakpointCanvases) {
                if (c == null) break;
                c.reposition(adjustmentEvent.getValue());
            }
            for (CodeCanvas c : syntaxErrorCanvases) {
                if (c == null) break;
                c.reposition(adjustmentEvent.getValue());
            }
            Iterator<CoverageMarker> iterator = coverageMarkers.iterator();
            while (iterator.hasNext() && (cm = (CoverageMarker)iterator.next()) != null) {
                cm.reposition();
            }
            this.scrollHoverHighlighter(adjustmentEvent.getValue());
            oldScrollPosition = adjustmentEvent.getValue();
        });
        codeScrollPane.getVerticalScrollBar().addAdjustmentListener(adjustmentListener);
        codeScrollPane.getViewport().setScrollMode(0);
        VisUAL.addMouseClickListener();
        swingNode.setOnMouseMoved(event -> {
            this.addHoverHighlighter((MouseEvent)event);
            event.consume();
        });
        swingNode.setOnMouseExited(event -> {
            this.removeHoverHighlighter();
            event.consume();
        });
        swingNode.setOnMouseDragged(event -> {
            this.removeHoverHighlighter();
            event.consume();
        });
        errorPopOver.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (errorPopOver.isShowing()) {
                errorPopOver.hide();
            }
            event.consume();
        });
        double[] oldDimensions = new double[]{0.0, 0.0};
        Timeline invalidateRsta = new Timeline(new KeyFrame[]{new KeyFrame(Duration.seconds((double)0.1), event -> {
            double[] newDimensions = new double[]{codeStackPane.getWidth(), codeStackPane.getHeight()};
            if (newDimensions[0] == oldDimensions[0] && newDimensions[1] == oldDimensions[1]) {
                return;
            }
            swingNode.resize(newDimensions[0], newDimensions[1]);
            codeScrollPane.repaint();
            codeScrollPane.getViewport().repaint();
        }, new KeyValue[0])});
        invalidateRsta.setCycleCount(-1);
        invalidateRsta.play();
        Accordion acc = regAcc.getAccordion();
        ScrollPane scrollPane = (ScrollPane)scene.lookup("#infoScrollPane");
        scrollPane.setContent((Node)acc);
        StatusRegBar statusRegBar = regAcc.getStatusRegBar();
        AnchorPane anchorPane = (AnchorPane)scene.lookup("#bottomInfoPane");
        anchorPane.getChildren().add(statusRegBar);
        AnchorPane.setLeftAnchor((Node)statusRegBar, (Double)10.0);
        AnchorPane.setRightAnchor((Node)statusRegBar, (Double)10.0);
        AnchorPane.setTopAnchor((Node)statusRegBar, (Double)36.0);
        AnchorPane.setBottomAnchor((Node)statusRegBar, (Double)10.0);
        anchorPane.getChildren().add(clockCycleBar);
        AnchorPane.setLeftAnchor((Node)clockCycleBar, (Double)10.0);
        AnchorPane.setRightAnchor((Node)clockCycleBar, (Double)10.0);
        AnchorPane.setTopAnchor((Node)clockCycleBar, (Double)6.0);
    }

    public static void removeFindAndReplacePane() {
        uiController.getChildren().remove(findAndReplacePane);
        findAndReplacePane.removeSearchResultHighlights();
    }

    public static void main(String[] args) {
        EmulatorLogFile.reconfigureLogging();
        VisUAL.launch((String[])args);
    }

    private static void createSwingContent(SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> {
            AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
            atmf.putMapping("text/myLanguage", "visual.ArmV7TokenMaker");
            textArea.setSyntaxEditingStyle("text/myLanguage");
            textArea.setCodeFoldingEnabled(false);
            textArea.setHighlightCurrentLine(false);
            VisUAL.applyTheme(settingsManager.getThemeName());
            swingNode.setContent((JComponent)codeScrollPane);
        });
    }

    public static RSyntaxTextArea getRSyntaxTextArea() {
        return textArea;
    }

    public static CodeCanvas getCurrentLineCanvas() {
        return currentLineCanvas;
    }

    public static CodeCanvas getBranchDestCanvas() {
        return branchDestCanvas;
    }

    public static CodeCanvas getBranchArrowCanvas() {
        return branchArrowCanvas;
    }

    public static CodeCanvas getLinkRegisterCanvas() {
        return linkRegisterCanvas;
    }

    public static Emulator getEmulator() {
        return emulator;
    }

    public static RTextScrollPane getCodeScrollPane() {
        return codeScrollPane;
    }

    public static StackPane getCodeStackPane() {
        return codeStackPane;
    }

    public static RegAccordion getRegAccordion() {
        return regAcc;
    }

    public static ClockCycleBar getClockCycleBar() {
        return clockCycleBar;
    }

    private static void applyThemeViaXml(String xmlFileName) {
        try {
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                xmlFileName = xmlFileName + "_consolas";
            } else if (System.getProperty("os.name").contains("nix") || System.getProperty("os.name").contains("nux")) {
                xmlFileName = xmlFileName + "_ubuntu";
            }
            System.out.println(System.getProperty("os.name"));
            Theme theme = Theme.load(VisUAL.class.getResourceAsStream("/"+xmlFileName + ".xml"));
            theme.apply(textArea);
            int fontSize = settingsManager.getFontSize();
            VisUAL.setFontSize(fontSize);
            VisUAL.updateLineHeight();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void setFontSize(int fontSize) {
        Font newFont = textArea.getFont().deriveFont((float)fontSize);
        textArea.setFont(newFont);
        Font newGutterFont = codeScrollPane.getGutter().getFont().deriveFont((float)fontSize);
        codeScrollPane.getGutter().setFont(newGutterFont);
    }

    private void addHoverHighlighter(MouseEvent event) {
        double lineYPos = (double)((int)((event.getY() + (double)(codeScrollPane.getVerticalScrollBar().getValue() % lineHeight)) / (double)lineHeight) * lineHeight) - codeStackPane.getHeight() / 2.0 + (double)(lineHeight / 2) - (double)(codeScrollPane.getVerticalScrollBar().getValue() % lineHeight);
        hoverCanvas.setColour(VisUAL.getCurrentTheme().getHoverHighColour(), false);
        hoverCanvas.setHidden(false, true);
        hoverCanvas.setTranslateY(lineYPos);
    }

    private void scrollHoverHighlighter(double newScrollPosition) {
        hoverCanvas.setTranslateY(hoverCanvas.getTranslateY() + oldScrollPosition % (double)lineHeight - newScrollPosition % (double)lineHeight);
    }

    private void removeHoverHighlighter() {
        hoverCanvas.setHidden(true, true);
    }

    private static void applyDarkThemeToEditor() {
        codeScrollPane.setBackground(Color.BLACK);
        codeScrollPane.getGutter().setIconRowHeaderInheritsGutterBackground(true);
        codeScrollPane.getGutter().setBackground(Color.BLACK);
        codeScrollPane.setComponentZOrder(codeScrollPane.getVerticalScrollBar(), 0);
        codeScrollPane.setComponentZOrder(codeScrollPane.getHorizontalScrollBar(), 1);
        codeScrollPane.setComponentZOrder(codeScrollPane.getViewport(), 2);
        codeScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        codeScrollPane.getVerticalScrollBar().setOpaque(false);
        codeScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            private final Dimension d = new Dimension();

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = null;
                JScrollBar sb = (JScrollBar)c;
                if (!sb.isEnabled() || r.width > r.height) {
                    return;
                }
                color = this.isDragging ? Color.decode(darkTheme.getNormHighColour()) : (this.isThumbRollover() ? Color.decode(darkTheme.getNormHighColour()) : Color.decode(darkTheme.getHoverHighColour()));
                g2.setPaint(color);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.setPaint(Color.BLACK);
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                this.scrollbar.repaint();
            }
        });
        codeScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        codeScrollPane.getHorizontalScrollBar().setOpaque(false);
        codeScrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI(){
            private final Dimension d = new Dimension();

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = null;
                JScrollBar sb = (JScrollBar)c;
                if (!sb.isEnabled() || r.height > r.width) {
                    return;
                }
                color = this.isDragging ? new Color(128, 113, 70, 255) : (this.isThumbRollover() ? new Color(128, 113, 70, 255) : new Color(83, 83, 83, 255));
                g2.setPaint(color);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.setPaint(Color.BLACK);
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                this.scrollbar.repaint();
            }
        });
    }

    private static void applyLightThemeToEditor() {
        codeScrollPane.setBackground(Color.WHITE);
        codeScrollPane.getGutter().setIconRowHeaderInheritsGutterBackground(true);
        codeScrollPane.getGutter().setBackground(Color.decode("#F4F4F4"));
        codeScrollPane.setComponentZOrder(codeScrollPane.getVerticalScrollBar(), 0);
        codeScrollPane.setComponentZOrder(codeScrollPane.getHorizontalScrollBar(), 1);
        codeScrollPane.setComponentZOrder(codeScrollPane.getViewport(), 2);
        codeScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        codeScrollPane.getVerticalScrollBar().setOpaque(false);
        codeScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            private final Dimension d = new Dimension();

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = null;
                JScrollBar sb = (JScrollBar)c;
                if (!sb.isEnabled() || r.width > r.height) {
                    return;
                }
                color = this.isDragging ? Color.decode(lightTheme.getNormHighColour()) : (this.isThumbRollover() ? Color.decode(lightTheme.getNormHighColour()) : Color.decode(lightTheme.getHoverHighColour()));
                g2.setPaint(color);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.setPaint(Color.WHITE);
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                this.scrollbar.repaint();
            }
        });
        codeScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        codeScrollPane.getHorizontalScrollBar().setOpaque(false);
        codeScrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI(){
            private final Dimension d = new Dimension();

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = null;
                JScrollBar sb = (JScrollBar)c;
                if (!sb.isEnabled() || r.height > r.width) {
                    return;
                }
                color = this.isDragging ? Color.decode(lightTheme.getNormHighColour()) : (this.isThumbRollover() ? Color.decode(lightTheme.getNormHighColour()) : Color.decode(lightTheme.getHoverHighColour()));
                g2.setPaint(color);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.setPaint(Color.WHITE);
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                this.scrollbar.repaint();
            }
        });
    }

    private static void applySolarizedDarkThemeToEditor() {
        codeScrollPane.setBackground(Color.BLACK);
        codeScrollPane.getGutter().setIconRowHeaderInheritsGutterBackground(true);
        codeScrollPane.getGutter().setBackground(Color.BLACK);
        codeScrollPane.setComponentZOrder(codeScrollPane.getVerticalScrollBar(), 0);
        codeScrollPane.setComponentZOrder(codeScrollPane.getHorizontalScrollBar(), 1);
        codeScrollPane.setComponentZOrder(codeScrollPane.getViewport(), 2);
        codeScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        codeScrollPane.getVerticalScrollBar().setOpaque(false);
        codeScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            private final Dimension d = new Dimension();

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = null;
                JScrollBar sb = (JScrollBar)c;
                if (!sb.isEnabled() || r.width > r.height) {
                    return;
                }
                color = this.isDragging ? Color.decode(solarizedDarkTheme.getNormHighColour()) : (this.isThumbRollover() ? Color.decode(solarizedDarkTheme.getNormHighColour()) : Color.decode(solarizedDarkTheme.getHoverHighColour()));
                g2.setPaint(color);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.setPaint(Color.BLACK);
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                this.scrollbar.repaint();
            }
        });
        codeScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        codeScrollPane.getHorizontalScrollBar().setOpaque(false);
        codeScrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI(){
            private final Dimension d = new Dimension();

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = null;
                JScrollBar sb = (JScrollBar)c;
                if (!sb.isEnabled() || r.height > r.width) {
                    return;
                }
                color = this.isDragging ? new Color(128, 113, 70, 255) : (this.isThumbRollover() ? new Color(128, 113, 70, 255) : new Color(83, 83, 83, 255));
                g2.setPaint(color);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.setPaint(Color.BLACK);
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                this.scrollbar.repaint();
            }
        });
    }

    private static void applySolarizedLightThemeToEditor() {
        codeScrollPane.setBackground(Color.WHITE);
        codeScrollPane.getGutter().setIconRowHeaderInheritsGutterBackground(true);
        codeScrollPane.getGutter().setBackground(Color.WHITE);
        codeScrollPane.setComponentZOrder(codeScrollPane.getVerticalScrollBar(), 0);
        codeScrollPane.setComponentZOrder(codeScrollPane.getHorizontalScrollBar(), 1);
        codeScrollPane.setComponentZOrder(codeScrollPane.getViewport(), 2);
        codeScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        codeScrollPane.getVerticalScrollBar().setOpaque(false);
        codeScrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            private final Dimension d = new Dimension();

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = null;
                JScrollBar sb = (JScrollBar)c;
                if (!sb.isEnabled() || r.width > r.height) {
                    return;
                }
                color = this.isDragging ? Color.decode(solarizedLightTheme.getNormHighColour()) : (this.isThumbRollover() ? Color.decode(solarizedLightTheme.getNormHighColour()) : Color.decode(solarizedLightTheme.getHoverHighColour()));
                g2.setPaint(color);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.setPaint(Color.WHITE);
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                this.scrollbar.repaint();
            }
        });
        codeScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        codeScrollPane.getHorizontalScrollBar().setOpaque(false);
        codeScrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI(){
            private final Dimension d = new Dimension();

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton(){

                    @Override
                    public Dimension getPreferredSize() {
                        return d;
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = null;
                JScrollBar sb = (JScrollBar)c;
                if (!sb.isEnabled() || r.height > r.width) {
                    return;
                }
                color = this.isDragging ? Color.decode(solarizedLightTheme.getNormHighColour()) : (this.isThumbRollover() ? Color.decode(solarizedLightTheme.getNormHighColour()) : Color.decode(solarizedLightTheme.getHoverHighColour()));
                g2.setPaint(color);
                g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.setPaint(Color.WHITE);
                g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                this.scrollbar.repaint();
            }
        });
    }

    public static GutterIconInfo addBreakpoint(int lineNumber) {
        GutterIconInfo info = null;
        try {
            info = codeScrollPane.getGutter().addLineTrackingIcon(lineNumber, breakpoint, "Breakpoint. Click to remove.");
            CodeCanvas canvas = new CodeCanvas(CanvasType.HIGHLIGHT);
            canvas.setLineNumber(lineNumber, true);
            canvas.setLineHeight(lineHeight);
            canvas.widthProperty().bind((ObservableValue)codeStackPane.widthProperty());
            canvas.heightProperty().set((double)lineHeight);
            String colour = VisUAL.getCurrentTheme().getFexecHighColour();
            canvas.setColour(colour, true);
            canvas.reposition(codeScrollPane.getVerticalScrollBar().getValue());
            canvas.setHidden(false, true);
            breakpointCanvases.add(canvas);
            codeStackPane.getChildren().add(2, canvas);
        }
        catch (BadLocationException ex) {
            Logger.getLogger(VisUAL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return info;
    }

    public static GutterIconInfo addSyntaxError(int lineNumber) {
        GutterIconInfo info = null;
        try {
            if (uiController.breakpointExists(lineNumber)) {
                codeScrollPane.getGutter().removeTrackingIcon(uiController.getBreakpointIndicator(lineNumber));
                List<GutterIconInfo> bpInfoList = uiController.getBpInfoList();
                for (GutterIconInfo i : bpInfoList) {
                    if (textArea.getLineOfOffset(i.getMarkedOffset()) != lineNumber) continue;
                    pendingBreakpointsIconInfo.add(i);
                    break;
                }
                info = codeScrollPane.getGutter().addLineTrackingIcon(lineNumber, syntaxErrorWithBreakpoint, "Syntax error. Click to view details.");
            } else {
                info = codeScrollPane.getGutter().addLineTrackingIcon(lineNumber, syntaxError, "Syntax error. Click to view details.");
            }
            CodeCanvas canvas = new CodeCanvas(CanvasType.HIGHLIGHT);
            canvas.setLineNumber(lineNumber, true);
            canvas.setLineHeight(lineHeight);
            canvas.widthProperty().bind((ObservableValue)codeStackPane.widthProperty());
            canvas.heightProperty().set((double)lineHeight);
            canvas.setColour(VisUAL.getCurrentTheme().getNormHighColour(), true);
            canvas.reposition(codeScrollPane.getVerticalScrollBar().getValue());
            canvas.setHidden(false, true);
            syntaxErrorCanvases.add(canvas);
            codeStackPane.getChildren().add(2, canvas);
        }
        catch (BadLocationException ex) {
            Logger.getLogger(VisUAL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return info;
    }

    public static void addLinkRegisterIcon(int lineNumber) {
        GutterIconInfo info = null;
        try {
            info = codeScrollPane.getGutter().addLineTrackingIcon(lineNumber, linkRegister);
        }
        catch (BadLocationException ex) {
            Logger.getLogger(VisUAL.class.getName()).log(Level.SEVERE, null, ex);
        }
        linkRegisterIconInfo = info;
    }

    public static void removeLinkRegisterIcon() {
        codeScrollPane.getGutter().removeTrackingIcon(linkRegisterIconInfo);
    }

    public static void removeRuntimeErrorIcon() {
        codeScrollPane.getGutter().removeTrackingIcon(runtimeErrorIconInfo);
    }

    private static void showToolTip() {
        Platform.runLater(() -> {
            if (tooltipPopOver.isShowing()) {
                tooltipPopOver.hide(Duration.millis((double)0.0));
                return;
            }
            String opcode = VisUAL.getOpcodeForLine(textArea.getCaretLineNumber());
            if (!opcode.isEmpty()) {
                if (uiController.isAbort() && !errorPopOver.isShowing()) {
                    return;
                }
                int lineNumber = errorPopOver.isShowing() && uiController.isAbort() ? syntaxErrorLineNumber : textArea.getCaretLineNumber();
                TooltipPopOverPane tooltip = new TooltipPopOverPane(opcode);
                tooltipPopOver = new PopOver();
                tooltipPopOver.setContentNode((Node)tooltip);
                tooltipPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
                tooltipPopOver.setArrowSize(0.0);
                tooltipPopOver.setHideOnEscape(false);
                tooltipPopOver.show((Node)swingNode);
                double yPos = lineNumber * lineHeight - codeScrollPane.getVerticalScrollBar().getValue() + lineHeight / 2;
                tooltipPopOver.setAnchorX(codeScrollPane.getLocationOnScreen().getX() + 20.0);
                tooltipPopOver.setAnchorY(codeScrollPane.getLocationOnScreen().getY() + yPos);
                primaryStage.requestFocus();
            }
        });
    }

    private static void addMouseClickListener() {
        ContextMenu gutterContextMenu = new ContextMenu();
        MenuItem clearBreakpoints = new MenuItem("Clear all breakpoints");
        clearBreakpoints.setOnAction(event -> {
            uiController.clearBreakpoints();
            codeStackPane.getChildren().removeAll(breakpointCanvases);
            breakpointCanvases.clear();
        });
        gutterContextMenu.getItems().add(clearBreakpoints);
        EventHandler<MouseEvent> clickHandler = event -> {
            swingNode.requestFocus();
            gutterContextMenu.hide();
            boolean doNotReset = true;
            if (tooltipPopOver.isShowing()) {
                tooltipPopOver.hide();
            }
            if (uiController.isAbort()) {
                List<SyntaxError> syntaxErrors = uiController.getSyntaxErrors();
                swingNode.requestFocus();
                boolean lineHasError = false;
                double yPos = 0.0;
                for (SyntaxError s : syntaxErrors) {
                    if (s == null) break;
                    if (!VisUAL.mouseOnLine(s.getLineNumber(), event.getY())) continue;
                    lineHasError = true;
                    syntaxErrorLineNumber = s.getLineNumber();
                    errorPopOver.setContentNode((Node)new ErrorPopOverPane("Syntax Error", s.getMessage(), s.getDetails(), false));
                    errorPopOver.setDetachable(false);
                    errorPopOver.setArrowSize(8.0);
                    errorPopOver.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
                    yPos = s.getLineNumber() * lineHeight - codeScrollPane.getVerticalScrollBar().getValue() + lineHeight / 2;
                    AdjustmentListener adjustmentListener1 = adjustmentEvent -> Platform.runLater(errorPopOver::hide);
                    codeScrollPane.getVerticalScrollBar().addAdjustmentListener(adjustmentListener1);
                    break;
                }
                if (lineHasError && !errorPopOver.isShowing()) {
                    errorPopOver.show((Node)codeStackPane);
                    errorPopOver.setAnchorX(codeScrollPane.getLocationOnScreen().getX() + 20.0);
                    errorPopOver.setAnchorY(codeScrollPane.getLocationOnScreen().getY() + yPos);
                    errorPopOver.requestFocus();
                } else {
                    errorPopOver.hide();
                }
                if (hasRuntimeError && VisUAL.mouseOnLine(runtimeErrorLineNumber, event.getY()) && !runtimePopOver.isShowing()) {
                    yPos = runtimeErrorLineNumber * lineHeight - codeScrollPane.getVerticalScrollBar().getValue() + lineHeight / 2;
                    runtimePopOver.show((Node)codeStackPane);
                    runtimePopOver.setAnchorX(codeScrollPane.getLocationOnScreen().getX() + 20.0);
                    runtimePopOver.setAnchorY(codeScrollPane.getLocationOnScreen().getY() + yPos);
                    runtimePopOver.requestFocus();
                } else {
                    runtimePopOver.hide();
                }
            } else if (event.getX() < 40.0) {
                doNotReset = true;
                if (event.isPrimaryButtonDown()) {
                    int lineNumber = (int)(event.getY() + (double)codeScrollPane.getVerticalScrollBar().getValue()) / lineHeight;
                    if (lineNumber < textArea.getLineCount() && uiController.instructionExistsOnLine(lineNumber)) {
                        if (uiController.breakpointExists(lineNumber)) {
                            VisUAL.removeBreakpoint(uiController.getBreakpointIndicator(lineNumber));
                        } else {
                            GutterIconInfo info = VisUAL.addBreakpoint(lineNumber);
                            uiController.addBreakpointIndicator(info);
                        }
                    }
                } else if (event.isSecondaryButtonDown()) {
                    double position = event.getScreenY() < Screen.getPrimary().getBounds().getMaxY() - 40.0 ? event.getY() : event.getY() - 40.0;
                    gutterContextMenu.show((Node)swingNode, Side.LEFT, 160.0, position);
                }
            } else if (!textArea.isEditable()) {
                doNotReset = false;
            }
            if (!doNotReset && settingsManager.getWarnResetToEdit()) {
                Alert resumeEditing = new Alert(Alert.AlertType.CONFIRMATION);
                resumeEditing.setTitle("Reset to continue editing?");
                resumeEditing.setContentText("You cannot edit code while the emulator is running. Please reset to continue editing.");
                resumeEditing.setHeaderText("Cannot edit code while emulator is running.");
                ButtonType doNotWarn = new ButtonType("Do not ask again");
                resumeEditing.getButtonTypes().add(doNotWarn);
                resumeEditing.getButtonTypes().remove(ButtonType.CANCEL);
                Optional result = resumeEditing.showAndWait();
                if (result.isPresent() && ((ButtonType)result.get()).equals(doNotWarn)) {
                    settingsManager.setWarnResetToEdit(false);
                }
            }
            event.consume();
        };
        swingNode.setOnMousePressed(clickHandler);
    }

    public static void addRuntimeError(int lineNumber, String message, String details) {
        try {
            uiController.setAbort(true);
            hasRuntimeError = true;
            runtimeErrorLineNumber = lineNumber;
            if (uiController.breakpointExists(lineNumber)) {
                codeScrollPane.getGutter().removeTrackingIcon(uiController.getBreakpointIndicator(lineNumber));
                List<GutterIconInfo> bpInfoList = uiController.getBpInfoList();
                for (GutterIconInfo i : bpInfoList) {
                    if (textArea.getLineOfOffset(i.getMarkedOffset()) != lineNumber) continue;
                    pendingBreakpointsIconInfo.add(i);
                    break;
                }
                runtimeErrorIconInfo = codeScrollPane.getGutter().addLineTrackingIcon(lineNumber, syntaxErrorWithBreakpoint);
            } else {
                runtimeErrorIconInfo = codeScrollPane.getGutter().addLineTrackingIcon(lineNumber, syntaxError);
            }
            runtimePopOver.setContentNode((Node)new ErrorPopOverPane("Runtime Error", message, details, true));
            runtimePopOver.setDetachable(false);
            runtimePopOver.setArrowSize(8.0);
            runtimePopOver.setArrowLocation(PopOver.ArrowLocation.TOP_LEFT);
            runtimePopOver.show((Node)codeStackPane);
            currentLineCanvas.setLineNumber(lineNumber, false);
            double yPos = lineNumber * lineHeight - codeScrollPane.getVerticalScrollBar().getValue() + lineHeight / 2;
            runtimePopOver.setAnchorX(codeScrollPane.getLocationOnScreen().getX() + 20.0);
            runtimePopOver.setAnchorY(codeScrollPane.getLocationOnScreen().getY() + yPos);
            AdjustmentListener adjustmentListener = adjustmentEvent -> Platform.runLater(runtimePopOver::hide);
            codeScrollPane.getVerticalScrollBar().addAdjustmentListener(adjustmentListener);
            VisUAL.disableEditor(true);
        }
        catch (BadLocationException ex) {
            Logger.getLogger(VisUAL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean getHasRuntimeError() {
        return hasRuntimeError;
    }

    public static void removeBreakpoint(GutterIconInfo info) {
        codeScrollPane.getGutter().removeTrackingIcon(info);
        try {
            uiController.removeBreakpointIndicator(textArea.getLineOfOffset(info.getMarkedOffset()));
            for (int i = 0; i < breakpointCanvases.size(); ++i) {
                if (breakpointCanvases.get(i).getLineNumber() != textArea.getLineOfOffset(info.getMarkedOffset())) continue;
                codeStackPane.getChildren().remove(breakpointCanvases.get(i));
                breakpointCanvases.remove(i);
                break;
            }
        }
        catch (BadLocationException e) {
            System.out.println("Invalid offset location for breakpoint.");
        }
    }

    public static void removeAllBreakpoints() {
        codeScrollPane.getGutter().removeAllTrackingIcons();
        codeStackPane.getChildren().removeAll(breakpointCanvases);
        breakpointCanvases.clear();
    }

    public static boolean mouseOnLine(int lineNumber, double y) {
        return (int)(y + (double)codeScrollPane.getVerticalScrollBar().getValue()) / lineHeight == lineNumber;
    }

    public static String getStringForLine(int lineNumber) {
        Token t = textArea.getTokenListForLine(lineNumber);
        String lineString = t.getLexeme();
        t = t.getNextToken();
        while (t.getType() != 0 && t.getType() != 1) {
            lineString = lineString + t.getLexeme();
            t = t.getNextToken();
        }
        return lineString.trim();
    }

    public static String getOpcodeForLine(int lineNumber) {
        if (textArea.getText().isEmpty()) {
            return "";
        }
        Token t = textArea.getTokenListForLine(lineNumber);
        String opcode = "";
        while (t.getType() != 0) {
            if (t.getType() == 6 || t.getType() == 8) {
                return t.getLexeme().toUpperCase();
            }
            t = t.getNextToken();
        }
        return opcode.toUpperCase();
    }

    public static void pauseRepositionBreakpointsTimeline(boolean pause) {
        if (pause) {
            repositionBreakpoints.stop();
        } else {
            repositionBreakpoints.play();
        }
    }

    public static Scene getScene() {
        return scene;
    }

    public static UIController getUIController() {
        return uiController;
    }

    public static SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public static void resetAll() {
        emulator = new Emulator();
        hasRuntimeError = false;
        errorPopOver.hide();
        codeStackPane.getChildren().removeAll(syntaxErrorCanvases);
        syntaxErrorCanvases.clear();
    }

    public static void hidePopOvers() {
        errorPopOver.hide();
        runtimePopOver.hide();
    }

    public static void setCurrentLineCanvas(CodeCanvasState state) {
        currentLineCanvas.restoreCanvas(state);
    }

    public static void setBranchDestCanvas(CodeCanvasState state) {
        branchDestCanvas.restoreCanvas(state);
    }

    public static void setBranchArrowCanvas(CodeCanvasState state) {
        branchArrowCanvas.restoreCanvas(state);
    }

    public static void setLinkRegisterCanvas(CodeCanvasState state) {
        linkRegisterCanvas.restoreCanvas(state);
        VisUAL.removeLinkRegisterIcon();
        if (!linkRegisterCanvas.isHide()) {
            VisUAL.addLinkRegisterIcon(linkRegisterCanvas.getLineNumber());
        }
    }

    public static void updateLineHeight() {
        lineHeight = textArea.getLineHeight();
        uiController.setLineHeight(lineHeight);
        hoverCanvas.heightProperty().set((double)lineHeight);
        branchDestCanvas.heightProperty().set((double)lineHeight);
        currentLineCanvas.heightProperty().set((double)lineHeight);
        branchArrowCanvas.setLineHeight(lineHeight);
        branchArrowCanvas.widthProperty().set((double)lineHeight);
        CoverageMarker.setLineHeight(lineHeight);
    }

    public static void addCoverageMarker(CoverageMarker cm) {
        boolean duplicate = false;
        for (int i = 0; i < coverageMarkers.size(); ++i) {
            if (coverageMarkers.get(i).getLineNumber() != cm.getLineNumber()) continue;
            duplicate = true;
            break;
        }
        if (!duplicate) {
            coverageMarkers.add(0, cm);
            codeStackPane.getChildren().add(codeStackPane.getChildren().size() - 1, cm);
            cm.translateXProperty().bind((ObservableValue)VisUAL.currentLineCanvas.coverageMarkerXOffsetProperty);
            cm.reposition();
        }
    }

    public static void clearCoverageMarkers() {
        uiController.getCodeStackPane().getChildren().removeAll(coverageMarkers);
        coverageMarkers = new ArrayList<CoverageMarker>();
    }

    public static void scrollToLine(int lineNumber) {
        int lineAtTop = codeScrollPane.getVerticalScrollBar().getValue() / lineHeight;
        int lineAtBottom = lineAtTop + (int)codeStackPane.getHeight() / lineHeight - 1;
        if (lineNumber < lineAtTop || lineNumber > lineAtBottom) {
            codeScrollPane.getVerticalScrollBar().setValue(lineNumber * lineHeight - (int)codeStackPane.getHeight() / 2);
        }
    }

    public static void disableEditor(boolean disable) {
        textArea.setEditable(!disable);
    }

    public static void setStageTitle(String title) {
        if (primaryStage != null && !title.isEmpty()) {
            primaryStage.setTitle(title + " - VisUAL");
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static FindAndReplacePane getFindAndReplacePane() {
        return findAndReplacePane;
    }

    public static void goToNextEasterEggFrame() {
        if (easterEgg.getImage() != null) {
            if (easterEggIndex == 0) {
                easterEgg.setImage(frameOne);
            } else if (easterEggIndex == 1) {
                easterEgg.setImage(frameTwo);
            } else if (easterEggIndex == 2) {
                easterEgg.setImage(frameThree);
            } else if (easterEggIndex == 3) {
                easterEgg.setImage(frameZero);
            }
            easterEggIndex = (easterEggIndex + 1) % 4;
        }
    }

    public static void applyTheme(String themeName) {
        switch (themeName) {
            default: {
                VisUAL.applyThemeViaXml("dark");
                VisUAL.applyDarkThemeToEditor();
                codeScrollPane.setBackground(Color.BLACK);
                codeStackPane.setId("textarea-dark");
                swingNode.setBlendMode(BlendMode.SCREEN);
                break;
            }
            case "Light": {
                VisUAL.applyThemeViaXml("light");
                VisUAL.applyLightThemeToEditor();
                codeScrollPane.setBackground(Color.WHITE);
                codeStackPane.setId("textarea-light");
                swingNode.setBlendMode(BlendMode.MULTIPLY);
                uiController.setEditorPaddingId("hbox-editor-padding-light");
                CoverageMarker.setColour(lightTheme.getHoverHighColour());
                break;
            }
            case "Solarized Dark": {
                VisUAL.applyThemeViaXml("solarized_dark");
                VisUAL.applySolarizedDarkThemeToEditor();
                codeScrollPane.setBackground(Color.BLACK);
                codeStackPane.setId("textarea-solarized-dark");
                swingNode.setBlendMode(BlendMode.SCREEN);
                uiController.setEditorPaddingId("hbox-editor-padding-solarized-dark");
                CoverageMarker.setColour(solarizedDarkTheme.getHoverHighColour());
                break;
            }
            case "Solarized Light": {
                VisUAL.applyThemeViaXml("solarized_light");
                VisUAL.applySolarizedLightThemeToEditor();
                codeScrollPane.setBackground(Color.WHITE);
                codeStackPane.setId("textarea-solarized-light");
                swingNode.setBlendMode(BlendMode.MULTIPLY);
                uiController.setEditorPaddingId("hbox-editor-padding-solarized-light");
                CoverageMarker.setColour(solarizedLightTheme.getHoverHighColour());
            }
        }
    }

    public static EditorTheme getCurrentTheme() {
        switch (settingsManager.getThemeName()) {
            default: {
                return darkTheme;
            }
            case "Light": {
                return lightTheme;
            }
            case "Solarized Dark": {
                return solarizedDarkTheme;
            }
            case "Solarized Light": 
        }
        return solarizedLightTheme;
    }

    public static void openWebpageInBrowser(String url) {
        try {
            if (System.getProperty("os.name").contains("nux")) {
                Runtime.getRuntime().exec("xdg-open " + url);
            } else {
                Desktop.getDesktop().browse(new URI(url));
            }
        }
        catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static CycleCounter getCycleCounter() {
        return cycleCounter;
    }

    private static /* synthetic */ void lambda$start$73(DoubleProperty doubleProperty, ObservableValue observable, Number oldValue, Number newValue) {
        doubleProperty.set(newValue.doubleValue() / 2.0 - 35.0);
        if (easterEgg.getImage() != null) {
            easterEgg.setTranslateX(-codeStackPane.getWidth() / 2.0 + easterEgg.getFitWidth() / 2.0 + 100.0);
        }
    }

    static {
        textArea = new RSyntaxTextArea();
        codeScrollPane = new RTextScrollPane(textArea);
        breakpoint = new ImageIcon(VisUAL.class.getResource("/images/breakpoint_16px.png"));
        syntaxError = new ImageIcon(VisUAL.class.getResource("/images/syntax_error_16px.png"));
        syntaxErrorWithBreakpoint = new ImageIcon(VisUAL.class.getResource("/images/syntax_error_breakpoint_16px.png"));
        linkRegister = new ImageIcon(VisUAL.class.getResource("/images/link_16px.png"));
        pendingBreakpointsIconInfo = new ArrayList<GutterIconInfo>();
        tooltipPopOver = new PopOver();
        errorPopOver = new PopOver();
        runtimePopOver = new PopOver();
        hasRuntimeError = false;
        runtimeErrorLineNumber = -1;
        syntaxErrorLineNumber = -1;
        lineHeight = 15;
        clockCycleBar = new ClockCycleBar();
        easterEgg = new ImageView();
        easterEggIndex = 0;
        frameZero = new Image(VisUAL.class.getResourceAsStream("/images/frame0.png"));
        frameOne = new Image(VisUAL.class.getResourceAsStream("/images/frame1.png"));
        frameTwo = new Image(VisUAL.class.getResourceAsStream("/images/frame2.png"));
        frameThree = new Image(VisUAL.class.getResourceAsStream("/images/frame3.png"));
        oldScrollPosition = codeScrollPane.getVerticalScrollBar().getValue();
        cycleCounter = CycleCounter.createInstance(settingsManager.getCycleModel());
    }
}

