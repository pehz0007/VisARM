/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.event.ActionEvent
 *  javafx.fxml.FXML
 *  javafx.fxml.FXMLLoader
 *  javafx.geometry.Insets
 *  javafx.geometry.Orientation
 *  javafx.scene.Node
 *  javafx.scene.control.Button
 *  javafx.scene.control.CheckBox
 *  javafx.scene.control.Label
 *  javafx.scene.control.TextField
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyEvent
 *  javafx.scene.layout.HBox
 *  javafx.scene.layout.Priority
 *  javafx.scene.layout.VBox
 */
package visual;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;
import visual.VisUAL;

public class FindAndReplacePane
extends VBox {
    private SearchContext searchContext;
    private SearchResult result;
    private RSyntaxTextArea textArea;
    @FXML
    private Label lblStatus;
    @FXML
    private TextField txtFind;
    @FXML
    private TextField txtReplace;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnReplace;
    @FXML
    private Button btnReplaceAll;
    @FXML
    private CheckBox checkMatchCase;
    @FXML
    private CheckBox checkRegex;
    @FXML
    private HBox hboxTop;
    @FXML
    private HBox hboxMiddle;
    @FXML
    private HBox hboxBottom;

    FindAndReplacePane(RSyntaxTextArea textArea) {
        FXMLLoader fxmlLoader = new FXMLLoader(((Object)((Object)this)).getClass().getResource("/FindAndReplacePane.fxml"));
        fxmlLoader.setRoot((Object)this);
        fxmlLoader.setController((Object)this);
        this.textArea = textArea;
        this.searchContext = new SearchContext();
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Win") || System.getProperty("os.name").contains("Mac")) {
                this.txtFind.setStyle("-fx-font-family: Consolas");
                this.txtReplace.setStyle("-fx-font-family: Consolas");
                this.lblStatus.setStyle("-fx-font-family: Consolas");
            }
            this.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
                if (event.getCode().equals((Object)KeyCode.ESCAPE)) {
                    VisUAL.removeFindAndReplacePane();
                }
            });
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void txtFindChanged(ActionEvent event) {
        this.searchForString(this.txtFind.getText(), true, true);
    }

    @FXML
    private void txtReplaceChanged(ActionEvent event) {
        this.replaceString(this.txtFind.getText(), this.txtReplace.getText(), false, true);
    }

    @FXML
    private void btnNextFired(ActionEvent event) {
        this.searchForString(this.txtFind.getText(), true, false);
    }

    @FXML
    private void btnPreviousFired(ActionEvent event) {
        this.searchForString(this.txtFind.getText(), false, false);
    }

    @FXML
    private void checkMatchCaseFired(ActionEvent event) {
        this.searchForString(this.txtFind.getText(), true, true);
    }

    @FXML
    private void checkRegexFired(ActionEvent event) {
        this.searchForString(this.txtFind.getText(), true, true);
    }

    @FXML
    private void btnReplaceFired(ActionEvent event) {
        this.replaceString(this.txtFind.getText(), this.txtReplace.getText(), false, true);
    }

    @FXML
    private void btnReplaceAllFired(ActionEvent event) {
        this.replaceString(this.txtFind.getText(), this.txtReplace.getText(), true, true);
    }

    private void searchForString(String searchString, boolean forward, boolean retry) {
        System.out.println("Searching for: " + searchString);
        this.searchContext.setSearchFor(searchString);
        this.searchContext.setMatchCase(this.checkMatchCase.isSelected());
        this.searchContext.setRegularExpression(this.checkRegex.isSelected());
        this.searchContext.setMarkAll(true);
        this.searchContext.setSearchForward(forward);
        this.result = SearchEngine.find(VisUAL.getRSyntaxTextArea(), this.searchContext);
        if (this.result.wasFound()) {
            this.lblStatus.setText(this.result.getMarkedCount() + " occurrences found");
            this.lblStatus.setId("find-result-found");
        } else if (this.result.getMarkedCount() > 0) {
            this.searchForString(searchString, forward, false);
        } else {
            this.lblStatus.setText("Not found");
            this.lblStatus.setId("find-result-not-found");
            if (retry) {
                this.textArea.setText(this.textArea.getText());
                this.searchForString(searchString, forward, false);
            }
        }
    }

    private void replaceString(String searchString, String replacementString, boolean all, boolean retry) {
        System.out.println("Replacing with: " + replacementString);
        this.searchContext.setReplaceWith(replacementString);
        this.searchContext.setSearchFor(searchString);
        this.searchContext.setMatchCase(this.checkMatchCase.isSelected());
        this.searchContext.setRegularExpression(this.checkRegex.isSelected());
        this.searchContext.setSearchForward(true);
        this.searchContext.setMarkAll(true);
        this.result = SearchEngine.find(VisUAL.getRSyntaxTextArea(), this.searchContext);
        if (this.result.wasFound()) {
            this.lblStatus.setText(this.result.getMarkedCount() + " occurrences found");
            this.lblStatus.setId("find-result-found");
        } else if (this.result.getMarkedCount() > 0) {
            this.replaceString(searchString, replacementString, all, false);
        } else {
            this.lblStatus.setText("Not found");
            this.lblStatus.setId("find-result-not-found");
            if (retry) {
                this.textArea.setText(this.textArea.getText());
                this.replaceString(searchString, replacementString, all, false);
            }
        }
        if (!all) {
            SearchEngine.replace(this.textArea, this.searchContext);
        } else {
            SearchEngine.replaceAll(this.textArea, this.searchContext);
        }
    }

    public void focusFindTextField() {
        this.txtFind.requestFocus();
        this.txtFind.setText("");
        this.lblStatus.setId("find-result-status");
        this.lblStatus.setText("Enter search term");
    }

    public void removeSearchResultHighlights() {
        this.searchForString("", true, true);
        this.txtFind.setText("");
        this.lblStatus.setId("find-result-status");
        this.lblStatus.setText("Enter search term");
    }

    public void changeLayout(Orientation orientation) {
        if (orientation.equals((Object)Orientation.HORIZONTAL)) {
            this.hboxTop.getChildren().removeAll(new Node[]{this.checkMatchCase, this.checkRegex, this.btnNext, this.btnPrevious});
            this.hboxMiddle.getChildren().removeAll(new Node[]{this.btnReplace, this.btnReplaceAll});
            this.hboxMiddle.getChildren().add(this.checkMatchCase);
            this.hboxBottom.getChildren().addAll(new Node[]{this.btnNext, this.btnPrevious, this.btnReplace, this.btnReplaceAll, this.checkRegex});
            HBox.setHgrow((Node)this.btnNext, (Priority)Priority.ALWAYS);
            HBox.setHgrow((Node)this.btnPrevious, (Priority)Priority.ALWAYS);
            HBox.setHgrow((Node)this.btnReplace, (Priority)Priority.ALWAYS);
            HBox.setHgrow((Node)this.btnReplaceAll, (Priority)Priority.ALWAYS);
            this.hboxBottom.setPadding(new Insets(4.0, 0.0, 4.0, 0.0));
            HBox.setMargin((Node)this.btnReplace, (Insets)new Insets(0.0, 8.0, 0.0, 0.0));
        } else {
            this.hboxBottom.getChildren().removeAll(new Node[]{this.btnNext, this.btnPrevious, this.btnReplace, this.btnReplaceAll, this.checkRegex});
            this.hboxMiddle.getChildren().remove((Object)this.checkMatchCase);
            this.hboxTop.getChildren().addAll(new Node[]{this.checkMatchCase, this.checkRegex, this.btnNext, this.btnPrevious});
            this.hboxMiddle.getChildren().addAll(new Node[]{this.btnReplace, this.btnReplaceAll});
            HBox.setHgrow((Node)this.btnNext, (Priority)Priority.NEVER);
            HBox.setHgrow((Node)this.btnPrevious, (Priority)Priority.NEVER);
            HBox.setHgrow((Node)this.btnReplace, (Priority)Priority.NEVER);
            HBox.setHgrow((Node)this.btnReplaceAll, (Priority)Priority.NEVER);
            this.hboxBottom.setPadding(new Insets(0.0, 0.0, 0.0, 0.0));
            HBox.setMargin((Node)this.btnReplace, (Insets)new Insets(0.0, 8.0, 0.0, 8.0));
        }
    }
}

