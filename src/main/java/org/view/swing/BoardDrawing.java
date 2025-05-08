package org.view.swing;

import java.awt.*;
import java.util.List;
import java.util.Comparator;

import org.core.domain.board.BoardType;
import org.core.dto.NodeViewDto;

public class BoardDrawing {
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
    }

    public void draw(Graphics2D g2) {
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.BLACK);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // 노드 그리기
        for (NodeViewDto dto : views) {
            int r = dto.name().startsWith("S") ? CORNER_R : NORMAL_R;
            g2.fillOval(dto.x() - r, dto.y() - r, 2 * r, 2 * r);
        }

        // 보드 형태 그리기
        switch (type) {
            case SQUARE:
                drawSquare(g2);
                break;
            case PENTAGON:
                drawNGon(g2, 5);
                break;
            case HEXAGON:
                drawNGon(g2, 6);
                break;
        }
    }

    private void drawSquare(Graphics2D g2) {
        g2.drawRect(margin, margin, size, size);
        g2.drawLine(margin, margin, margin + size, margin + size);
        g2.drawLine(margin + size, margin, margin, margin + size);
    }

    private void drawNGon(Graphics2D g2, int sides) {
        NodeViewDto center = views.stream()
                .filter(dto -> dto.name().equals("S6"))
                .findFirst()
                .orElseThrow();
        List<NodeViewDto> sNodes = views.stream()
                .filter(dto -> dto.name().startsWith("S"))
                .sorted(Comparator.comparingInt(dto -> Integer.parseInt(dto.name().substring(1))))
                .toList();

        for (int i = 0; i < sides; i++) {
            NodeViewDto cur = sNodes.get(i);
            NodeViewDto next = sNodes.get((i + 1) % sides);
            g2.drawLine(cur.x(), cur.y(), next.x(), next.y());

            String target = "S" + i;
            views.stream()
                    .filter(dto -> dto.name().equals(target))
                    .findFirst()
                    .ifPresent(v -> g2.drawLine(center.x(), center.y(), v.x(), v.y()));
        }
    }
}