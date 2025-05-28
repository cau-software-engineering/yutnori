package org.view.jfx.control;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import org.core.domain.yut.YutResult;   // Swing 버전과 동일한 모델 사용

/**
 * YutResultPanel (JavaFX)
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

    public void display(YutResult result) {
        if (result == null) {
            clear();
            return;
        }
        Platform.runLater(() -> label.setText(result.getName()));
    }

    public void clear() {
        Platform.runLater(() -> label.setText(""));
    }
}
