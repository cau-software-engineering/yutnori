package org.view.jfx.board;

import java.util.List;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import org.core.dto.NodeViewDto;
import org.core.state.game.GameStateMachine;
import org.core.state.turn.TurnStateMachine;
import org.core.state.turn.state.TurnWaitForActionState;
import org.view.jfx.StoreJFX;
import org.view.mapper.BoardViewMapper;

/**
 * BoardPanel (JavaFX)
 */
public final class BoardPanel extends Pane {

  private static final int MARGIN = 80;
  private static final int SIZE = 400;
  private static final int NORMAL_R = 15;

  private final BoardDrawing boardLayer;
  private final PieceDrawing pieceLayer;
  private final MoveControl moveControlLayer;


  private final GameStateMachine gameSM;
  private final TurnStateMachine turnSM;
  private final StoreJFX store;

  public BoardPanel(GameStateMachine gameSM, TurnStateMachine turnSM, StoreJFX store,
      int teamCount) {
    this.gameSM = gameSM;
    this.turnSM = turnSM;
    this.store = store;

    // Set background image
    Image bgImage = new Image(
        getClass().getResource("/images/board-bg.jpg").toExternalForm());
    BackgroundImage bg = new BackgroundImage(
        bgImage,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        new BackgroundSize(
            1.0, 1.0, true, true, false, true
        )
    );
    this.setBackground(new Background(bg));

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