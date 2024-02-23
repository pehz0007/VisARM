/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.event.ActionEvent
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Button
 *  javafx.scene.control.Label
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.VBox
 */
package visual;

import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

public class StackPopOverPane
extends HBox {
    private PopOver parentStage;
    @FXML
    private HBox hboxWriteback;
    @FXML
    private Label lblStackType;
    @FXML
    private Label lblBaseAdr;
    @FXML
    private VBox vboxContents;
    @FXML
    private Label lblAccessMode;
    @FXML
    private Label lblPtrName;
    @FXML
    private Label lblWriteback;
    @FXML
    private Label lblBehaviour;
    @FXML
    private Label lblSummary;
    @FXML
    private VBox vboxEntryPane;
    @FXML
    private Button btnClose;

    StackPopOverPane(PopOver parentStage, StackInfo info) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/StackPopOverPane.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        this.parentStage = parentStage;
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.lblPtrName.setStyle("-fx-font-family: Consolas");
                this.lblBaseAdr.setStyle("-fx-font-family: Consolas");
            }
            ArrayList<StackEntryHBox> entries = new ArrayList<StackEntryHBox>();
            for (int i = 0; i < info.getEntries().size(); ++i) {
                boolean baseFirst;
                AddressType type = AddressType.NORMAL;
                StackType stackType = info.getStackType();
                boolean isStore = info.isStore();
                boolean bl = baseFirst = stackType == StackType.FD && !isStore || stackType == StackType.ED && !isStore || stackType == StackType.FA && isStore || stackType == StackType.EA && isStore;
                if (i == 0) {
                    type = baseFirst ? AddressType.BASE : AddressType.ACTUAL;
                } else if (i == info.getEntries().size() - 1) {
                    type = baseFirst ? AddressType.ACTUAL : AddressType.BASE;
                }
                entries.add(new StackEntryHBox(info.getEntries().get(i).getAddress(), info.getEntries().get(i).getRegNumber(), info.getEntries().get(i).getValue(), type));
            }
            this.vboxEntryPane.getChildren().addAll(entries);
            this.lblPtrName.setText("R" + info.getRegPointer());
            this.lblBaseAdr.setText("0x" + Integer.toHexString(info.getAddress()).toUpperCase());
            switch (info.getStackType()) {
                case FA: {
                    this.lblStackType.setText("Full Ascending");
                    if (info.isStore()) {
                        this.lblBehaviour.setText("Increment Before Store");
                        this.lblSummary.setText("The base address is incremented. Then, registers are stored in ascending order. The lowest-numbered register is stored to the lowest location in memory.");
                        break;
                    }
                    this.lblBehaviour.setText("Decrement After Load");
                    this.lblSummary.setText("Registers are loaded in descending order. The lowest-numbered register is loaded from the lowest location in memory. Then, the base address is decremented.");
                    break;
                }
                case EA: {
                    this.lblStackType.setText("Empty Ascending");
                    if (info.isStore()) {
                        this.lblBehaviour.setText("Increment After Store");
                        this.lblSummary.setText("Registers are stored in ascending order. The lowest-numbered register is stored to the lowest location in memory. Then, the base address is incremented.");
                        break;
                    }
                    this.lblBehaviour.setText("Decrement Before Load");
                    this.lblSummary.setText("The base address is decremented. Then, registers are loaded in descending order. The lowest-numbered register is loaded from the lowest location in memory.");
                    break;
                }
                case FD: {
                    this.lblStackType.setText("Full Descending");
                    if (info.isStore()) {
                        this.lblBehaviour.setText("Decrement Before Store");
                        this.lblSummary.setText("The base address is decremented. Then, registers are stored in descending order. The lowest-numbered register is stored to the lowest location in memory.");
                        break;
                    }
                    this.lblBehaviour.setText("Increment After Load");
                    this.lblSummary.setText("Registers are loaded in ascending order. The lowest-numbered register is loaded from the lowest location in memory. Then, the base address is incremented.");
                    break;
                }
                case ED: {
                    this.lblStackType.setText("Empty Descending");
                    if (info.isStore()) {
                        this.lblBehaviour.setText("Decrement After Store");
                        this.lblSummary.setText("Registers are stored in descending order. The lowest-numbered register is stored to the lowest location in memory. Then, the base address is decremented.");
                        break;
                    }
                    this.lblBehaviour.setText("Increment Before Load");
                    this.lblSummary.setText("The base address is incremented. Then, registers are loaded in ascending order. The lowest-numbered register is loaded from the lowest location in memory.");
                }
            }
            if (info.isStore()) {
                this.lblAccessMode.setText("Store");
            } else {
                this.lblAccessMode.setText("Load");
            }
            if (info.isWriteback()) {
                this.lblWriteback.setText("Write-back enabled");
                this.hboxWriteback.setDisable(false);
            }
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void btnCloseFired(ActionEvent event) {
        this.parentStage.hide();
    }
}

