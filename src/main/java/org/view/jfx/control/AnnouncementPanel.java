package org.view.jfx.control;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * AnnouncementPanel (JavaFX)
 * <p>
 * Swing 버전과 동일하게 중앙 정렬된 굵은 텍스트를 표시하고, duration(ms) 이 0보다 크면
 * 지정된 시간 후 자동으로 사라집니다.
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

    /**
     * 공지 텍스트를 보여줍니다.
     *
     * @param text           표시할 문자열
     * @param durationMillis 표시 지속 시간(ms). 0 이하면 계속 표시
     */
    public void announce(String text, int durationMillis) {
        // 이전 타이머 중지
        if (timer != null) {
            timer.stop();
        }

        Platform.runLater(() -> label.setText(text));

        // durationMillis>0 이면 지정 시간 뒤 텍스트 제거
        if (durationMillis > 0) {
            timer = new PauseTransition(Duration.millis(durationMillis));
            timer.setOnFinished(e -> Platform.runLater(() -> label.setText("")));
            timer.play();
        }
    }
}
