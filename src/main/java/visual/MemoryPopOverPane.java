/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.event.ActionEvent
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.control.Label
 *  javafx.scene.layout.AnchorPane
 *  javafx.scene.layout.VBox
 */
package visual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import visual.AddressType;
import visual.MemWord;
import visual.MemoryDataHBox;
import visual.MemoryInfo;

public class MemoryPopOverPane
extends AnchorPane {
    @FXML
    private Label lblModeRead;
    @FXML
    private Label lblModeWrite;
    @FXML
    private VBox vboxData;
    private List<MemoryDataHBox> wordBoxes = new ArrayList<MemoryDataHBox>();
    private PopOver parentStage;

    MemoryPopOverPane(PopOver parentStage, MemoryInfo info) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/MemoryPopOverPane.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        this.parentStage = parentStage;
        try {
            int i;
            int numWords;
            fxmlLoader.load();
            if (info.isWrite()) {
                this.lblModeRead.setId("memory-mode-read-off");
                this.lblModeWrite.setId("memory-mode-write-on");
            }
            if ((numWords = info.getWords().size()) > 0) {
                int prevWordAddress = -1;
                for (i = 0; i < numWords; ++i) {
                    int currentWordAddress = info.getWords().get(i).getAddress();
                    if (prevWordAddress == currentWordAddress) continue;
                    this.addTableEntry(info.getWords().get(i), info.getNames().get(i), info.getTypes().get(i));
                    prevWordAddress = currentWordAddress;
                }
            }
            if (info.isBytes()) {
                System.out.println("Base byte index: " + info.getBaseByteIndex());
                System.out.println("Actual byte index: " + info.getActualByteIndex());
                int numBytesProcessed = 0;
                for (i = 0; i < this.wordBoxes.size(); ++i) {
                    this.wordBoxes.get(i).setWordStyle("memory-data-hbox-normal");
                    for (int j = 0; j < 4; ++j) {
                        System.out.println("numBytesProcessed: " + numBytesProcessed);
                        if (numBytesProcessed == info.getBaseByteIndex()) {
                            this.wordBoxes.get(i).setByteStyle("memory-data-hbox-byte-base", j);
                        } else if (numBytesProcessed == info.getActualByteIndex()) {
                            this.wordBoxes.get(i).setByteStyle("memory-data-hbox-byte-actual", j);
                        }
                        ++numBytesProcessed;
                    }
                }
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

    public final void addTableEntry(MemWord word, String name, AddressType type) {
        this.wordBoxes.add(new MemoryDataHBox(word, name, type));
        this.vboxData.getChildren().add(this.wordBoxes.get(this.wordBoxes.size() - 1));
    }
}

