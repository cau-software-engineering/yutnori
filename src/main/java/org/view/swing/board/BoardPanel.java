// BoardPanel.java
package org.view.swing.board;

import java.awt.Color;
import java.util.List;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import org.core.dto.NodeViewDto;
import org.core.state.game.GameStateMachine;
import org.core.state.turn.TurnStateMachine;
import org.view.mapper.BoardViewMapper;
import org.view.swing.Store;

/**
 * 보드 + 말 레이어를 그리는 패널
 */
public class BoardPanel extends JLayeredPane {

  private static final int MARGIN = 100;
  private static final int SIZE = 400;
  private static final int NORMAL_R = 15;

  private final GameStateMachine gameSM;
  private final TurnStateMachine turnSM;

  private final BoardDrawing boardLayer;
  private final PieceDrawing pieceLayer;
  private final MoveControl moveControlLayer;

  public BoardPanel(
      GameStateMachine gameSM,
      TurnStateMachine turnSM,
      Store store,
      int teamCount
  ) {

    this.setLayout(null);

    this.gameSM = gameSM;
    this.turnSM = turnSM;

    setBackground(new Color(246, 246, 246));
    setDoubleBuffered(true);

    List<NodeViewDto> nodeViews = new BoardViewMapper()
        .mapTo(gameSM.context.boardType, MARGIN, SIZE);

    store.setNodePos(nodeViews);

    boardLayer = new BoardDrawing(gameSM.context.boardType, nodeViews, MARGIN, SIZE);
    boardLayer.setBounds(0, 0, SIZE + 2 * MARGIN, SIZE + 2 * MARGIN);

    pieceLayer = new PieceDrawing(store, gameSM.context.boardService,
        nodeViews,
        teamCount,
        NORMAL_R);
    pieceLayer.setBounds(0, 0, SIZE + 2 * MARGIN, SIZE + 2 * MARGIN);

    moveControlLayer = new MoveControl(store, gameSM, turnSM);
    moveControlLayer.setBounds(0, 0, SIZE + 2 * MARGIN, SIZE + 2 * MARGIN);

    this.add(boardLayer, JLayeredPane.DEFAULT_LAYER);
    this.add(pieceLayer, JLayeredPane.PALETTE_LAYER);
    this.add(moveControlLayer, JLayeredPane.MODAL_LAYER);

    /* 말 변동 시 즉시 다시 그리기 */
    turnSM.observe(s -> SwingUtilities.invokeLater(this::repaint));
  }
}