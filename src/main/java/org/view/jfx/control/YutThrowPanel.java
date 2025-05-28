package org.view.jfx.control;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import org.core.domain.yut.YutGenerateOptions;
import org.core.domain.yut.YutResult;
import org.core.dto.YutGenerationRequest;
import org.core.state.turn.TurnStateMachine;
import org.core.state.turn.event.TurnGenerateYutEvent;

import java.util.Optional;

/**
 * YutThrowPanel (JavaFX)
 */
public final class YutThrowPanel extends HBox {


    private final Button fixedThrowButton;
    private final Button randomThrowButton;

    private final TurnStateMachine turnSM;

    public YutThrowPanel(TurnStateMachine turnSM) {
        this.turnSM = turnSM;

        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(10));
        setPadding(new Insets(280, 10, 10, 10));

        fixedThrowButton = createStyledButton("지정 윷 던지기");
        randomThrowButton = createStyledButton("랜덤 윷 던지기");

        fixedThrowButton.setOnAction(e -> handleFixedThrowButton());
        randomThrowButton.setOnAction(e -> handleRandomThrowButton());

        getChildren().addAll(fixedThrowButton, randomThrowButton);
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(160, 50);
        btn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: #333333;");
        return btn;
    }

    private void handleFixedThrowButton() {
        setEnabled(false);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText("어떤 윷을 던지시겠습니까? (빽도/도/개/걸/윷/모)");

        Optional<String> resultOpt = dialog.showAndWait();
        if (resultOpt.isEmpty()) {
            // 취소 → 다시 활성화
            setEnabled(true);
            return;
        }

        String input = resultOpt.get().trim();
        YutResult res;
        try {
            res = YutResult.from(input);
        } catch (Exception ex) {
            showError("입력이 올바르지 않습니다: " + input);
            setEnabled(true);
            return;
        }

        YutGenerationRequest req = new YutGenerationRequest(YutGenerateOptions.DESIGNATED, res);
        turnSM.dispatchEvent(new TurnGenerateYutEvent(req));
    }

    private void handleRandomThrowButton() {
        setEnabled(false);
        YutGenerationRequest req = new YutGenerationRequest(YutGenerateOptions.RANDOM, null);
        turnSM.dispatchEvent(new TurnGenerateYutEvent(req));
    }

    public void setEnabled(boolean enabled) {
        Platform.runLater(() -> {
            setDisable(!enabled);
            fixedThrowButton.setDisable(!enabled);
            randomThrowButton.setDisable(!enabled);
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

}
