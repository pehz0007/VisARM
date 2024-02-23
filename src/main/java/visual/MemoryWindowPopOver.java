/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Label
 *  javafx.scene.layout.VBox
 */
package visual;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MemoryWindowPopOver
extends VBox {
    @FXML
    private Label lblStartAddress;
    @FXML
    private Label lblEndAddress;
    @FXML
    private Label lblWordsUsed;
    private int startAddress;
    private int endAddress;

    MemoryWindowPopOver(int startAddress, int endAddress, int wordsUsed) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/MemoryWindowPopOver.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.lblStartAddress.setStyle("-fx-font-family: Consolas");
                this.lblEndAddress.setStyle("-fx-font-family: Consolas");
                this.lblWordsUsed.setStyle("-fx-font-family: Consolas");
            }
            this.lblStartAddress.setText("0x" + Integer.toHexString(startAddress).toUpperCase());
            this.lblEndAddress.setText("0x" + Integer.toHexString(endAddress).toUpperCase());
            this.lblWordsUsed.setText("" + wordsUsed);
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public int getStartAddress() {
        return this.startAddress;
    }

    public int getEndAddress() {
        return this.endAddress;
    }
}

