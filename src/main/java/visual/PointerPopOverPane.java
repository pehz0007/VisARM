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
 *  javafx.scene.text.Text
 *  javafx.scene.text.TextFlow
 */
package visual;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.controlsfx.control.PopOver;
import visual.PointerInfo;

public class PointerPopOverPane
extends VBox {
    @FXML
    private Label lblPtrName;
    @FXML
    private Label lblBaseAdr;
    @FXML
    private Label lblOffset;
    @FXML
    private Label lblActualAdr;
    @FXML
    private Label lblCurrentValue;
    @FXML
    private Label lblIndexing;
    @FXML
    private HBox hbIndexing;
    @FXML
    private TextFlow tfSummary;
    @FXML
    private Button btnClose;
    private boolean changeFont = false;
    private PopOver parentStage;

    PointerPopOverPane(PopOver parentStage, PointerInfo info) {
        FXMLLoader fxmlLoader = new FXMLLoader(((this)).getClass().getResource("/PointerPopOverPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.parentStage = parentStage;
        try {
            fxmlLoader.load();
            if (System.getProperty("os.name").contains("Mac") || System.getProperty("os.name").contains("Win")) {
                this.changeFont = true;
                this.lblBaseAdr.setStyle("-fx-font-family: Consolas");
                this.lblPtrName.setStyle("-fx-font-family: Consolas");
                this.lblOffset.setStyle("-fx-font-family: Consolas");
                this.lblActualAdr.setStyle("-fx-font-family: Consolas");
                this.lblCurrentValue.setStyle("-fx-font-family: Consolas");
            }
            this.lblPtrName.setText(info.getName());
            this.lblBaseAdr.setText("0x" + Integer.toHexString(info.getBaseAddress()).toUpperCase());
            this.lblOffset.setText("0x" + Integer.toHexString(info.getOffset()).toUpperCase());
            this.lblActualAdr.setText("0x" + Integer.toHexString(info.getOffset() + info.getBaseAddress()).toUpperCase());
            this.lblCurrentValue.setText("0x" + Integer.toHexString(info.getCurrentValue()).toUpperCase());
            this.genSummary(info);
            if (info.isPreIndexed()) {
                this.hbIndexing.setDisable(false);
                this.lblIndexing.setDisable(false);
                this.lblIndexing.setText("Pre-index increment enabled");
            } else if (info.isPostIndexed()) {
                this.hbIndexing.setDisable(false);
                this.lblIndexing.setDisable(false);
                this.lblIndexing.setText("Post-index increment enabled");
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

    private void genSummary(PointerInfo info) {
        switch (info.getType()) {
            case LDR: {
                if (!info.isPreIndexed() && !info.isPostIndexed()) {
                    Text t = new Text("This instruction loads the word from ");
                    t.setId("summary-normal");
                    this.tfSummary.getChildren().add(t);
                    if (info.getOffset() == 0) {
                        Text t1 = new Text("the address in ");
                        t1.setId("summary-normal");
                        this.tfSummary.getChildren().add(t1);
                        Text t2 = new Text(info.getName());
                        t2.setId("summary-code");
                        if (this.changeFont) {
                            t2.setStyle("-fx-font-family: Consolas");
                        }
                        this.tfSummary.getChildren().add(t2);
                        Text t3 = new Text(" to ");
                        t3.setId("summary-normal");
                        this.tfSummary.getChildren().add(t3);
                        Text t4 = new Text(info.getDestination());
                        t4.setId("summary-code");
                        if (this.changeFont) {
                            t4.setStyle("-fx-font-family: Consolas");
                        }
                        this.tfSummary.getChildren().add(t4);
                        Text t5 = new Text(".");
                        t5.setId("summary-normal");
                        this.tfSummary.getChildren().add(t5);
                        break;
                    }
                    Text t1 = new Text("the address in ");
                    t1.setId("summary-normal");
                    this.tfSummary.getChildren().add(t1);
                    Text t2 = new Text("(" + info.getName() + " + 0x" + Integer.toHexString(info.getOffset()) + ")");
                    t2.setId("summary-code");
                    if (this.changeFont) {
                        t2.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t2);
                    Text t3 = new Text(" to ");
                    t3.setId("summary-normal");
                    this.tfSummary.getChildren().add(t3);
                    Text t4 = new Text(info.getDestination());
                    t4.setId("summary-code");
                    if (this.changeFont) {
                        t4.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t4);
                    Text t5 = new Text(".");
                    t5.setId("summary-normal");
                    this.tfSummary.getChildren().add(t5);
                    break;
                }
                if (info.isPreIndexed()) {
                    Text t = new Text(info.getName());
                    t.setId("summary-code");
                    if (this.changeFont) {
                        t.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t);
                    Text t1 = new Text(" is set to ");
                    t1.setId("summary-normal");
                    this.tfSummary.getChildren().add(t1);
                    Text t2 = new Text("(" + info.getName() + " + 0x" + Integer.toHexString(info.getOffset()) + ")");
                    t2.setId("summary-code");
                    if (this.changeFont) {
                        t2.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t2);
                    Text t3 = new Text(". Then, this instruction loads the word from the new value of ");
                    t3.setId("summary-normal");
                    this.tfSummary.getChildren().add(t3);
                    Text t4 = new Text(info.getName());
                    t4.setId("summary-code");
                    if (this.changeFont) {
                        t4.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t4);
                    Text t5 = new Text(" to ");
                    t5.setId("summary-normal");
                    this.tfSummary.getChildren().add(t5);
                    Text t6 = new Text(info.getDestination());
                    t6.setId("summary-code");
                    if (this.changeFont) {
                        t6.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t6);
                    Text t7 = new Text(".");
                    t7.setId("summary-normal");
                    this.tfSummary.getChildren().add(t7);
                    break;
                }
                Text t = new Text("This instruction loads the word from ");
                t.setId("summary-normal");
                this.tfSummary.getChildren().add(t);
                Text t1 = new Text("the address in ");
                t1.setId("summary-normal");
                this.tfSummary.getChildren().add(t1);
                Text t2 = new Text(info.getName());
                t2.setId("summary-code");
                if (this.changeFont) {
                    t2.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t2);
                Text t3 = new Text(" to ");
                t3.setId("summary-normal");
                this.tfSummary.getChildren().add(t3);
                Text t4 = new Text(info.getDestination());
                t4.setId("summary-code");
                if (this.changeFont) {
                    t4.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t4);
                Text t5 = new Text(".");
                t5.setId("summary-normal");
                this.tfSummary.getChildren().add(t5);
                Text t6 = new Text(" Then, ");
                t6.setId("summary-normal");
                this.tfSummary.getChildren().add(t6);
                Text t7 = new Text(info.getName());
                t7.setId("summary-code");
                if (this.changeFont) {
                    t7.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t7);
                Text t8 = new Text(" is set to ");
                t8.setId("summary-normal");
                this.tfSummary.getChildren().add(t8);
                Text t9 = new Text("(" + info.getName() + " + 0x" + Integer.toHexString(info.getOffset()) + ")");
                t9.setId("summary-code");
                if (this.changeFont) {
                    t9.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t9);
                Text t10 = new Text(".");
                t10.setId("summary-normal");
                this.tfSummary.getChildren().add(t10);
                break;
            }
            case LDRB: {
                if (!info.isPreIndexed() && !info.isPostIndexed()) {
                    Text t = new Text("This instruction loads the byte from ");
                    t.setId("summary-normal");
                    this.tfSummary.getChildren().add(t);
                    if (info.getOffset() == 0) {
                        Text t1 = new Text("the address in ");
                        t1.setId("summary-normal");
                        this.tfSummary.getChildren().add(t1);
                        Text t2 = new Text(info.getName());
                        t2.setId("summary-code");
                        if (this.changeFont) {
                            t2.setStyle("-fx-font-family: Consolas");
                        }
                        this.tfSummary.getChildren().add(t2);
                        Text t3 = new Text(" to ");
                        t3.setId("summary-normal");
                        this.tfSummary.getChildren().add(t3);
                        Text t4 = new Text(info.getDestination());
                        t4.setId("summary-code");
                        if (this.changeFont) {
                            t4.setStyle("-fx-font-family: Consolas");
                        }
                        this.tfSummary.getChildren().add(t4);
                        Text t5 = new Text(".");
                        t5.setId("summary-normal");
                        this.tfSummary.getChildren().add(t5);
                        break;
                    }
                    Text t1 = new Text("the address in ");
                    t1.setId("summary-normal");
                    this.tfSummary.getChildren().add(t1);
                    Text t2 = new Text("(" + info.getName() + " + 0x" + Integer.toHexString(info.getOffset()) + ")");
                    t2.setId("summary-code");
                    if (this.changeFont) {
                        t2.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t2);
                    Text t3 = new Text(" to ");
                    t3.setId("summary-normal");
                    this.tfSummary.getChildren().add(t3);
                    Text t4 = new Text(info.getDestination());
                    t4.setId("summary-code");
                    if (this.changeFont) {
                        t4.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t4);
                    Text t5 = new Text(".");
                    t5.setId("summary-normal");
                    this.tfSummary.getChildren().add(t5);
                    break;
                }
                if (info.isPreIndexed()) {
                    Text t = new Text(info.getName());
                    t.setId("summary-code");
                    if (this.changeFont) {
                        t.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t);
                    Text t1 = new Text(" is set to ");
                    t1.setId("summary-normal");
                    this.tfSummary.getChildren().add(t1);
                    Text t2 = new Text("(" + info.getName() + " + 0x" + Integer.toHexString(info.getOffset()) + ")");
                    t2.setId("summary-code");
                    if (this.changeFont) {
                        t2.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t2);
                    Text t3 = new Text(". Then, this instruction loads the byte from the new value of ");
                    t3.setId("summary-normal");
                    this.tfSummary.getChildren().add(t3);
                    Text t4 = new Text(info.getName());
                    t4.setId("summary-code");
                    if (this.changeFont) {
                        t4.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t4);
                    Text t5 = new Text(" to ");
                    t5.setId("summary-normal");
                    this.tfSummary.getChildren().add(t5);
                    Text t6 = new Text(info.getDestination());
                    t6.setId("summary-code");
                    if (this.changeFont) {
                        t6.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t6);
                    Text t7 = new Text(".");
                    t7.setId("summary-normal");
                    this.tfSummary.getChildren().add(t7);
                    break;
                }
                Text t = new Text("This instruction loads the byte from ");
                t.setId("summary-normal");
                this.tfSummary.getChildren().add(t);
                Text t1 = new Text("the address in ");
                t1.setId("summary-normal");
                this.tfSummary.getChildren().add(t1);
                Text t2 = new Text(info.getName());
                t2.setId("summary-code");
                if (this.changeFont) {
                    t2.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t2);
                Text t3 = new Text(" to ");
                t3.setId("summary-normal");
                this.tfSummary.getChildren().add(t3);
                Text t4 = new Text(info.getDestination());
                t4.setId("summary-code");
                if (this.changeFont) {
                    t4.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t4);
                Text t5 = new Text(".");
                t5.setId("summary-normal");
                this.tfSummary.getChildren().add(t5);
                Text t6 = new Text(" Then, ");
                t6.setId("summary-normal");
                this.tfSummary.getChildren().add(t6);
                Text t7 = new Text(info.getName());
                t7.setId("summary-code");
                if (this.changeFont) {
                    t7.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t7);
                Text t8 = new Text(" is set to ");
                t8.setId("summary-normal");
                this.tfSummary.getChildren().add(t8);
                Text t9 = new Text("(" + info.getName() + " + 0x" + Integer.toHexString(info.getOffset()) + ")");
                t9.setId("summary-code");
                if (this.changeFont) {
                    t9.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t9);
                Text t10 = new Text(".");
                t10.setId("summary-normal");
                this.tfSummary.getChildren().add(t10);
                break;
            }
            case STR: {
                if (!info.isPreIndexed() && !info.isPostIndexed()) {
                    Text t = new Text("This instruction saves the value in ");
                    t.setId("summary-normal");
                    this.tfSummary.getChildren().add(t);
                    Text t0 = new Text(info.getDestination());
                    t0.setId("summary-code");
                    if (this.changeFont) {
                        t0.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t0);
                    if (info.getOffset() == 0) {
                        Text t1 = new Text(" to the address in ");
                        t1.setId("summary-normal");
                        this.tfSummary.getChildren().add(t1);
                        Text t2 = new Text(info.getName());
                        t2.setId("summary-code");
                        if (this.changeFont) {
                            t2.setStyle("-fx-font-family: Consolas");
                        }
                        this.tfSummary.getChildren().add(t2);
                        Text t3 = new Text(".");
                        t3.setId("summary-normal");
                        this.tfSummary.getChildren().add(t3);
                        break;
                    }
                    Text t1 = new Text(" to the address in ");
                    t1.setId("summary-normal");
                    this.tfSummary.getChildren().add(t1);
                    Text t2 = new Text("(" + info.getName() + " + 0x" + Integer.toHexString(info.getOffset()) + ")");
                    t2.setId("summary-code");
                    if (this.changeFont) {
                        t2.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t2);
                    Text t3 = new Text(".");
                    t3.setId("summary-normal");
                    this.tfSummary.getChildren().add(t3);
                    break;
                }
                if (info.isPreIndexed()) {
                    Text t = new Text(info.getName());
                    t.setId("summary-code");
                    if (this.changeFont) {
                        t.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t);
                    Text t1 = new Text(" is set to ");
                    t1.setId("summary-normal");
                    this.tfSummary.getChildren().add(t1);
                    Text t2 = new Text("(" + info.getName() + " + 0x" + Integer.toHexString(info.getOffset()) + ")");
                    t2.setId("summary-code");
                    if (this.changeFont) {
                        t2.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t2);
                    Text t3 = new Text(". Then, this instruction saves the value in ");
                    t3.setId("summary-normal");
                    this.tfSummary.getChildren().add(t3);
                    Text t4 = new Text(info.getDestination());
                    t4.setId("summary-code");
                    if (this.changeFont) {
                        t4.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t4);
                    Text t5 = new Text(" to the new address in ");
                    t5.setId("summary-normal");
                    this.tfSummary.getChildren().add(t5);
                    Text t6 = new Text(info.getName());
                    t6.setId("summary-code");
                    if (this.changeFont) {
                        t6.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t6);
                    Text t7 = new Text(".");
                    t7.setId("summary-normal");
                    this.tfSummary.getChildren().add(t7);
                    break;
                }
                Text t = new Text("This instruction saves the value in ");
                t.setId("summary-normal");
                this.tfSummary.getChildren().add(t);
                Text t0 = new Text(info.getDestination());
                t0.setId("summary-code");
                if (this.changeFont) {
                    t0.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t0);
                Text t1 = new Text(" to the address in ");
                t1.setId("summary-normal");
                this.tfSummary.getChildren().add(t1);
                Text t2 = new Text(info.getName());
                t2.setId("summary-code");
                if (this.changeFont) {
                    t2.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t2);
                Text t3 = new Text(".");
                t3.setId("summary-normal");
                this.tfSummary.getChildren().add(t3);
                Text t6 = new Text(" Then, ");
                t6.setId("summary-normal");
                this.tfSummary.getChildren().add(t6);
                Text t7 = new Text(info.getName());
                t7.setId("summary-code");
                if (this.changeFont) {
                    t7.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t7);
                Text t8 = new Text(" is set to ");
                t8.setId("summary-normal");
                this.tfSummary.getChildren().add(t8);
                Text t9 = new Text("(" + info.getName() + " + 0x" + Integer.toHexString(info.getOffset()) + ")");
                t9.setId("summary-code");
                if (this.changeFont) {
                    t9.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t9);
                Text t10 = new Text(".");
                t10.setId("summary-normal");
                this.tfSummary.getChildren().add(t10);
                break;
            }
            case STRB: {
                if (!info.isPreIndexed() && !info.isPostIndexed()) {
                    Text t = new Text("This instruction saves the byte value in ");
                    t.setId("summary-normal");
                    this.tfSummary.getChildren().add(t);
                    Text t0 = new Text(info.getDestination());
                    t0.setId("summary-code");
                    if (this.changeFont) {
                        t0.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t0);
                    if (info.getOffset() == 0) {
                        Text t1 = new Text(" to the address in ");
                        t1.setId("summary-normal");
                        this.tfSummary.getChildren().add(t1);
                        Text t2 = new Text(info.getName());
                        t2.setId("summary-code");
                        if (this.changeFont) {
                            t2.setStyle("-fx-font-family: Consolas");
                        }
                        this.tfSummary.getChildren().add(t2);
                        Text t3 = new Text(".");
                        t3.setId("summary-normal");
                        this.tfSummary.getChildren().add(t3);
                        break;
                    }
                    Text t1 = new Text(" to the address in ");
                    t1.setId("summary-normal");
                    this.tfSummary.getChildren().add(t1);
                    Text t2 = new Text("(" + info.getName() + " + 0x" + Integer.toHexString(info.getOffset()) + ")");
                    t2.setId("summary-code");
                    if (this.changeFont) {
                        t2.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t2);
                    Text t3 = new Text(".");
                    t3.setId("summary-normal");
                    this.tfSummary.getChildren().add(t3);
                    break;
                }
                if (info.isPreIndexed()) {
                    Text t = new Text(info.getName());
                    t.setId("summary-code");
                    if (this.changeFont) {
                        t.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t);
                    Text t1 = new Text(" is set to ");
                    t1.setId("summary-normal");
                    this.tfSummary.getChildren().add(t1);
                    Text t2 = new Text("(" + info.getName() + " + 0x" + Integer.toHexString(info.getOffset()) + ")");
                    t2.setId("summary-code");
                    if (this.changeFont) {
                        t2.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t2);
                    Text t3 = new Text(". Then, this instruction saves the byte value in ");
                    t3.setId("summary-normal");
                    this.tfSummary.getChildren().add(t3);
                    Text t4 = new Text(info.getDestination());
                    t4.setId("summary-code");
                    if (this.changeFont) {
                        t4.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t4);
                    Text t5 = new Text(" to the new address in ");
                    t5.setId("summary-normal");
                    this.tfSummary.getChildren().add(t5);
                    Text t6 = new Text(info.getName());
                    t6.setId("summary-code");
                    if (this.changeFont) {
                        t6.setStyle("-fx-font-family: Consolas");
                    }
                    this.tfSummary.getChildren().add(t6);
                    Text t7 = new Text(".");
                    t7.setId("summary-normal");
                    this.tfSummary.getChildren().add(t7);
                    break;
                }
                Text t = new Text("This instruction saves the byte value in ");
                t.setId("summary-normal");
                this.tfSummary.getChildren().add(t);
                Text t0 = new Text(info.getDestination());
                t0.setId("summary-code");
                if (this.changeFont) {
                    t0.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t0);
                Text t1 = new Text(" to the address in ");
                t1.setId("summary-normal");
                this.tfSummary.getChildren().add(t1);
                Text t2 = new Text(info.getName());
                t2.setId("summary-code");
                if (this.changeFont) {
                    t2.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t2);
                Text t3 = new Text(".");
                t3.setId("summary-normal");
                this.tfSummary.getChildren().add(t3);
                Text t6 = new Text(" Then, ");
                t6.setId("summary-normal");
                this.tfSummary.getChildren().add(t6);
                Text t7 = new Text(info.getName());
                t7.setId("summary-code");
                if (this.changeFont) {
                    t7.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t7);
                Text t8 = new Text(" is set to ");
                t8.setId("summary-normal");
                this.tfSummary.getChildren().add(t8);
                Text t9 = new Text("(" + info.getName() + " + 0x" + Integer.toHexString(info.getOffset()) + ")");
                t9.setId("summary-code");
                if (this.changeFont) {
                    t9.setStyle("-fx-font-family: Consolas");
                }
                this.tfSummary.getChildren().add(t9);
                Text t10 = new Text(".");
                t10.setId("summary-normal");
                this.tfSummary.getChildren().add(t10);
                break;
            }
        }
    }
}

