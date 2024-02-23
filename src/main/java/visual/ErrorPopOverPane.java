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

public class ErrorPopOverPane
extends VBox {
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblMessage;
    @FXML
    private Label lblDetails;

    ErrorPopOverPane(String title, String message, String details, boolean runtime) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/ErrorPopOverPane.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        try {
            fxmlLoader.load();
            this.lblTitle.setText(title);
            this.lblMessage.setText(message);
            this.lblDetails.setText(details);
            if (runtime) {
                this.lblTitle.setId("error-runtime");
            }
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}

