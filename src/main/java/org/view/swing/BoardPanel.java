package org.view.swing;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.core.state.game.GameStateMachine;
import org.view.mapper.BoardViewMapper;
import org.core.dto.NodeViewDto;

public class BoardPanel extends JPanel {
    private final GameStateMachine gameSM;
    private final BoardDrawing boardDrawing = new BoardDrawing();

    public BoardPanel(GameStateMachine gameSM) {
        this.gameSM = gameSM;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int margin = 100, size = 400;
        Graphics2D g2 = (Graphics2D) g;
        List<NodeViewDto> views = new BoardViewMapper()
                .mapTo(gameSM.context.boardType, margin, size);

        boardDrawing.draw(g2, gameSM.context.boardType, views, margin, size);
    }
}

