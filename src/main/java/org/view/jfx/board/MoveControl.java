package org.view.jfx.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.core.domain.piece.GamePieces;
import org.core.domain.yut.YutResult;
import org.core.service.BoardService;
import org.core.state.game.GameStateMachine;
import org.core.state.turn.TurnStateMachine;
import org.core.state.turn.event.TurnMovePieceEvent;
import org.core.state.turn.state.TurnWaitForActionState;
import org.view.jfx.StoreJFX;

/**
 * MoveControl (JavaFX)
 */
public final class MoveControl extends Pane {


  private static final double PIECE_BTN_D = 40;  // 말 선택 링 버튼 지름
  private static final double DEST_BTN_D = 20;  // 목적지 버튼 지름
  private static final double END_BTN_D = 40;  // "끝" 버튼 지름

  private final GameStateMachine gameSM;
  private final TurnStateMachine turnSM;
  private final StoreJFX store;
  private final List<Button> placedPieceButtons = new ArrayList<>();
  private final List<Button> movablePlaceButtons = new ArrayList<>();

  private Optional<UnstartedPieceControl> unstartedPieceControl = Optional.empty(); // 미출발 말 버튼을 담는 HBox
  private GamePieces selectedPiece;


  public MoveControl(GameStateMachine gameSM,
      TurnStateMachine turnSM,
      StoreJFX store) {
    this.gameSM = gameSM;
    this.turnSM = turnSM;
    this.store = store;

    setPickOnBounds(false);

    setMouseTransparent(false);

    // TurnStateMachine 변화 감지 → 말 선택 표시 / 초기화
    turnSM.observe(state -> {
      if (state instanceof TurnWaitForActionState) {
        Platform.runLater(this::showActions);
      } else {
        Platform.runLater(this::clearAllButtons);
      }
    });
  }


  private void showActions() {
    clearAllButtons();

    int turn = turnSM.context.turn.getTurn();
    List<GamePieces> list = gameSM.context.boardService
        .findAllPiecesByTeam(turn)               // 전체 수집
        .stream()
        .filter(p -> p.getTeam() == turn)
        .toList();

    List<GamePieces> placedPieces = list.stream()
        .filter(p -> !"start".equals(p.getPlace()))
        .toList();
    List<GamePieces> unstartedPieces = list.stream()
        .filter(p -> "start".equals(p.getPlace()))
        .toList();

    if (!unstartedPieces.isEmpty()) {
      UnstartedPieceControl unstartedPieceControl = new UnstartedPieceControl(
          unstartedPieces, store,
          piece -> {
            selectedPiece = piece;
            showMovablePlaces(piece);
            unhighlightPlacedButtons();
            return null;
          });

      unstartedPieceControl.layoutBoundsProperty().addListener((i, e, newBounds) -> {
        double w = newBounds.getWidth();
        double h = newBounds.getHeight();
        final int MARGIN = 10; // 여백
        unstartedPieceControl.setLayoutX(560 - w - MARGIN);
        unstartedPieceControl.setLayoutY(560 - h - MARGIN);
      });

      getChildren().add(unstartedPieceControl);
      this.unstartedPieceControl = Optional.of(unstartedPieceControl);
    }

    placedPieces.forEach(piece -> {
      Button btn = createPlacedPieceButton(piece);
      placedPieceButtons.add(btn);
      getChildren().add(btn);
    });
  }

  private void showMovablePlaces(GamePieces piece) {
    // 기존 목적지 버튼 제거
    movablePlaceButtons.forEach(this::removeButton);
    movablePlaceButtons.clear();

    List<YutResult> yutResults = turnSM.context.getYutResults();
    int turn = turnSM.context.turn.getTurn();
    BoardService bs = gameSM.context.boardService;
    List<GamePieces> allPlaced = bs.findAllPiecesByTeam(turn);

    for (YutResult yutResult : yutResults) {
      List<String> places = bs.findMovablePlaces(piece.getPlace(), yutResult);
      for (String place : places) {
        if (bs.getNode(place).isEnd()) {
          // "끝" 버튼  좌상단
          Button endBtn = createEndButton();
          endBtn.setOnAction(e -> onMove(piece, place, yutResult));
          endBtn.setLayoutX(8);
          endBtn.setLayoutY(8);
          addMovableButton(endBtn);
          continue;
        }

        // 일반 목적지 노드 버튼
        Point2D p = store.getNodePos(place);
        boolean overlapping = allPlaced.stream().anyMatch(gp -> place.equals(gp.getPlace()));
        double marginX = overlapping ? 40 : 0;

        Button destBtn = createDestButton(store.getPalette(piece.getTeam()));
        destBtn.setOnAction(e -> onMove(piece, place, yutResult));
        destBtn.setLayoutX(p.getX() - DEST_BTN_D / 2 + marginX);
        destBtn.setLayoutY(p.getY() - DEST_BTN_D / 2);
        addMovableButton(destBtn);
      }
    }
  }

  private void onMove(GamePieces piece, String destNode, YutResult yutResult) {
    clearAllButtons();
    Platform.runLater(() ->
        turnSM.dispatchEvent(new TurnMovePieceEvent(piece.getId(), destNode, yutResult))
    );
  }

  private Button createPlacedPieceButton(GamePieces piece) {
    Point2D p = store.getNodePos(piece.getPlace());
    Button btn = new Button();
    btn.setPrefSize(PIECE_BTN_D, PIECE_BTN_D);
    btn.setStyle(Utils.ringStyle(Color.WHITE));
    btn.setLayoutX(p.getX() - PIECE_BTN_D / 2);
    btn.setLayoutY(p.getY() - PIECE_BTN_D / 2);

    btn.setOnAction(e -> {
      selectedPiece = piece;
      unstartedPieceControl.ifPresent(pieceControl -> {
        pieceControl.unselectAll(); // 미출발 말 선택 해제
      });
      unhighlightPlacedButtons();
      highlightPlacedButton(btn);
      showMovablePlaces(piece);
    });
    btn.setMouseTransparent(false);
    return btn;
  }

  private Button createDestButton(Color color) {
    Color light = color.interpolate(Color.WHITE, 0.2);
    Button btn = new Button();
    btn.setPrefSize(DEST_BTN_D, DEST_BTN_D);
    btn.setMinSize(DEST_BTN_D, DEST_BTN_D);
    btn.setMaxSize(DEST_BTN_D, DEST_BTN_D);
    btn.setStyle(Utils.roundStyle(light));
    btn.setMouseTransparent(false);
    return btn;
  }

  private Button createEndButton() {
    Button btn = new Button("끝");
    btn.setPrefSize(END_BTN_D, END_BTN_D);
    btn.setStyle(Utils.roundStyle(Color.WHITE));
    btn.setMouseTransparent(false);
    return btn;
  }

  private void highlightPlacedButton(Button b) {
    b.setStyle(Utils.ringStyle(Color.BLACK));
  }

  private void unhighlightPlacedButtons() {
    placedPieceButtons.forEach(btn -> btn.setStyle(Utils.ringStyle(Color.WHITE)));
  }

  // Cleanup helpers
  private void addMovableButton(Button b) {
    movablePlaceButtons.add(b);
    getChildren().add(b);
  }

  private void removeButton(Button b) {
    getChildren().remove(b);
  }

  private void clearAllButtons() {
    getChildren().removeAll(placedPieceButtons);
    getChildren().removeAll(movablePlaceButtons);
    unstartedPieceControl.ifPresent(pieceControl -> getChildren().remove(pieceControl));
    unstartedPieceControl = Optional.empty();
    placedPieceButtons.clear();
    movablePlaceButtons.clear();
    selectedPiece = null;
  }
}

class Utils {

  public static String innerRoundstyle(Color fill) {
    return
        "-fx-border-color: transparent, " + toHex(fill) + ";" +
            "-fx-border-width: 0, 3;" +
            "-fx-border-insets: 0;";
  }

  // Styling helpers
  public static String roundStyle(Color fill) {
    return "-fx-background-radius: 100; -fx-background-color: " + toHex(fill) + ";"
        + "-fx-border-radius: 100; -fx-border-color: transparent;";
  }

  public static String ringStyle(Color stroke) {
    return "-fx-background-color: transparent; -fx-background-radius: 100; "
        + "-fx-border-radius: 100; -fx-border-width: 3; -fx-border-color: " + toHex(stroke) + ";";
  }

  public static String toHex(Color c) {
    return String.format("#%02x%02x%02x", (int) (c.getRed() * 255), (int) (c.getGreen() * 255),
        (int) (c.getBlue() * 255));
  }
}