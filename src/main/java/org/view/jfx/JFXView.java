package org.view.jfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage; // 코어 로직 예시

public class JFXView extends Application {

    @Override
    public void start(Stage stage) {


        stage.setScene(new Scene(new Label("Yutnori JFX UI 가동!"), 400, 200));
        stage.setTitle("Yutnori - JavaFX");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);              // IDE에서 직접 실행할 때도 가능
    }
}
