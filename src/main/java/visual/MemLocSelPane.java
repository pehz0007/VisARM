/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.event.ActionEvent
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Button
 *  javafx.scene.control.ListView
 *  javafx.scene.control.SelectionMode
 *  javafx.scene.control.TextInputDialog
 *  javafx.scene.layout.VBox
 *  javafx.stage.Stage
 */
package visual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MemLocSelPane
extends VBox {
    private Stage stage;
    @FXML
    private ListView lstMemCustom;
    @FXML
    private Button btnAddMemCustom;
    @FXML
    private Button btnRemoveMemCustom;
    @FXML
    private Button btnRemoveAllMemCustom;
    @FXML
    private Button btnCloseMemCustom;
    private List<String> newLocations = new ArrayList<String>();

    MemLocSelPane(Stage stage, String[] savedLocations) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/MemLocSelPane.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        this.stage = stage;
        try {
            fxmlLoader.load();
            this.lstMemCustom.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            for (int i = 0; i < savedLocations.length; ++i) {
                if (savedLocations[i].isEmpty()) continue;
                this.lstMemCustom.getItems().add((Object)savedLocations[i]);
                this.newLocations.add(savedLocations[i]);
            }
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void btnAddMemCustomFired(ActionEvent event) {
        this.stage.setAlwaysOnTop(false);
        this.setDisable(true);
        ObservableList items = this.lstMemCustom.getItems();
        TextInputDialog dialog = new TextInputDialog("0xFFFFFFF0");
        dialog.setTitle("");
        dialog.setHeaderText("Add location to log");
        dialog.setContentText("Enter a word address as a hexadecimal value:\nNote that in addresses must be word-aligned.");
        Optional<String> response = dialog.showAndWait();
        response.ifPresent(result -> {
            if (!result.matches("(?i:0x[a-f0-9]{0,7}(0|4|8|C))")) {
                this.btnAddMemCustomFired(event);
                return;
            }
            String[] split = result.split("0x");
            int padding = 8 - split[1].length();
            String padded = "0x";
            for (int i = 0; i < padding; ++i) {
                padded = padded + "0";
            }
            if (!items.contains((Object)(padded = padded + split[1].toUpperCase()))) {
                items.add((Object)padded);
                this.newLocations.add(padded);
            }
        });
        this.lstMemCustom.setItems(items);
        this.lstMemCustom.getSelectionModel().clearSelection();
        this.lstMemCustom.getSelectionModel().selectLast();
        this.stage.setAlwaysOnTop(true);
        this.setDisable(false);
    }

    @FXML
    private void btnRemoveMemCustomFired(ActionEvent event) {
        ObservableList toRemove = this.lstMemCustom.getSelectionModel().getSelectedItems();
        ObservableList allItems = this.lstMemCustom.getItems();
        this.newLocations.removeAll((Collection<?>)toRemove);
        allItems.removeAll((Collection)toRemove);
    }

    @FXML
    private void btnRemoveAllMemCustomFired(ActionEvent event) {
        this.lstMemCustom.getItems().clear();
        this.newLocations.clear();
    }

    @FXML
    private void btnCloseMemCustomFired(ActionEvent event) {
        this.stage.close();
    }

    public List<String> getNewLocations() {
        return this.newLocations;
    }
}

