// BoardPanel.java
package org.view.swing;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.core.state.game.GameStateMachine;
import org.core.state.turn.TurnStateMachine;
import org.view.mapper.BoardViewMapper;
import org.core.dto.NodeViewDto;

/** 보드 + 말 레이어를 그리는 패널 */
public class BoardPanel extends JPanel {

    private static final int MARGIN   = 100;
    private static final int SIZE     = 400;
    private static final int NORMAL_R = 15;

    private final GameStateMachine gameSM;
    private final TurnStateMachine turnSM;

    private final BoardDrawing boardLayer;
    private final PieceDrawing pieceLayer;

    public BoardPanel(GameStateMachine gameSM,
                      TurnStateMachine  turnSM,
                      int teamCount) {

        this.gameSM = gameSM;
        this.turnSM = turnSM;

        setBackground(Color.WHITE);
        setDoubleBuffered(true);

        List<NodeViewDto> nodeViews = new BoardViewMapper()
                .mapTo(gameSM.context.boardType, MARGIN, SIZE);

        boardLayer = new BoardDrawing(gameSM.context.boardType, nodeViews, MARGIN, SIZE);
        pieceLayer = new PieceDrawing(gameSM.context.boardService,
                nodeViews,
                NORMAL_R,
                teamCount);

        /* 말 변동 시 즉시 다시 그리기 */
        turnSM.observe(s -> SwingUtilities.invokeLater(this::repaint));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        boardLayer.draw(g2);   // 고정 보드
        pieceLayer.draw(g2);   // 가변 말
    }
}
