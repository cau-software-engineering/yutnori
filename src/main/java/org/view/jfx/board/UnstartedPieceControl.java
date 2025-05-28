package org.view.jfx.board;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.core.domain.piece.GamePieces;
import org.view.jfx.StoreJFX;

public class UnstartedPieceControl extends HBox {

  private List<UnstartedPieceButton> buttons = new ArrayList<>();

  public UnstartedPieceControl(List<GamePieces> unstartedPieces, StoreJFX store,
      Function<GamePieces, Void> onPieceSelected) {
    HBox hbox = new HBox();

    hbox.setStyle(
        "-fx-background-color: rgba(255,255,255,0.6);" +
            "-fx-background-radius: 24;" +
            "-fx-border-radius: 24;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 4;" +
            "-fx-spacing: 4;" +
            "-fx-alignment: center;"
    );

    Label label = new Label("대기 중인 말");
    label.setPadding(new javafx.geometry.Insets(0, 10, 0, 10));
    label.setStyle("-fx-font-size: 13px; -fx-text-fill: black; -fx-font-weight: bold;");

    hbox.getChildren().add(label);

    unstartedPieces.forEach(piece -> {
      UnstartedPieceButton btn = new UnstartedPieceButton(piece, store);
      buttons.add(btn);

      btn.setOnAction(e -> {
        btn.setHighlighted(true); // 현재 버튼 하이라이트 토글
        onPieceSelected.apply(btn.getPiece());

        buttons.forEach(p -> {
          if (p != btn) {
            p.setHighlighted(false); // 다른 버튼 하이라이트 해제
          }
        });
      });
      btn.setMouseTransparent(false);

      hbox.getChildren().add(btn);
      buttons.add(btn);
    });

    getChildren().add(hbox);
  }

  public void unselectAll() {
    buttons.forEach(btn -> btn.setHighlighted(false));
  }

}

class UnstartedPieceButton extends Button {

  private final GamePieces piece;
  private boolean highlighted = false;

  public UnstartedPieceButton(GamePieces piece, StoreJFX store) {
    this.piece = piece;
    setPrefSize(40, 40);
    setStyle(
        Utils.roundStyle(store.getPalette(piece.getTeam())) +
            "-fx-background-insets: 0, 2;"
    );

    setHighlighted(false);
    setOnAction(e -> {
      // 클릭 시 하이라이트 상태 토글
      setHighlighted(!highlighted);
    });
  }

  public void setHighlighted(boolean highlighted) {
    this.highlighted = highlighted;

    if (highlighted) {
      setStyle(getStyle() + Utils.innerRoundstyle(Color.BLACK));
    } else {
      setStyle(getStyle() + Utils.innerRoundstyle(Color.WHITE));
    }

    applyCss();
    requestLayout();
  }

  public GamePieces getPiece() {
    return piece;
  }
}