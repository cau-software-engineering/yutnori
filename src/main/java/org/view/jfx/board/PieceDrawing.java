package org.view.jfx.board;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.core.domain.piece.GamePieces;
import org.core.service.BoardService;
import org.core.state.game.GameStateMachine;
import org.core.state.turn.TurnStateMachine;
import org.view.jfx.StoreFX;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX replacement for Swing {@code org.view.swing.board.PieceDrawing}.
 * <p>
 * ● 모든 말을 원형(Circle)으로 그린다. (크기 = 20px)<br>
 * ● 색상은 {@link StoreFX#getPalette(int)} 로부터 팀 색상을 가져와 채움(fill)으로 사용.<br>
 * ● 턴 / 상태 변화마다 {@link #refresh()} 가 호출돼 최신 보드 상태를 반영한다.
 * </p>
 */
public final class PieceDrawing extends Pane {

    // ──────────────────────────────────────────────────────────────────────────
    // Constants
    // ──────────────────────────────────────────────────────────────────────────

    private static final double PIECE_R = 10; // 반지름 (원형 크기)

    // ──────────────────────────────────────────────────────────────────────────
    // Dependencies
    // ──────────────────────────────────────────────────────────────────────────

    private final GameStateMachine gameSM;
    private final TurnStateMachine turnSM;
    private final StoreFX          store;

    // ──────────────────────────────────────────────────────────────────────────
    // Internal state
    // ──────────────────────────────────────────────────────────────────────────

    /** 현재 화면에 그려진 원형(말) 노드 모음 */
    private final List<Circle> circles = new ArrayList<>();

    // ──────────────────────────────────────────────────────────────────────────
    // Construction
    // ──────────────────────────────────────────────────────────────────────────

    public PieceDrawing(GameStateMachine gameSM,
                        TurnStateMachine turnSM,
                        StoreFX store)
    {
        this.gameSM = gameSM;
        this.turnSM = turnSM;
        this.store  = store;

        setPickOnBounds(false);     // 빈 영역 클릭은 통과 (BoardPanel 이벤트용)
        setMouseTransparent(true);  // 자체적으로는 클릭 처리 안 함

        // 게임/턴 상태가 바뀔 때마다 새로 그린다.
        gameSM.observe(s -> Platform.runLater(this::refresh));
        turnSM.observe(s -> Platform.runLater(this::refresh));

        refresh(); // 초기 1회
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Drawing logic
    // ──────────────────────────────────────────────────────────────────────────

    /** BoardService 의 최신 말 위치 데이터를 읽어 화면을 갱신 */
    public void refresh() {
        // 기존 말 노드 제거
        getChildren().removeAll(circles);
        circles.clear();

        int turn = turnSM.context.turn.getTurn();
        BoardService bs = gameSM.context.boardService;
        List<GamePieces> allPieces = bs.findAllPiecesByTeam(turn);

        for (GamePieces gp : allPieces) {
            // "start" 위치(아직 출발 안 한 말)는 그리지 않음
            if ("start".equals(gp.getPlace())) continue;

            Point2D pos = store.getNodePos(gp.getPlace());
            Color    col = store.getPalette(gp.getTeam());

            Circle c = new Circle(pos.getX(), pos.getY(), PIECE_R, col);
            c.setStroke(Color.BLACK);
            c.setStrokeWidth(1);

            circles.add(c);
        }

        getChildren().addAll(circles);
    }
}
