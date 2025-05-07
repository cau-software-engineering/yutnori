package org.view.swing;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.core.domain.piece.GamePieces;
import org.core.domain.yut.YutResult;
import org.core.service.BoardService;
import org.core.domain.game.GameDecision;
import org.core.state.game.GameStateMachine;
import org.core.state.game.event.GameOverEvent;
import org.core.state.game.state.GameOverState;
import org.core.state.turn.TurnStateMachine;
import org.core.state.turn.event.TurnGenerateYutEvent;
import org.core.state.turn.event.TurnMovePieceEvent;
import org.core.state.turn.state.*;

public class ControlPanel extends JPanel {

    private final GameStateMachine gameSM;
    private final TurnStateMachine turnSM;
    private final JFrame owner;

    private final JTextArea logArea = new JTextArea();
    private final JButton throwBtn  = new JButton("윷 던지기");

    public ControlPanel(GameStateMachine gameSM,
                        TurnStateMachine turnSM,
                        JFrame owner) {
        super(new BorderLayout());
        this.gameSM = gameSM;
        this.turnSM = turnSM;
        this.owner  = owner;

        buildUI();
        registerObservers();
    }

    // UI
    private void buildUI() {
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        throwBtn.addActionListener(e -> onClickThrow());
        JPanel south = new JPanel();
        south.add(throwBtn);
        add(south, BorderLayout.SOUTH);
    }

    // Observers
    private void registerObservers() {

        /* GameState */
        gameSM.observe(st -> {
            if (st instanceof GameOverState) {
                GameDecision d = DialogUtil.askGameDecision(owner);
                if (d == GameDecision.RESTART) {
                    owner.dispose();
                    new SwingView();
                } else {
                    System.exit(0);
                }
            }
        });

        /* TurnState */
        turnSM.observe(st -> SwingUtilities.invokeLater(() -> handleTurnState(st)));
    }

    // handle turn state
    private void handleTurnState(Object st) {

        if (st instanceof TurnIdleState) {
            if (gameSM.isGameOver()) {
                gameSM.dispatchEvent(new GameOverEvent());
                return;
            }
            append("\n--- " + turnSM.context.turn.getTurn() + "팀 차례 ---");
            throwBtn.setEnabled(true);

        } else if (st instanceof TurnRegeneratingState) {
            append("윷을 또 던질 수 있어요");
            throwBtn.setEnabled(true);

        } else if (st instanceof TurnWaitForActionState) {

            YutResult yutRes = DialogUtil.chooseYutResult(owner,
                    turnSM.context.getYutResults());

            GamePieces movingPiece = DialogUtil.chooseMovingPiece(owner,
                    gameSM.context.boardService.findAllPiecesByTeam(
                            turnSM.context.turn.getTurn()));

            BoardService bs = gameSM.context.boardService;
            List<String> places = bs.findMovablePlaces(movingPiece.getPlace(), yutRes);
            String place = DialogUtil.chooseMovingPlace(owner, places);

            turnSM.dispatchEvent(new TurnMovePieceEvent(
                    movingPiece.getId(), place, yutRes));

        } else if (st instanceof TurnKilledOtherState) {
            append("상대 말을 잡았습니다!");

        } else if (st instanceof TurnTookPieceState) {
            append("나의 말을 업었습니다!");
        }
    }

    //
    private void onClickThrow() {
        throwBtn.setEnabled(false);

        var req = DialogUtil.askYutGeneration(owner, turnSM);

        if (req.options() == org.core.domain.yut.YutGenerateOptions.DESIGNATED) {
            append("지정 윷 결과 → " + req.yutResult().getName());
        } else {
            append("랜덤으로 윷을 던집니다.");
        }
        turnSM.dispatchEvent(new TurnGenerateYutEvent(req));
    }

    private void append(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}
