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
 * PieceDrawing (JavaFX)
 */
public final class PieceDrawing extends Pane {

    private static final double PIECE_R = 10; // 반지름 (원형 크기)

    private final GameStateMachine gameSM;
    private final TurnStateMachine turnSM;
    private final StoreFX          store;

    private final List<Circle> circles = new ArrayList<>();


    public PieceDrawing(GameStateMachine gameSM,
                        TurnStateMachine turnSM,
                        StoreFX store)
    {
        this.gameSM = gameSM;
        this.turnSM = turnSM;
        this.store  = store;

        setPickOnBounds(false);     // 빈 영역 클릭은 통과 (BoardPanel 이벤트용)
        setMouseTransparent(true);  // 자체적으로는 클릭 처리 안 함

        gameSM.observe(s -> Platform.runLater(this::refresh));
        turnSM.observe(s -> Platform.runLater(this::refresh));

        refresh(); // 초기 1회
    }

    public void refresh() {

        getChildren().removeAll(circles);
        circles.clear();

        int turn = turnSM.context.turn.getTurn();
        BoardService bs = gameSM.context.boardService;
        List<GamePieces> allPieces = bs.findAllPiecesByTeam(turn);

        for (int team = 1; team <= store.getTeamCount(); team++) {
            for (GamePieces gp : bs.findAllPiecesByTeam(team)) {
                if ("start".equals(gp.getPlace())) continue;   // 아직 출발 안 한 말은 스킵
                Point2D pos = store.getNodePos(gp.getPlace());
                Circle c = new Circle(pos.getX(), pos.getY(), PIECE_R, store.getPalette(gp.getTeam()));
                c.setStroke(Color.BLACK);
                circles.add(c);
            }
        }

        getChildren().addAll(circles);
    }
}
