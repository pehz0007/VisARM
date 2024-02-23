/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.collections.ObservableList
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.scene.Node
 *  javafx.scene.control.TableView
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.layout.AnchorPane
 *  javafx.util.Duration
 */
package visual;

import java.io.IOException;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import visual.ErrorPopOverEntry;
import visual.ErrorPopOverPane;
import visual.SyntaxError;
import visual.TooltipPopOverPane;
import visual.VisUAL;

public class ErrorListPopOverPane
extends AnchorPane {
    @FXML
    private TableView<ErrorPopOverEntry> tableErrors;
    private PopOver errorDetailPopOver;
    private PopOver tooltipPopOver;

    ErrorListPopOverPane(List<SyntaxError> errors) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/ErrorListPopOverPane.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        this.errorDetailPopOver = new PopOver();
        this.tooltipPopOver = new PopOver();
        try {
            fxmlLoader.load();
            ObservableList entries = this.tableErrors.getItems();
            for (SyntaxError e : errors) {
                if (e == null) break;
                entries.add((Object)new ErrorPopOverEntry(e.getLineNumber(), e.getMessage()));
            }
            this.tableErrors.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                if (this.errorDetailPopOver.isShowing()) {
                    this.errorDetailPopOver.hide(Duration.millis((double)0.0));
                    this.tooltipPopOver.hide(Duration.millis((double)0.0));
                }
                VisUAL.scrollToLine(Integer.parseInt(((ErrorPopOverEntry)entries.get(newValue.intValue())).getLineNumber()));
                ErrorPopOverPane errorPopOverPane = new ErrorPopOverPane("Syntax Error", ((SyntaxError)errors.get(newValue.intValue())).getMessage(), ((SyntaxError)errors.get(newValue.intValue())).getDetails(), false);
                this.errorDetailPopOver.setContentNode((Node)errorPopOverPane);
                this.errorDetailPopOver.setDetachable(false);
                this.errorDetailPopOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
                this.tooltipPopOver.setAutoFix(true);
                this.errorDetailPopOver.show((Node)this);
                this.errorDetailPopOver.addEventHandler(KeyEvent.ANY, event -> {
                    if (event.getEventType() == KeyEvent.KEY_RELEASED && event.isControlDown() && event.getCode() == KeyCode.SPACE) {
                        if (this.tooltipPopOver.isShowing()) {
                            this.tooltipPopOver.hide(Duration.millis((double)0.0));
                            return;
                        }
                        this.tooltipPopOver.setContentNode((Node)new TooltipPopOverPane(VisUAL.getOpcodeForLine(((SyntaxError)errors.get(newValue.intValue())).getLineNumber())));
                        this.tooltipPopOver.setDetachable(false);
                        this.tooltipPopOver.setArrowSize(0.0);
                        this.tooltipPopOver.setHideOnEscape(false);
                        this.tooltipPopOver.setAutoFix(true);
                        this.tooltipPopOver.show((Node)this);
                    }
                });
            });
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void hideDetailPopOver() {
        this.errorDetailPopOver.hide(Duration.millis((double)0.0));
        this.tooltipPopOver.hide(Duration.millis((double)0.0));
    }
}

