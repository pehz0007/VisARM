/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.beans.value.ObservableValue
 *  javafx.event.ActionEvent
 *  javafx.event.EventHandler
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.Node
 *  javafx.scene.control.Alert
 *  javafx.scene.control.Alert$AlertType
 *  javafx.scene.control.Button
 *  javafx.scene.control.Label
 *  javafx.scene.control.TableView
 *  javafx.scene.control.TextField
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.VBox
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 *  javafx.scene.shape.Rectangle
 *  javafx.stage.StageStyle
 *  javafx.util.Duration
 */
package visual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import visual.Bases;
import visual.MemWord;
import visual.MemWordEntry;
import visual.MemoryBank;
import visual.MemoryWindowPopOver;
import visual.VisUAL;

public class MemoryWindow
extends HBox {
    @FXML
    private VBox vboxMemoryMap;
    @FXML
    private TableView<MemWordEntry> tabWordEntries;
    @FXML
    private TextField txtStartAddress;
    @FXML
    private TextField txtEndAddress;
    @FXML
    private Button btnDecimal;
    @FXML
    private Button btnHex;
    private MemoryBank memoryBank;
    private List<Rectangle> memoryMapMarkers = new ArrayList<Rectangle>();
    private List<EventHandler> memoryMapMouseMovedEventHandlers = new ArrayList<EventHandler>();
    private List<EventHandler> memoryMapMouseEnteredEventHandlers = new ArrayList<EventHandler>();
    private Bases wordDispFormat = Bases.HEX;

    MemoryWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/MemoryWindow.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.txtStartAddress.setStyle("-fx-font-family: Consolas");
                this.txtEndAddress.setStyle("-fx-font-family: Consolas");
                this.tabWordEntries.getColumns().forEach(memWordEntryTableColumn -> memWordEntryTableColumn.setStyle("-fx-font-family: Consolas"));
            }
            this.tabWordEntries.setPlaceholder((Node)new Label("No declared words in specified region"));
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setMemoryBank(MemoryBank memoryBank) {
        this.memoryBank = memoryBank;
        this.txtStartAddress.setText("0x" + Integer.toHexString(VisUAL.getSettingsManager().getInstMemSize()).toUpperCase());
        int defaultEndAddress = Integer.compareUnsigned(VisUAL.getSettingsManager().getInstMemSize(), -4096) <= 0 ? VisUAL.getSettingsManager().getInstMemSize() + 4096 : -4;
        this.txtEndAddress.setText("0x" + Integer.toHexString(defaultEndAddress).toUpperCase());
    }

    @FXML
    private void txtStartAddressChanged(ActionEvent event) {
        if (this.txtStartAddress.getText().matches("(?i:0x[A-F0-9]{1,8})")) {
            Alert alert;
            int startAddress = (int)Long.parseLong(this.txtStartAddress.getText().substring(2), 16);
            int endAddress = (int)Long.parseLong(this.txtEndAddress.getText().substring(2), 16);
            if (Integer.compareUnsigned(startAddress, VisUAL.getSettingsManager().getInstMemSize()) < 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Invalid start address");
                alert.setHeaderText("Start address is part of instruction memory space.");
                alert.setContentText("Updated start address to first valid address.");
                alert.showAndWait();
                this.txtStartAddress.setText("0x" + Integer.toHexString(VisUAL.getSettingsManager().getInstMemSize()).toUpperCase());
            } else if (Integer.compareUnsigned(startAddress, endAddress) > 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Invalid end address");
                alert.setHeaderText("End address is smaller than the start address.");
                alert.setContentText("Set end address to start address.");
                alert.showAndWait();
                this.txtEndAddress.setText(this.txtStartAddress.getText());
            } else if (Integer.compareUnsigned(endAddress - startAddress, 0x100000) > 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Invalid address range");
                alert.setHeaderText("Address range too large to display.");
                alert.setContentText("Set end address to start address.");
                alert.showAndWait();
                this.txtEndAddress.setText(this.txtStartAddress.getText());
            }
            this.tabWordEntries.getItems().clear();
            List<MemWord> wordsInRange = this.memoryBank.getWordsInRange(Integer.parseInt(this.txtStartAddress.getText().substring(2), 16), Integer.parseInt(this.txtEndAddress.getText().substring(2), 16));
            for (MemWord w : wordsInRange) {
                this.tabWordEntries.getItems().add(new MemWordEntry(w.getAddress(), w.getValue(), this.wordDispFormat));
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Invalid start address format");
            alert.setHeaderText("Start address is not a valid address.");
            alert.setContentText("Please re-enter end address as hexadecimal number.");
            alert.showAndWait();
        }
    }

    @FXML
    private void txtEndAddressChanged(ActionEvent event) {
        if (this.txtEndAddress.getText().matches("(?i:0x[A-F0-9]{1,8})")) {
            Alert alert;
            int startAddress = (int)Long.parseLong(this.txtStartAddress.getText().substring(2), 16);
            int endAddress = (int)Long.parseLong(this.txtEndAddress.getText().substring(2), 16);
            if (Integer.compareUnsigned(startAddress, VisUAL.getSettingsManager().getInstMemSize()) < 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Invalid start address");
                alert.setHeaderText("Start address is part of instruction memory space.");
                alert.setContentText("Updated start address to first valid address.");
                alert.showAndWait();
                this.txtStartAddress.setText("0x" + Integer.toHexString(VisUAL.getSettingsManager().getInstMemSize()).toUpperCase());
            } else if (Integer.compareUnsigned(startAddress, endAddress) > 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Invalid end address");
                alert.setHeaderText("End address is smaller than the start address.");
                alert.setContentText("Set end address to start address.");
                alert.showAndWait();
                this.txtEndAddress.setText(this.txtStartAddress.getText());
            } else if (Integer.compareUnsigned(endAddress - startAddress, 0x100000) > 0) {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Invalid address range");
                alert.setHeaderText("Address range too large to display.");
                alert.setContentText("Set end address to start address.");
                alert.showAndWait();
                this.txtEndAddress.setText(this.txtStartAddress.getText());
            }
            this.tabWordEntries.getItems().clear();
            List<MemWord> wordsInRange = this.memoryBank.getWordsInRange(Integer.parseInt(this.txtStartAddress.getText().substring(2), 16), Integer.parseInt(this.txtEndAddress.getText().substring(2), 16));
            for (MemWord w : wordsInRange) {
                this.tabWordEntries.getItems().add(new MemWordEntry(w.getAddress(), w.getValue(), this.wordDispFormat));
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Invalid end address format");
            alert.setHeaderText("End address is not a valid address.");
            alert.setContentText("Please re-enter end address as hexadecimal number.");
            alert.showAndWait();
        }
    }

    public void updateContents(MemoryBank memoryBank, boolean deferSceneUpdates) {
        if (deferSceneUpdates) {
            return;
        }
        this.memoryBank = memoryBank;
        this.vboxMemoryMap.getChildren().removeAll(this.memoryMapMarkers);
        this.memoryMapMarkers.clear();
        for (EventHandler e : this.memoryMapMouseMovedEventHandlers) {
            this.vboxMemoryMap.removeEventHandler(MouseEvent.MOUSE_MOVED, e);
        }
        this.memoryMapMouseMovedEventHandlers.clear();
        for (EventHandler e : this.memoryMapMouseEnteredEventHandlers) {
            this.tabWordEntries.removeEventHandler(MouseEvent.MOUSE_ENTERED, e);
        }
        this.memoryMapMouseEnteredEventHandlers.clear();
        this.vboxMemoryMap.setStyle("-fx-background-color: white");
        int instMemSize = VisUAL.getSettingsManager().getInstMemSize();
        double instMemHeight = (double)Integer.toUnsignedLong(instMemSize) / (double)Integer.toUnsignedLong(-1) * 300.0;
        if (instMemHeight < 4.0) {
            instMemHeight = 4.0;
        }
        Rectangle instMemRect = new Rectangle();
        instMemRect.setHeight(instMemHeight);
        instMemRect.widthProperty().bind((ObservableValue)this.vboxMemoryMap.widthProperty());
        instMemRect.setFill((Paint)Color.web((String)"FFE290"));
        this.vboxMemoryMap.getChildren().add(instMemRect);
        this.memoryMapMarkers.add(instMemRect);
        List<MemoryWindowPopOver> dataMarkers = memoryBank.getDataMarkers();
        for (int i = 0; i < dataMarkers.size(); ++i) {
            MemoryWindowPopOver m = dataMarkers.get(i);
            int startAddress = m.getStartAddress();
            double dataMarkerPosition = (double)Integer.toUnsignedLong(startAddress) / (double)Integer.toUnsignedLong(-1) * 300.0 - (double)i * 4.0;
            Rectangle dataMarkerRect = new Rectangle();
            dataMarkerRect.setHeight(4.0);
            dataMarkerRect.widthProperty().bind((ObservableValue)this.vboxMemoryMap.widthProperty());
            dataMarkerRect.setFill((Paint)Color.web((String)"0C967A"));
            this.vboxMemoryMap.getChildren().add(dataMarkerRect);
            dataMarkerRect.translateYProperty().set(dataMarkerPosition);
            PopOver dataMarkerPopOver = new PopOver((Node)m);
            dataMarkerPopOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_CENTER);
            dataMarkerPopOver.setDetachable(false);
            EventHandler<MouseEvent> mouseMovedHandler = event -> {
                if (dataMarkerRect.getBoundsInParent().intersects(0.0, event.getY(), this.vboxMemoryMap.getWidth(), 3.9)) {
                    dataMarkerPopOver.show((Node)dataMarkerRect);
                } else {
                    dataMarkerPopOver.hide(Duration.millis((double)0.0));
                }
            };
            this.vboxMemoryMap.addEventHandler(MouseEvent.MOUSE_MOVED, mouseMovedHandler);
            this.memoryMapMouseMovedEventHandlers.add(mouseMovedHandler);
            dataMarkerRect.setOnMouseClicked(event -> {
                this.tabWordEntries.getItems().clear();
                List<MemWord> wordsInRange = memoryBank.getWordsInRange(m.getStartAddress(), m.getEndAddress());
                for (MemWord w : wordsInRange) {
                    this.tabWordEntries.getItems().add(new MemWordEntry(w.getAddress(), w.getValue(), this.wordDispFormat));
                }
                this.txtStartAddress.setText("0x" + Integer.toHexString(m.getStartAddress()).toUpperCase());
                this.txtEndAddress.setText("0x" + Integer.toHexString(m.getEndAddress()).toUpperCase());
                dataMarkerPopOver.hide(Duration.millis((double)0.0));
            });
            EventHandler mouseEnteredHandler = event -> dataMarkerPopOver.hide(Duration.millis((double)0.0));
            this.tabWordEntries.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEnteredHandler);
            this.memoryMapMouseEnteredEventHandlers.add(mouseEnteredHandler);
            this.memoryMapMarkers.add(dataMarkerRect);
        }
        this.txtStartAddressChanged(new ActionEvent());
    }

    @FXML
    private void btnDecimalFired(ActionEvent event) {
        this.wordDispFormat = Bases.DEC;
        this.updateContents(this.memoryBank, false);
        this.btnDecimal.setDisable(true);
        this.btnHex.setDisable(false);
    }

    @FXML
    private void btnHexFired(ActionEvent event) {
        this.wordDispFormat = Bases.HEX;
        this.updateContents(this.memoryBank, false);
        this.btnDecimal.setDisable(false);
        this.btnHex.setDisable(true);
    }
}

