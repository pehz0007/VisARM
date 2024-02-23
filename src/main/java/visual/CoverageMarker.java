/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.scene.canvas.Canvas
 *  javafx.scene.canvas.GraphicsContext
 *  javafx.scene.paint.Color
 *  javafx.scene.paint.Paint
 */
package visual;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import visual.VisUAL;

public class CoverageMarker
extends Canvas {
    private static String colour = "#6D597C";
    private static int lineHeight = 15;
    private int lineNumber = -1;

    public CoverageMarker(int lineNumber) {
        this.lineNumber = lineNumber;
        this.translateXProperty().set(VisUAL.getCodeStackPane().getWidth() / 2.0 - 3.0);
        this.setWidth(6.0);
        this.setHeight(lineHeight);
        this.draw();
        this.heightProperty().addListener(evt -> this.draw());
    }

    public void draw() {
        double width = this.getWidth();
        double height = this.getHeight();
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill((Paint)Color.web((String)colour));
        gc.fillRect(0.0, 0.0, width, height);
    }

    public static void setLineHeight(int value) {
        lineHeight = value;
    }

    public static void setColour(String colour) {
        CoverageMarker.colour = colour;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
        this.reposition();
    }

    public void reposition() {
        this.translateYProperty().set((double)(this.lineNumber * VisUAL.getRSyntaxTextArea().getLineHeight() - VisUAL.getCodeScrollPane().getVerticalScrollBar().getValue()) - VisUAL.getCodeStackPane().getHeight() / 2.0 + this.getHeight() / 2.0);
    }

    public int getLineNumber() {
        return this.lineNumber;
    }
}

