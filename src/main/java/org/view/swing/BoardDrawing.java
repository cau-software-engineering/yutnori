package org.view.swing;

import java.awt.*;
import java.util.List;
import java.util.Comparator;

import org.core.domain.board.BoardType;
import org.core.dto.NodeViewDto;

public class BoardDrawing {
    private static final int NORMAL_R = 15;
    private static final int CORNER_R = 30;

    public void draw(Graphics2D g2,
                     BoardType type,
                     List<NodeViewDto> views,
                     int margin,
                     int size) {
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.BLACK);

        for (var dto : views) {
            int r = dto.name().startsWith("S") ? CORNER_R : NORMAL_R;
            g2.fillOval(dto.x() - r, dto.y() - r, 2*r, 2*r);
        }

        switch (type) {
            case SQUARE:
                drawSquare(g2, margin, size);
                break;
            case PENTAGON:
                drawNGon(g2, views, 5);
                break;
            case HEXAGON:
                drawNGon(g2, views, 6);
                break;
        }
    }

    private void drawSquare(Graphics2D g2, int m, int s) {
        g2.drawRect(m, m, s, s);
        g2.drawLine(m, m, m + s, m + s);
        g2.drawLine(m + s, m, m, m + s);
    }

    private void drawNGon(Graphics2D g2,
                          List<NodeViewDto> views,
                          int sides) {
        var center = views.stream()
                .filter(dto -> dto.name().equals("S6"))
                .findFirst()
                .orElseThrow();
        var sNodes = views.stream()
                .filter(dto -> dto.name().startsWith("S"))
                .sorted(Comparator.comparingInt(dto -> Integer.parseInt(dto.name().substring(1))))
                .toList();

        for (int i = 0; i < sides; i++) {
            var cur = sNodes.get(i);
            var next = sNodes.get((i + 1) % sides);
            g2.drawLine(cur.x(), cur.y(), next.x(), next.y());
            String target = "S" + i;
            views.stream()
                    .filter(dto -> dto.name().equals(target))
                    .findFirst()
                    .ifPresent(v -> g2.drawLine(center.x(), center.y(), v.x(), v.y()));
        }
    }
}
