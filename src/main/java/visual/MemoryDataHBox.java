/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Label
 *  javafx.scene.layout.HBox
 */
package visual;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import visual.AddressType;
import visual.MemWord;

public class MemoryDataHBox
extends HBox {
    @FXML
    private Label lblWordAddress;
    @FXML
    private Label lblByte0;
    @FXML
    private Label lblByte1;
    @FXML
    private Label lblByte2;
    @FXML
    private Label lblByte3;
    @FXML
    private Label lblName;

    MemoryDataHBox(MemWord word, String name, AddressType type) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/MemoryDataHBox.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.lblWordAddress.setStyle("-fx-font-family: Consolas");
                this.lblByte0.setStyle("-fx-font-family: Consolas");
                this.lblByte1.setStyle("-fx-font-family: Consolas");
                this.lblByte2.setStyle("-fx-font-family: Consolas");
                this.lblByte3.setStyle("-fx-font-family: Consolas");
                this.lblName.setStyle("-fx-font-family: Consolas");
            }
            int wordAddress = word.getAddress();
            int value = word.getValue();
            this.lblWordAddress.setText("0x" + Integer.toHexString(wordAddress).toUpperCase());
            this.lblByte0.setText(Integer.toHexString((byte)value & 0xFF).toUpperCase());
            this.lblByte1.setText(Integer.toHexString((byte)(value >>> 8) & 0xFF).toUpperCase());
            this.lblByte2.setText(Integer.toHexString((byte)(value >>> 16) & 0xFF).toUpperCase());
            this.lblByte3.setText(Integer.toHexString((byte)(value >>> 24) & 0xFF).toUpperCase());
            this.lblName.setText(name);
            switch (type) {
                case NORMAL: {
                    this.setId("memory-data-hbox-normal");
                    break;
                }
                case BASE: {
                    this.setId("memory-data-hbox-base");
                    break;
                }
                case ACTUAL: {
                    this.setId("memory-data-hbox-actual");
                    break;
                }
            }
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setWordStyle(String style) {
        this.setId(style);
    }

    public void setByteStyle(String style, int byteIndex) {
        switch (byteIndex) {
            default: {
                this.lblByte0.setId(style);
                break;
            }
            case 1: {
                this.lblByte1.setId(style);
                break;
            }
            case 2: {
                this.lblByte2.setId(style);
                break;
            }
            case 3: {
                this.lblByte3.setId(style);
            }
        }
    }
}

