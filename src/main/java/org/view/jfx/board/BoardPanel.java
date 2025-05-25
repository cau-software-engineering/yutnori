package org.view.jfx.board;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * JFX BoardPanel
 */
public class BoardPanel extends Pane {
    private final double size;
    private final double margin;

    public BoardPanel(double size, double margin) {
        this.size = size;
        this.margin = margin;
        setPrefSize(size + 2 * margin, size + 2 * margin);
        drawSquareBoard();
    }

    private void drawSquareBoard() {
        Rectangle border = new Rectangle(margin, margin, size, size);
        border.setStrokeWidth(2);
        border.setStroke(Color.BLACK);
        border.setFill(Color.TRANSPARENT);

        Line d1 = new Line(margin, margin, margin + size, margin + size);
        Line d2 = new Line(margin + size, margin, margin, margin + size);

        getChildren().addAll(border, d1, d2);
    }
}
