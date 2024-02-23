/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Label
 *  javafx.scene.image.ImageView
 *  javafx.scene.layout.HBox
 */
package visual;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class ClockCycleBar
extends HBox {
    @FXML
    private Label lblCurrentCycles;
    @FXML
    private Label lblTotalCycles;
    @FXML
    private ImageView imgClockIcon;
    private int totalCycles = 0;
    private int currentCycles = 0;

    ClockCycleBar() {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/ClockCycleBar.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        try {
            fxmlLoader.load();
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void setCurrentCycles(int value, boolean deferSceneUpdates) {
        this.totalCycles += value;
        this.currentCycles = value;
        if (!deferSceneUpdates) {
            this.lblCurrentCycles.setText("" + value);
            this.lblTotalCycles.setText("" + this.totalCycles);
        }
    }

    public void resetAll(int value) {
        this.totalCycles = value;
        this.lblTotalCycles.setText("" + value);
        this.lblCurrentCycles.setText("0");
    }

    public void applySavedCycles() {
        this.lblCurrentCycles.setText("" + this.currentCycles);
        this.lblTotalCycles.setText("" + this.totalCycles);
    }

    public int getCurrentCycles() {
        return this.currentCycles;
    }

    public int getTotalCycles() {
        return this.totalCycles;
    }
}

