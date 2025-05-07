package org.view.swing;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

import org.core.domain.board.BoardType;
import org.core.state.game.GameStateMachine;
import org.view.mapper.BoardViewMapper;

public class BoardPanel extends JPanel {

    private static final int NORMAL_R = 15;
    private static final int CORNER_R = 30;

    private final GameStateMachine gameSM;

    public BoardPanel(GameStateMachine gameSM) {
        this.gameSM = gameSM;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int MARGIN = 100, SIZE = 400;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.BLACK);

        // mapTo 에 margin/size 넘겨서 NodeViewDto 리스트 받기
        var views = new BoardViewMapper()
                .mapTo(gameSM.context.boardType, MARGIN, SIZE);



        // dto 좌표값으로 노드 랜더링
        for (var dto : views) {
            int r = dto.name().startsWith("S") ? CORNER_R : NORMAL_R;
            g2.fillOval(dto.x() - r, dto.y() - r, 2*r, 2*r);
        }

        var sNodes = views.stream()
                .filter(dto -> dto.name().startsWith("S"))
                .sorted(Comparator.comparingInt(dto -> Integer.parseInt(dto.name().substring(1))))
                .toList();



        switch (gameSM.context.boardType) {

            case SQUARE:
                g2.drawRect(MARGIN, MARGIN, SIZE, SIZE);
                g2.drawLine(MARGIN, MARGIN, MARGIN + SIZE, MARGIN + SIZE);
                g2.drawLine(MARGIN + SIZE, MARGIN, MARGIN, MARGIN + SIZE);

                break;

            case PENTAGON: {
                var center = views.stream()
                        .filter(dto -> dto.name().equals("S6"))
                        .findFirst()
                        .orElseThrow();
                for (int i = 0; i < 5; i++) {
                    var cur = sNodes.get(i);
                    var next = sNodes.get((i + 1) % 5);
                    g2.drawLine(cur.x(), cur.y(), next.x(), next.y());

                    String target = "S" + i;
                    views.stream()
                            .filter(dto -> dto.name().equals(target))
                            .findFirst()
                            .ifPresent(v -> g2.drawLine(center.x(), center.y(), v.x(), v.y())
                            );
                }
                break;
            }

            case HEXAGON: {
                var center = views.stream()
                        .filter(dto -> dto.name().equals("S6"))
                        .findFirst()
                        .orElseThrow();
                for (int i = 0; i < 6; i++) {
                    var cur = sNodes.get(i);
                    var next = sNodes.get((i + 1) % 6);
                    g2.drawLine(cur.x(), cur.y(), next.x(), next.y());

                    String target = "S" + i;
                    views.stream()
                            .filter(dto -> dto.name().equals(target))
                            .findFirst()
                            .ifPresent(v -> g2.drawLine(center.x(), center.y(), v.x(), v.y())
                            );

                }
                break;
            }
        }
    }
}
