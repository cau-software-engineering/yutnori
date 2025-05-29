package org.view.jfx.board;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import org.core.domain.piece.GamePieces;
import org.core.service.BoardService;
import org.core.state.game.GameStateMachine;
import org.core.state.turn.TurnStateMachine;
import org.view.jfx.StoreJFX;

/**
 * PieceDrawing (JavaFX)
 */
public final class PieceDrawing extends Pane {

  private static final double PIECE_R = 10; // 반지름 (원형 크기)

  private final GameStateMachine gameSM;
  private final TurnStateMachine turnSM;
  private final StoreJFX store;

  private final List<Group> groups = new ArrayList<>();


  public PieceDrawing(GameStateMachine gameSM,
      TurnStateMachine turnSM,
      StoreJFX store) {
    this.gameSM = gameSM;
    this.turnSM = turnSM;
    this.store = store;

    setPickOnBounds(false);     // 빈 영역 클릭은 통과 (BoardPanel 이벤트용)
    setMouseTransparent(true);  // 자체적으로는 클릭 처리 안 함

    gameSM.observe(s -> Platform.runLater(this::refresh));
    turnSM.observe(s -> Platform.runLater(this::refresh));

    refresh(); // 초기 1회
  }

  public void refresh() {

    getChildren().removeAll(groups);
    groups.clear();

    int turn = turnSM.context.turn.getTurn();
    BoardService bs = gameSM.context.boardService;
    List<GamePieces> allPieces = bs.findAllPiecesByTeam(turn);

    for (int team = 1; team <= store.getTeamCount(); team++) {
      for (GamePieces gp : bs.findAllPiecesByTeam(team)) {
        if ("start".equals(gp.getPlace())) {
          continue;   // 아직 출발 안 한 말은 스킵
        }
        Group group = new Group();

        Point2D pos = store.getNodePos(gp.getPlace());
        Circle c = new Circle(0, 0, PIECE_R, store.getPalette(gp.getTeam()));
        c.setStroke(Color.BLACK);

        Label label = new Label(gp.getCount() > 1 ? String.valueOf(gp.getCount()) : "");
        label.setAlignment(javafx.geometry.Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Color.BLACK);
        label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        // Center label on circle
        label.translateXProperty().bind(label.widthProperty().multiply(-0.5));
        label.translateYProperty().bind(label.heightProperty().multiply(-0.5));

        group.getChildren().add(c);
        group.getChildren().add(label);
        group.setLayoutX(pos.getX());
        group.setLayoutY(pos.getY());
        groups.add(group);
      }
    }

    getChildren().addAll(groups);
  }
}