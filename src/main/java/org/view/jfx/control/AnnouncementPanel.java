package org.view.jfx.control;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * AnnouncementPanel (JavaFX)
 */
public final class AnnouncementPanel extends StackPane {

    private final Label label;
    private PauseTransition timer;

    public AnnouncementPanel() {
        setPrefHeight(80);                 // Swing 에서 고정 높이 80px 사용
        setAlignment(Pos.CENTER);          // 중앙 정렬
        setStyle("-fx-background-color: white;");

        label = new Label();
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");

        getChildren().add(label);
    }

    public void announce(String text, int durationMillis) {
        if (timer != null) {
            timer.stop();
        }

        Platform.runLater(() -> label.setText(text));

        if (durationMillis > 0) {
            timer = new PauseTransition(Duration.millis(durationMillis));
            timer.setOnFinished(e -> Platform.runLater(() -> label.setText("")));
            timer.play();
        }
    }
}
