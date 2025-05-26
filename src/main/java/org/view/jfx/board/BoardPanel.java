package org.view.jfx.board;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.core.dto.NodeViewDto;
import org.core.state.game.GameStateMachine;
import org.core.state.turn.TurnStateMachine;
import org.core.state.turn.state.TurnWaitForActionState;
import org.view.jfx.StoreFX;
import org.view.mapper.BoardViewMapper;

import java.util.List;

/**
 * BoardPanel (JavaFX)
 */
public final class BoardPanel extends Pane {

    private static final int MARGIN = 100;
    private static final int SIZE = 400;
    private static final int NORMAL_R = 15;

    private final BoardDrawing boardLayer;
    private final PieceDrawing pieceLayer;
    private final MoveControl moveControlLayer;


    private final GameStateMachine gameSM;
    private final TurnStateMachine turnSM;
    private final StoreFX store;

    public BoardPanel(GameStateMachine gameSM, TurnStateMachine turnSM, StoreFX store, int teamCount) {
        this.gameSM = gameSM;
        this.turnSM = turnSM;
        this.store = store;

        List<NodeViewDto> nodeViews = new BoardViewMapper()
                .mapTo(gameSM.context.boardType, MARGIN, SIZE);

        setPrefSize(SIZE + 2 * MARGIN, SIZE + 2 * MARGIN);
        store.setNodePos(nodeViews);

        boardLayer = new BoardDrawing(gameSM.context.boardType, nodeViews, MARGIN, SIZE);
        pieceLayer = new PieceDrawing(gameSM, turnSM, store);
        moveControlLayer = new MoveControl(gameSM, turnSM, store);

        getChildren().addAll(boardLayer, pieceLayer, moveControlLayer);

        // 첫 렌더링
        pieceLayer.refresh();

        // 턴 상태가 바뀔 때마다 말 레이어 갱신
        turnSM.observe(state -> {
            if (state instanceof TurnWaitForActionState) {
                Platform.runLater(pieceLayer::refresh);
            }
        });
    }
}
