/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.beans.property.DoubleProperty
 *  javafx.beans.property.SimpleDoubleProperty
 *  javafx.scene.canvas.Canvas
 *  javafx.scene.canvas.GraphicsContext
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 */
package visual;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import visual.CanvasType;
import visual.CodeCanvasState;
import visual.VisUAL;

public class CodeCanvas
extends Canvas {
    private CanvasType type;
    private String colour = "#272822";
    private int lineNumber = -1;
    private int lineHeight = 13;
    private int[] arrowLines = new int[2];
    private boolean hide = false;
    public DoubleProperty visBtnXOffsetProperty = new SimpleDoubleProperty();
    public DoubleProperty coverageMarkerXOffsetProperty = new SimpleDoubleProperty();

    public CodeCanvas(CanvasType type) {
        this.widthProperty().addListener(evt -> {
            this.draw();
            this.visBtnXOffsetProperty.setValue((Number)(this.getWidth() / 2.0 - 200.0));
            this.coverageMarkerXOffsetProperty.setValue((Number)(this.getWidth() / 2.0 - 3.0));
        });
        this.heightProperty().addListener(evt -> this.draw());
        this.type = type;
    }

    public void restoreCanvas(CodeCanvasState state) {
        this.lineNumber = state.getLineNumber();
        this.colour = state.getColour();
        this.type = state.getType();
        this.hide = state.isHide();
        this.arrowLines[0] = state.getArrowLines()[0];
        this.arrowLines[1] = state.getArrowLines()[1];
        if (this.type == CanvasType.ARROW) {
            double newHeight = (Math.abs(this.arrowLines[0] - this.arrowLines[1]) + 1) * this.lineHeight;
            if (newHeight < 16384.0) {
                this.setHeight((Math.abs(this.arrowLines[0] - this.arrowLines[1]) + 1) * this.lineHeight);
            } else {
                this.hide = true;
                return;
            }
        }
        this.reposition(VisUAL.getCodeScrollPane().getVerticalScrollBar().getValue());
        this.draw();
    }

    public void draw() {
        double width = this.getWidth();
        double height = this.getHeight();
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.clearRect(0.0, 0.0, width, height);
        if (this.hide) {
            return;
        }
        gc.setFill((Paint)Color.web((String)this.colour));
        switch (this.type) {
            case HIGHLIGHT: {
                gc.fillRect(0.0, 0.0, width, height);
                break;
            }
            case ARROW: {
                int ARROW_WIDTH = 2;
                int HEAD_SIZE = 10;
                gc.fillRect(width / 2.0 - (double)(ARROW_WIDTH / 2), 1.0, (double)ARROW_WIDTH, height - 1.0);
                double[] headX = new double[]{width / 2.0, width / 2.0 - (double)(HEAD_SIZE / 2), width / 2.0 + (double)(HEAD_SIZE / 2)};
                if (this.arrowLines[1] < this.arrowLines[0]) {
                    double[] headY = new double[]{this.lineHeight, this.lineHeight + HEAD_SIZE, this.lineHeight + HEAD_SIZE};
                    gc.fillPolygon(headX, headY, 3);
                    break;
                }
                double[] headY = new double[]{height - (double)this.lineHeight, height - (double)this.lineHeight - (double)HEAD_SIZE, height - (double)this.lineHeight - (double)HEAD_SIZE};
                gc.fillPolygon(headX, headY, 3);
            }
        }
    }

    public void setColour(String colour, boolean redraw) {
        this.colour = colour;
        if (redraw) {
            this.draw();
        }
    }

    public void setLineNumber(int lineNumber, boolean deferSceneUpdates) {
        this.lineNumber = lineNumber;
        if (!deferSceneUpdates) {
            this.reposition(VisUAL.getCodeScrollPane().getVerticalScrollBar().getValue());
        }
    }

    public void setArrowLines(int[] arrowLines, boolean redraw) {
        this.arrowLines[0] = arrowLines[0];
        this.arrowLines[1] = arrowLines[1];
        if (redraw) {
            this.draw();
        }
    }

    public void reposition(int scrollPosition) {
        this.translateYProperty().set((double)(this.lineNumber * VisUAL.getRSyntaxTextArea().getLineHeight() - scrollPosition) - VisUAL.getCodeStackPane().getHeight() / 2.0 + this.getHeight() / 2.0);
    }

    public void setHidden(boolean value, boolean redraw) {
        this.hide = value;
        if (redraw) {
            this.draw();
        }
    }

    public void setLineHeight(int value) {
        this.lineHeight = value;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public boolean isHide() {
        return this.hide;
    }

    public String getColour() {
        return this.colour;
    }

    public int[] getArrowLines() {
        return this.arrowLines;
    }

    public CanvasType getType() {
        return this.type;
    }

    public String toString() {
        return "CodeCanvas of type " + this.type.toString() + " on line " + this.lineNumber + " with colour " + this.colour + " with translateY position " + this.translateYProperty().getValue();
    }
}

