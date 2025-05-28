package org.view.jfx.control;

import java.util.List;
import java.util.stream.Collectors;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.core.domain.yut.YutResult;
import org.core.state.turn.TurnStateMachine;

/**
 * YutResultPanel (JavaFX)
 */
public final class YutResultPanel extends StackPane {

  private final Label label;
  private final TurnStateMachine turnSM;

  public YutResultPanel(
      TurnStateMachine turnSM
  ) {
    this.turnSM = turnSM;

    setPrefHeight(80);               // 컨트롤 패널 안에서 적당한 높이 확보
    setAlignment(Pos.CENTER);

    label = new Label();
    label.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: black;");
    getChildren().add(label);
  }

  public void update() {
    List<YutResult> results = turnSM.context.getYutResults();

    if (results == null) {
      label.setText("");
    } else {
      label.setText(results.stream().map(YutResult::getName).collect(Collectors.joining(", ")));
    }
  }
}