package org.view.jfx.control;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import org.core.domain.yut.YutResult;   // Swing 버전과 동일한 모델 사용

/**
 * YutResultPanel (JavaFX)
 * <p>
 * 윷 던지기 결과(빽도/도/개/걸/윷/모)를 굵은 50 pt 글꼴로 중앙에 표시합니다.
 * Swing 의 {@code org.view.swing.control.YutResultPanel} 과 API가 동일합니다.
 */
public final class YutResultPanel extends StackPane {

    private final Label label;

    public YutResultPanel() {
        setPrefHeight(120);               // 컨트롤 패널 안에서 적당한 높이 확보
        setAlignment(Pos.CENTER);

        label = new Label();
        label.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: black;");
        getChildren().add(label);
    }

    /**
     * 결과를 화면에 표시합니다.
     * @param result 윷 결과 객체
     */
    public void display(YutResult result) {
        if (result == null) {
            clear();
            return;
        }
        Platform.runLater(() -> label.setText(result.getName()));
    }

    /**
     * 텍스트를 지웁니다.
     */
    public void clear() {
        Platform.runLater(() -> label.setText(""));
    }
}
