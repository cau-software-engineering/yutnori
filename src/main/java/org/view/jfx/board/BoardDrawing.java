package org.view.jfx.board;

import java.util.Comparator;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.core.domain.board.BoardType;
import org.core.dto.NodeViewDto;

/**
 * BoardDrawing (JavaFX)
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
    this.type = type;
    this.views = views;
    this.margin = margin;
    this.size = size;

    setPickOnBounds(false);
    drawBoard();
  }


  private void drawBoard() {
    switch (type) {
      case SQUARE -> drawSquare();
      case PENTAGON -> drawNGon(5);
      case HEXAGON -> drawNGon(6);
    }

    for (NodeViewDto v : views) {
      int r = v.name().startsWith("S") ? CORNER_R : NORMAL_R;
      Circle c = new Circle(v.x(), v.y(), r, Color.WHITE);
      c.setStroke(Color.BLACK);
      getChildren().add(c);
    }
  }

  private void drawSquare() {
    double x = margin, y = margin, w = size;

    getChildren().addAll(
        new Line(x, y, x + w, y),                          // 상
        new Line(x + w, y, x + w, y + w),             // 우
        new Line(x + w, y + w, x, y + w),             // 하
        new Line(x, y + w, x, y),                          // 좌
        new Line(x, y, x + w, y + w),                   // 대각
        new Line(x + w, y, x, y + w)                     // 대각
    );
  }

  private void drawNGon(int sides) {
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
      NodeViewDto cur = sNodes.get(i);
      NodeViewDto next = sNodes.get((i + 1) % sides);

      // 외곽
      getChildren().add(new Line(cur.x(), cur.y(), next.x(), next.y()));
      // 중심 ↔ 모서리
      getChildren().add(new Line(center.x(), center.y(), cur.x(), cur.y()));
    }
  }
}