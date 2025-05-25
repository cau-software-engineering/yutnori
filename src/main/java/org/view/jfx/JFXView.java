package org.view.jfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.view.jfx.board.BoardPanel;
import org.view.jfx.control.ControlPanel;

/**
 * 좌측 400×400 BoardPanel, 우측 400×400 ControlPanel 을 나란히 렌더링.
 */
public class JFXView extends Application {

    @Override
    public void start(Stage stage) {
        final double PANEL_SIDE = 400;        // 최종 외곽 크기
        final double INNER_SIDE = 360;        // 20px margin*2 를 뺀 실제 판 크기

        BoardPanel   boardPanel   = new BoardPanel(INNER_SIDE, 20); // 360+20*2 = 400
        ControlPanel controlPanel = new ControlPanel();

        boardPanel.setPrefSize(PANEL_SIDE, PANEL_SIDE);
        controlPanel.setPrefSize(PANEL_SIDE, PANEL_SIDE);

        HBox root = new HBox(boardPanel, controlPanel);

        Scene scene = new Scene(root, PANEL_SIDE * 2, PANEL_SIDE); // 800×400
        stage.setTitle("Yutnori (JavaFX)");
        stage.setScene(scene);
        stage.setResizable(false);            // 창 크기 고정(선택)
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
