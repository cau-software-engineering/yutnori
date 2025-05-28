package org.view.jfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.core.state.game.GameStateMachine;
import org.core.state.game.event.GameStartEvent;
import org.core.state.turn.TurnStateMachine;
import org.view.jfx.board.BoardPanel;
import org.view.jfx.control.ControlPanel;

/**
 * JavaFXView
 */
public final class JFXView extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    // 설정 창 호출
    SetupPanel setupPanel = new SetupPanel();
    setupPanel.startSetup().thenAccept(dto -> {

      // 메인 UI 구성 (JavaFX Thread)
      Platform.runLater(() -> {
        GameStateMachine gameSM = GameStateMachine.create(dto);
        TurnStateMachine turnSM = TurnStateMachine.create(gameSM);
        StoreJFX store = new StoreJFX(dto.teamCount());

        // 왼쪽: 보드 오른쪽: 컨트롤
        BoardPanel board = new BoardPanel(gameSM, turnSM, store, dto.teamCount());
        ControlPanel control = new ControlPanel(gameSM, turnSM, primaryStage);

        HBox box = new HBox(board, control);

        HBox.setHgrow(box, Priority.ALWAYS);
        HBox.setHgrow(control, Priority.ALWAYS);

        // Stage 설정
        primaryStage.setTitle("윷놀이 (JavaFX)");
        primaryStage.setScene(new Scene(box, 1120, 560, Color.WHITE));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();

        // 게임 시작
        gameSM.dispatchEvent(new GameStartEvent());
      });
    });
  }
}