package org.view.jfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.core.state.game.GameStateMachine;
import org.core.state.game.event.GameStartEvent;
import org.core.state.turn.TurnStateMachine;
import org.view.jfx.board.BoardPanel;
import org.view.jfx.control.ControlPanel;

/**
 * SwingView → JavaFX
 */
public final class JFXView extends Application {

    /* ------------------- JavaFX entry ------------------- */

    @Override
    public void start(Stage primaryStage) {
        // 1) 설정 창(Stage) 호출
        SetupPanel setupPanel = new SetupPanel();
        setupPanel.startSetup().thenAccept(dto -> {

            // 2) 설정 완료 후 메인 UI 구성 (JavaFX Thread)
            Platform.runLater(() -> {
                GameStateMachine gameSM = GameStateMachine.create(dto);
                TurnStateMachine turnSM = TurnStateMachine.create(gameSM);
                StoreFX store             = new StoreFX(dto.teamCount());

                // 왼쪽: 보드  │  오른쪽: 컨트롤
                BoardPanel   board   = new BoardPanel(gameSM, turnSM, store, dto.teamCount());
                ControlPanel control = new ControlPanel(gameSM, turnSM, primaryStage);

                SplitPane split = new SplitPane(board, control);
                split.setPrefSize(1220, 640);
                split.setDividerPositions(0.49);          // 약 600px 위치
                split.setStyle("-fx-background-color: white;");

                // 3) Stage 설정
                primaryStage.setTitle("윷놀이 (JavaFX)");
                primaryStage.setScene(new Scene(split, 1220, 640, Color.WHITE));
                primaryStage.setResizable(false);
                primaryStage.centerOnScreen();
                primaryStage.show();

                // 4) 게임 시작
                gameSM.dispatchEvent(new GameStartEvent());
            });
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
