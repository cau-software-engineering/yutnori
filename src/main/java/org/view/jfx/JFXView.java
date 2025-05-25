package org.view.jfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.view.jfx.board.BoardPanel;

public class JFXView extends Application {

    @Override
    public void start(Stage stage) {
        BoardPanel board = new BoardPanel(600, 30);
        StackPane root = new StackPane(board);
        Scene scene = new Scene(root);
        stage.setTitle("윷놀이 (JavaFX)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
