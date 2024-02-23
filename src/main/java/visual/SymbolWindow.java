/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.Node
 *  javafx.scene.control.Label
 *  javafx.scene.control.TableView
 *  javafx.scene.layout.HBox
 */
package visual;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class SymbolWindow
extends HBox {
    @FXML
    private TableView<SymbolEntry> tabSymbolEntries;

    SymbolWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(((this)).getClass().getResource("/SymbolWindow.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.tabSymbolEntries.getColumns().forEach(symbolDataEntryTableColumn -> symbolDataEntryTableColumn.setStyle("-fx-font-family: Consolas"));
            }
            this.tabSymbolEntries.setPlaceholder((Node)new Label("No data or code symbols declared"));
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void updateContents(List<LineLabel> lineLabels, List<Symbol> symbols, List<DcdWord> dcdWords, boolean deferSceneUpdates) throws MemoryBank.MemAccessException {
        if (deferSceneUpdates) {
            return;
        }
        this.tabSymbolEntries.getItems().clear();
        for (LineLabel l : lineLabels) {
            String labelContent;
            try {
                labelContent = VisUAL.getStringForLine(l.getLineNo()).split("\\s+", 2)[1];
            }
            catch (IndexOutOfBoundsException e) {
                labelContent = "Label without code.";
            }
            this.tabSymbolEntries.getItems().add(new SymbolEntry(l.getAddress(), l.getId(), labelContent, SymbolType.CODE));
        }
        for (DcdWord w : dcdWords) {
            this.tabSymbolEntries.getItems().add(new SymbolEntry(w.getAddress(), w.getName(), "0x" + Integer.toHexString(VisUAL.getEmulator().memory.getWord(w.getAddress(), false)).toUpperCase(), SymbolType.DATA));
        }
        for (Symbol s : symbols) {
            this.tabSymbolEntries.getItems().add(new SymbolEntry(0, s.getName(), "0x" + Integer.toHexString(s.getValue()).toUpperCase(), SymbolType.CONST));
        }
    }
}

