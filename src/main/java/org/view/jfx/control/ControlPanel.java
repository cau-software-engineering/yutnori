package org.view.jfx.control;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.core.domain.game.GameDecision;
import org.core.state.game.GameStateMachine;
import org.core.state.game.event.GameOverEvent;
import org.core.state.game.state.GameOverState;
import org.core.state.turn.TurnStateMachine;
import org.core.state.turn.event.TurnNextTurnEvent;
import org.core.state.turn.state.*;

public final class ControlPanel extends VBox {

    /* ----------------- 의존 객체 ----------------- */
    private final GameStateMachine gameSM;
    private final TurnStateMachine turnSM;
    private final Stage owner;

    /* ----------------- 서브 패널 ----------------- */
    private final AnnouncementPanel announcementPanel;
    private final YutResultPanel    yutResultPanel;
    private final YutThrowPanel     yutThrowPanel;

    public ControlPanel(GameStateMachine gameSM,
                        TurnStateMachine turnSM,
                        Stage owner) {

        this.gameSM = gameSM;
        this.turnSM = turnSM;
        this.owner  = owner;

        /* 레이아웃 */
        setPrefWidth(400);
        setPadding(new Insets(0, 0, 0, 0));
        setStyle("-fx-background-color: white;");

        /* 패널 생성 */
        announcementPanel = new AnnouncementPanel();
        announcementPanel.setPrefHeight(80);

        yutResultPanel = new YutResultPanel();
        yutThrowPanel  = new YutThrowPanel(turnSM);
        yutThrowPanel.setDisable(true);                // 초기엔 비활성화

        /* 배치 (VBox) */
        getChildren().addAll(
                announcementPanel,
                spacer(40),
                yutResultPanel,
                spacer(10),
                yutThrowPanel,
                spacer(20)
        );

        /* 최초 안내 메시지 */
        announcementPanel.announce(
                "게임을 시작합니다. " + gameSM.context.teamCount + "팀이 참여합니다.", 1500);

        PauseTransition pt = new PauseTransition(Duration.millis(1800));
        pt.setOnFinished(ev -> {
            announcementPanel.announce("1팀이 먼저 시작합니다. 윷을 던져주세요.", 0);
            yutThrowPanel.setDisable(false);
        });
        pt.play();

        registerObservers();
    }

    /* --------------- 옵저버 등록 --------------- */
    private void registerObservers() {

        /* GameState */
        gameSM.observe(st -> {
            if (st instanceof GameOverState) {
                Platform.runLater(this::handleGameOver);
            }
        });

        /* TurnState */
        turnSM.observe(st ->
                Platform.runLater(() -> handleTurnState(st))
        );
    }

    /* --------------- GameOver 처리 --------------- */
    private void handleGameOver() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "게임을 다시 시작하시겠습니까?",
                ButtonType.YES, ButtonType.NO);
        alert.initOwner(owner);
        alert.setHeaderText(null);
        alert.setTitle("게임 종료");

        ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
        if (result == ButtonType.YES) {
            owner.close();
            Stage stage = new Stage();
            new org.view.jfx.JFXView().start(stage);
        } else {
            Platform.exit();
        }
    }

    /* --------------- TurnState 처리 --------------- */
    private void handleTurnState(Object st) {

        if (st instanceof TurnIdleState) {
            if (gameSM.isGameOver()) {
                announcementPanel.announce(
                        turnSM.context.getScoreBoard().getWinner() + "팀이 승리하였습니다!", 0);
                gameSM.dispatchEvent(new GameOverEvent());
                return;
            }

            announcementPanel.announce(
                    turnSM.context.turn.getTurn() + "팀 차례입니다. 윷을 던져주세요.", 0);
            yutThrowPanel.setDisable(false);

        } else if (st instanceof TurnRegeneratingState) {
            announcementPanel.announce(
                    turnSM.context.turn.getTurn() + "팀이 윷을 한 번 더 던질 수 있어요!", 0);
            yutThrowPanel.setDisable(false);

        } else if (st instanceof TurnWaitForActionState) {
            announcementPanel.announce(
                    "이동할 " + turnSM.context.turn.getTurn() + "팀 말을 선택해주세요", 0);

        } else if (st instanceof TurnInvalidYutState) {
            announcementPanel.announce("이동할 수 있는 말이 없어 무효 처리 되었습니다.", 1600);
            yutThrowPanel.setDisable(true);

            PauseTransition delay = new PauseTransition(Duration.millis(2000));
            delay.setOnFinished(e -> {
                yutThrowPanel.setDisable(false);
                turnSM.dispatchEvent(new TurnNextTurnEvent());
            });
            delay.play();
        }
        // 나머지 상태는 별도 처리 없음
    }

    /* --------------- 간단한 Spacer --------------- */
    private Region spacer(double height) {
        Region r = new Region();
        r.setMinHeight(height);
        return r;
    }
}
