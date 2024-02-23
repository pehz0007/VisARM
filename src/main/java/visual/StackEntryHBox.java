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

public class StackEntryHBox
extends HBox {
    @FXML
    private Label lblAddress;
    @FXML
    private Label lblRegister;
    @FXML
    private Label lblValue;

    StackEntryHBox(int address, int registerNumber, int value, AddressType type) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/StackEntryHBox.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.lblAddress.setStyle("-fx-font-family: Consolas");
                this.lblRegister.setStyle("-fx-font-family: Consolas");
                this.lblValue.setStyle("-fx-font-family: Consolas");
            }
            this.lblAddress.setText("0x" + Integer.toHexString(address).toUpperCase());
            if (registerNumber != -1) {
                this.lblRegister.setText("R" + registerNumber);
            } else {
                this.lblRegister.setText("--");
            }
            this.lblValue.setText("0x" + Integer.toHexString(value).toUpperCase());
            switch (type) {
                case BASE: {
                    this.setId("stack-entry-hbox-before");
                    break;
                }
                case NORMAL: {
                    break;
                }
                case ACTUAL: {
                    this.setId("stack-entry-hbox-after");
                }
            }
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}

