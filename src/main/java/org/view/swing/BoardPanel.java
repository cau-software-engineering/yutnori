package org.view.swing;

import javax.swing.*;
import java.awt.*;

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

        // 사각형 보드 외곽선
        if (gameSM.context.boardType == BoardType.SQUARE){
            g2.drawRect(MARGIN, MARGIN, SIZE, SIZE);
            g2.drawLine(MARGIN, MARGIN, MARGIN+SIZE, MARGIN+SIZE);
            g2.drawLine(MARGIN+SIZE, MARGIN, MARGIN, MARGIN+SIZE);

        }
        // 오각형
        else if (gameSM.context.boardType == BoardType.PENTAGON){

        }

        // 육각형
        else {

        }
    }
}
