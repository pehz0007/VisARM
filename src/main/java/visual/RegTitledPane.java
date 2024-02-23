/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.Node
 *  javafx.scene.control.Label
 *  javafx.scene.control.TableColumn
 *  javafx.scene.control.TableView
 *  javafx.scene.input.MouseEvent
 *  javafx.scene.layout.VBox
 */
package visual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import visual.Bases;
import visual.EmulatorState;
import visual.RegHistEntry;
import visual.UIController;
import visual.VisUAL;

public class RegTitledPane
extends VBox {
    final int id;
    private static UIController uiController = VisUAL.getUIController();
    private List<Integer> savedStatePointers = new ArrayList<Integer>();
    private List<RegHistEntry> addLater = new ArrayList<RegHistEntry>();
    @FXML
    private TableView<RegHistEntry> historyTable;

    RegTitledPane(int id2) {
        this.id = id2;
        FXMLLoader fxmlLoader = new FXMLLoader(((this)).getClass().getResource("/RegTitledPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            if (VisUAL.getCurrentTheme().getName().equals("Light")) {
                this.getStylesheets().clear();
                this.getStylesheets().add(VisUAL.class.getResource("tableview-light.css").toExternalForm());
            }
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.historyTable.getColumns().forEach(regHistEntryTableColumn -> regHistEntryTableColumn.setStyle("-fx-font-family: Consolas"));
            }
            this.historyTable.setPlaceholder((Node)new Label("No history available for this register"));
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void onMouseClick(MouseEvent event) {
        System.out.println("Restore state: " + ((RegHistEntry)this.historyTable.getItems().get(this.historyTable.getSelectionModel().getSelectedIndex())).getStateNo());
        uiController.restoreEmulator(((RegHistEntry)this.historyTable.getItems().get(this.historyTable.getSelectionModel().getSelectedIndex())).getStateNo());
    }

    public void addTableEntry(int lineNumber, int value, boolean deferSceneUpdates) {
        if (this.savedStatePointers.contains(EmulatorState.getLastStateSaved())) {
            return;
        }
        ObservableList data = this.historyTable.getItems();
        if (!deferSceneUpdates) {
            data.add(new RegHistEntry(lineNumber, value, EmulatorState.getLastStateSaved()));
        } else {
            this.addLater.add(new RegHistEntry(lineNumber, value, EmulatorState.getLastStateSaved()));
        }
        this.savedStatePointers.add(EmulatorState.getLastStateSaved());
    }

    public void deleteEntriesAfterLine(int lineNumber) {
        ObservableList<RegHistEntry> data = this.historyTable.getItems();
        ArrayList<RegHistEntry> toDelete = new ArrayList<RegHistEntry>();
        for (RegHistEntry e : data) {
            if (e == null) {
                return;
            }
            int savedNumber = Integer.parseInt(e.getLineNumber());
            if (savedNumber <= lineNumber) continue;
            toDelete.add(e);
        }
        data.removeAll(toDelete);
    }

    public void setDispFormat(Bases base) {
        RegHistEntry e;
        ObservableList data = this.historyTable.getItems();
        Iterator iterator = data.iterator();
        while (iterator.hasNext() && (e = (RegHistEntry)iterator.next()) != null) {
            e.setDispBase(base);
        }
        ((TableColumn)this.historyTable.getColumns().get(1)).setVisible(false);
        ((TableColumn)this.historyTable.getColumns().get(1)).setVisible(true);
    }

    public void clearSavedStatePointers() {
        this.savedStatePointers.clear();
    }

    public void addDeferredEntries() {
        ObservableList data = this.historyTable.getItems();
        data.addAll(this.addLater);
        this.addLater.clear();
    }
}

