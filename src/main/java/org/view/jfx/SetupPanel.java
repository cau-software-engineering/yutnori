package org.view.jfx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.core.domain.board.BoardType;
import org.core.dto.GameInitializeDto;

import java.util.concurrent.CompletableFuture;

public final class SetupPanel extends VBox {

    private int teamCount = 2;
    private final Label teamLabel = new Label(String.valueOf(teamCount));

    private int malCount  = 2;
    private final Label malLabel  = new Label(String.valueOf(malCount));

    private int boardSize = 4;            // 4×4 기본

    public CompletableFuture<GameInitializeDto> startSetup() {
        CompletableFuture<GameInitializeDto> promise = new CompletableFuture<>();

        Stage stage = new Stage();
        stage.setTitle("윷놀이 게임 설정");
        stage.setScene(new Scene(this, 400, 360));
        stage.setResizable(false);
        stage.centerOnScreen();

        // 전체 레이아웃
        setPadding(new Insets(20, 30, 20, 30));
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);

        // 팀·말 수 카운터
        getChildren().add(createCounterBox("몇 명의 팀으로 진행할까요? (2-4)",
                teamLabel, 2, 4, true));
        getChildren().add(createCounterBox("몇 개의 말로 진행할까요? (2-5)",
                malLabel, 2, 5, false));

        // 보드 크기 선택
        getChildren().add(createBoardSelector());

        // 확정 버튼
        Button confirm = new Button("확정");
        confirm.setOnAction(e -> {
            promise.complete(new GameInitializeDto(
                    teamCount,
                    malCount,
                    BoardType.mapTo(boardSize)
            ));
            stage.close();
        });
        getChildren().add(confirm);

        stage.show();
        return promise;
    }



    private VBox createCounterBox(String title, Label valueLabel,
                                  int min, int max, boolean isTeam) {

        valueLabel.setMinWidth(30);
        valueLabel.setAlignment(Pos.CENTER);

        Button plus  = new Button("+");
        Button minus = new Button("-");

        plus.setOnAction(e -> adjustCounter(valueLabel, +1, max, isTeam));
        minus.setOnAction(e -> adjustCounter(valueLabel, -1, min, isTeam));

        HBox counter = new HBox(20, minus, valueLabel, plus);
        counter.setAlignment(Pos.CENTER);

        VBox box = new VBox(10,
                new Label(title),
                counter
        );
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void adjustCounter(Label label, int delta, int bound, boolean isTeam) {
        int val = Integer.parseInt(label.getText()) + delta;
        if ((delta > 0 && val <= bound) || (delta < 0 && val >= bound)) {
            label.setText(String.valueOf(val));
            if (isTeam) teamCount = val;
            else        malCount = val;
        }
    }

    private VBox createBoardSelector() {
        Label prompt = new Label("어떤 보드로 진행할까요?");
        ToggleGroup group = new ToggleGroup();

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);

        for (int size : new int[]{4, 5, 6}) {
            ToggleButton btn = new ToggleButton(size + "×" + size);
            btn.setToggleGroup(group);
            if (size == boardSize) btn.setSelected(true);
            btn.setOnAction(e -> boardSize = size);
            buttons.getChildren().add(btn);
        }

        VBox box = new VBox(10, prompt, buttons);
        box.setAlignment(Pos.CENTER);
        return box;
    }
}
