package org.view.jfx.board;

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
import org.view.jfx.StoreFX;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX version of {@code org.view.swing.board.MoveControl}.
 * <p>
 * ● 선택 버튼 (말 위치 ‐ 링 형태)<br>
 * ● 미출발 말 번호 버튼 (원형, 하단)<br>
 * ● 이동 가능 노드 버튼 (작은 원형)<br>
 * </p>
 * 스테이트 머신이 {@link TurnWaitForActionState} 가 되면 말 선택지를 표시하고,
 * 이동 이벤트가 발생하면 버튼들이 사라집니다.
 */
public final class MoveControl extends Pane {

    // ──────────────────────────────────────────────────────────────────────────
    // Constants
    // ──────────────────────────────────────────────────────────────────────────

    private static final double PIECE_BTN_D      = 40;  // 말 선택 링 버튼 지름
    private static final double UNSTART_BTN_D    = 50;  // 미출발 말 번호 버튼 지름
    private static final double DEST_BTN_D       = 20;  // 목적지 버튼 지름
    private static final double END_BTN_D        = 40;  // "끝" 버튼 지름

    // ──────────────────────────────────────────────────────────────────────────
    // Dependencies
    // ──────────────────────────────────────────────────────────────────────────

    private final GameStateMachine gameSM;
    private final TurnStateMachine turnSM;
    private final StoreFX          store;

    // ──────────────────────────────────────────────────────────────────────────
    // Internal state
    // ──────────────────────────────────────────────────────────────────────────

    private GamePieces                 selectedPiece;
    private final List<Button>         placedPieceButtons   = new ArrayList<>();
    private final List<Button>         unstartedPieceButtons= new ArrayList<>();
    private final List<Button>         movablePlaceButtons  = new ArrayList<>();

    // ──────────────────────────────────────────────────────────────────────────
    // Construction
    // ──────────────────────────────────────────────────────────────────────────

    public MoveControl(GameStateMachine gameSM,
                       TurnStateMachine turnSM,
                       StoreFX store)
    {
        this.gameSM = gameSM;
        this.turnSM = turnSM;
        this.store  = store;

        // BoardPanel 위에 투명 레이어로 동작해야 하므로 패널 자체는 클릭 비활성화
        setPickOnBounds(false);
        // NOTE: 부모 Pane 은 빈 영역(배경 부분)만 클릭을 무시하고,
        // 자식 버튼들은 정상적으로 이벤트를 받아야 하므로 mouseTransparent 는 false 로 둡니다.
        // 마우스 투과를 원할 경우 자식 노드에 개별 설정을 하세요.
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

    // ──────────────────────────────────────────────────────────────────────────
    // Main UI flow
    // ──────────────────────────────────────────────────────────────────────────

    private void showActions() {
        clearAllButtons();

        int turn = turnSM.context.turn.getTurn();
        List<GamePieces> list = gameSM.context.boardService.findAllPiecesByTeam(turn);

        for (int idx = 0; idx < list.size(); idx++) {
            GamePieces piece = list.get(idx);
            Button btn;
            if (!"start".equals(piece.getPlace())) {
                // ─ placed pieces -------------------------------------------------
                btn = createPlacedPieceButton(piece);
                placedPieceButtons.add(btn);
            } else {
                // ─ unstarted pieces ---------------------------------------------
                btn = createUnstartedPieceButton(idx, list.size(), piece);
                unstartedPieceButtons.add(btn);
            }
            getChildren().add(btn);
        }
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
                    // "끝" 버튼 — 좌상단 고정
                    Button endBtn = createEndButton();
                    endBtn.setOnAction(e -> onMove(piece, place, yutResult));
                    endBtn.setLayoutX(20);
                    endBtn.setLayoutY(20);
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

    // ──────────────────────────────────────────────────────────────────────────
    // Event helpers
    // ──────────────────────────────────────────────────────────────────────────

    private void onMove(GamePieces piece, String destNode, YutResult yutResult) {
        clearAllButtons();
        Platform.runLater(() ->
                turnSM.dispatchEvent(new TurnMovePieceEvent(piece.getId(), destNode, yutResult))
        );
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Button factory
    // ──────────────────────────────────────────────────────────────────────────

    private Button createPlacedPieceButton(GamePieces piece) {
        Point2D p = store.getNodePos(piece.getPlace());
        Button btn = new Button();
        btn.setPrefSize(PIECE_BTN_D, PIECE_BTN_D);
        btn.setStyle(ringStyle(store.getPalette(piece.getTeam())));
        btn.setLayoutX(p.getX() - PIECE_BTN_D / 2);
        btn.setLayoutY(p.getY() - PIECE_BTN_D / 2);

        btn.setOnAction(e -> {
            selectedPiece = piece;
            highlightPlacedButton(btn);
            showMovablePlaces(piece);
            unhighlightUnstartedButtons();
        });
        btn.setMouseTransparent(false);
        return btn;
    }

    private Button createUnstartedPieceButton(int idx, int total, GamePieces piece) {
        double x = 540 - ((total - idx - 1) * 60);
        double y = 540;
        Button btn = new Button(String.valueOf(idx + 1));
        btn.setPrefSize(UNSTART_BTN_D, UNSTART_BTN_D);
        btn.setStyle(roundStyle(store.getPalette(piece.getTeam())));
        btn.setLayoutX(x);
        btn.setLayoutY(y);

        btn.setOnAction(e -> {
            selectedPiece = piece;
            highlightUnstartedButton(btn);
            showMovablePlaces(piece);
            unhighlightPlacedButtons();
        });
        btn.setMouseTransparent(false);
        return btn;
    }

    private Button createDestButton(Color color) {
        Button btn = new Button();
        btn.setPrefSize(DEST_BTN_D, DEST_BTN_D);
        btn.setStyle(roundStyle(color));
        btn.setMouseTransparent(false);
        return btn;
    }

    private Button createEndButton() {
        Button btn = new Button("끝");
        btn.setPrefSize(END_BTN_D, END_BTN_D);
        btn.setStyle(roundStyle(Color.DARKGRAY));
        btn.setMouseTransparent(false);
        return btn;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Styling helpers
    // ──────────────────────────────────────────────────────────────────────────

    private static String roundStyle(Color fill) {
        return "-fx-background-radius: 100; -fx-background-color: " + toHex(fill) + ";"
                + "-fx-border-radius: 100; -fx-border-color: transparent;";
    }

    private static String ringStyle(Color stroke) {
        return "-fx-background-color: transparent; -fx-background-radius: 100; "
                + "-fx-border-radius: 100; -fx-border-width: 3; -fx-border-color: " + toHex(stroke) + ";";
    }

    private static String toHex(Color c) {
        return String.format("#%02x%02x%02x", (int)(c.getRed()*255), (int)(c.getGreen()*255), (int)(c.getBlue()*255));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Utility – highlight / clear helpers
    // ──────────────────────────────────────────────────────────────────────────

    private void highlightPlacedButton(Button b) {
        placedPieceButtons.forEach(btn -> btn.setStyle(ringStyle(Color.web("#FFFFFF"))));
        b.setStyle(ringStyle(Color.LIMEGREEN));
    }

    private void highlightUnstartedButton(Button b) {
        unstartedPieceButtons.forEach(btn -> btn.setStyle(btn.getStyle().replace("-fx-border-color: lime;", "")));
        b.setStyle(b.getStyle() + " -fx-border-width:3; -fx-border-color: lime; -fx-border-radius:100;");
    }

    private void unhighlightPlacedButtons() {
        placedPieceButtons.forEach(btn -> btn.setStyle(ringStyle(Color.web("#FFFFFF"))));
    }

    private void unhighlightUnstartedButtons() {
        unstartedPieceButtons.forEach(btn -> btn.setStyle(btn.getStyle().replace("-fx-border-color: lime;", "")));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Cleanup helpers
    // ──────────────────────────────────────────────────────────────────────────

    private void addMovableButton(Button b) {
        movablePlaceButtons.add(b);
        getChildren().add(b);
    }

    private void removeButton(Button b) { getChildren().remove(b); }

    private void clearAllButtons() {
        getChildren().removeAll(placedPieceButtons);
        getChildren().removeAll(unstartedPieceButtons);
        getChildren().removeAll(movablePlaceButtons);
        placedPieceButtons.clear();
        unstartedPieceButtons.clear();
        movablePlaceButtons.clear();
        selectedPiece = null;
    }
}
