package org.view.jfx.board;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * BoardDrawing (JavaFX)
 */
public class BoardDrawing extends Pane {
    private final double size;
    private final double margin;
    private final double cell;

    public BoardDrawing(double size, double margin) {
        this.size = size;
        this.margin = margin;
        this.cell = size / 4.0; // 5×5 그리드 간격
        setPickOnBounds(false); // 이벤트 전파를 위해 투명하게 패스
        drawLines();
        drawNodes();
    }

    private void drawLines() {
        double left = margin;
        double right = margin + size;
        double top = margin;
        double bottom = margin + size;
        double center = margin + size / 2.0;

        // 외곽선은 BoardPanel이 그리므로 생략하고 내부 선만
        // 중앙 십자(—, |)
        getChildren().add(new Line(left, center, right, center));
        getChildren().add(new Line(center, top, center, bottom));

        // 중심 ↔ 네 모서리
        getChildren().add(new Line(left, top, right, bottom));
        getChildren().add(new Line(right, top, left, bottom));

        // 네 변의 중점 ↔ 중심
        getChildren().add(new Line(center, top, center, center));
        getChildren().add(new Line(center, bottom, center, center));
        getChildren().add(new Line(left, center, center, center));
        getChildren().add(new Line(right, center, center, center));
    }

    private void drawNodes() {
        double r = 6; // node radius
        // 5×5 격자 노드 중 실제 윷놀이 핵심 노드만 표시 (간단 버전)
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boolean isEdge = (i == 0 || i == 4 || j == 0 || j == 4);
                boolean isCenter = (i == 2 && j == 2);
                boolean isCross = (i == 2 || j == 2) || (i == j) || (i + j == 4);
                if (isCenter || (isEdge && isCross)) {
                    double cx = margin + i * cell;
                    double cy = margin + j * cell;
                    Circle node = new Circle(cx, cy, r, Color.WHITE);
                    node.setStroke(Color.BLACK);
                    getChildren().add(node);
                }
            }
        }
    }
}
