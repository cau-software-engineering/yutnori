package org.view.jfx.board;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.core.domain.board.BoardType;
import org.core.dto.NodeViewDto;

import java.util.Comparator;
import java.util.List;

/**
 * Swing BoardDrawing → JavaFX.
 *   · views : BoardViewMapper 가 계산한 노드 좌표
 *   · type  : SQUARE / PENTAGON / HEXAGON 에 따라 선 패턴 결정
 */
public final class BoardDrawing extends Pane {

    private static final int NORMAL_R = 15;
    private static final int CORNER_R = 30;

    private final BoardType type;
    private final List<NodeViewDto> views;
    private final int margin;
    private final int size;

    public BoardDrawing(BoardType type,
                        List<NodeViewDto> views,
                        int margin,
                        int size) {
        this.type   = type;
        this.views  = views;
        this.margin = margin;
        this.size   = size;

        setPickOnBounds(false);
        drawBoard();
    }

    /* ─────────────────────────────────────────────────────────── */

    private void drawBoard() {
        // 1) 노드(점)부터
        for (NodeViewDto v : views) {
            int r = v.name().startsWith("S") ? CORNER_R : NORMAL_R;
            Circle c = new Circle(v.x(), v.y(), r, Color.WHITE);
            c.setStroke(Color.BLACK);
            getChildren().add(c);
        }

        // 2) 보드 외곽/대각선/중앙선
        switch (type) {
            case SQUARE   -> drawSquare();
            case PENTAGON -> drawNGon(5);
            case HEXAGON  -> drawNGon(6);
        }
    }

    private void drawSquare() {
        double x = margin, y = margin, w = size;
        // 외곽 + 대각선
        getChildren().addAll(
                new Line(x, y, x + w, y),                     // 상
                new Line(x + w, y, x + w, y + w),             // 우
                new Line(x + w, y + w, x, y + w),             // 하
                new Line(x, y + w, x, y),                     // 좌
                new Line(x, y, x + w, y + w),                 // 대각
                new Line(x + w, y, x, y + w)                  // 대각
        );
    }

    private void drawNGon(int sides) {
        /* Swing 로직 그대로: S1-S2-… 모서리 ↔ S6(센터) 연결 */
        NodeViewDto center = views.stream()
                .filter(v -> v.name().equals("S6"))
                .findFirst()
                .orElseThrow();

        List<NodeViewDto> sNodes =
                views.stream()
                        .filter(v -> v.name().startsWith("S"))
                        .sorted(Comparator.comparingInt(
                                v -> Integer.parseInt(v.name().substring(1))))
                        .toList();

        for (int i = 0; i < sides; i++) {
            NodeViewDto cur  = sNodes.get(i);
            NodeViewDto next = sNodes.get((i + 1) % sides);

            // 외곽
            getChildren().add(new Line(cur.x(), cur.y(), next.x(), next.y()));
            // 중심 ↔ 모서리
            getChildren().add(new Line(center.x(), center.y(), cur.x(), cur.y()));
        }
    }
}
