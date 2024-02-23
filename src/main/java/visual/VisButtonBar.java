/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Button
 *  javafx.scene.layout.HBox
 */
package visual;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class VisButtonBar
extends HBox {
    VisButtonBar() {
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/VisButtonBar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void addButton(Button button) {
        this.getChildren().add(button);
    }
}

