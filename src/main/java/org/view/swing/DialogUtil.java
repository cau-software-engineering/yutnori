package org.view.swing;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.core.domain.board.BoardType;
import org.core.domain.game.GameDecision;
import org.core.domain.piece.GamePieces;
import org.core.domain.yut.*;
import org.core.dto.*;
import org.core.state.turn.TurnStateMachine;

public final class DialogUtil {

    private DialogUtil() {}

    // Init
    public static GameInitializeDto showInitDialog(Component parent) {
        int teamCnt  = Integer.parseInt(
                JOptionPane.showInputDialog(parent, "몇 명의 팀으로 진행할까요? (2~4)"));
        int pieceCnt = Integer.parseInt(
                JOptionPane.showInputDialog(parent, "몇 개의 말로 진행할까요? (1~5)"));
        int type     = Integer.parseInt(
                JOptionPane.showInputDialog(parent, "어떤 보드로 진행할까요? (4,5,6)"));
        return new GameInitializeDto(teamCnt, pieceCnt, BoardType.mapTo(type));
    }

    // Yut generate
    public static YutGenerationRequest askYutGeneration(Component parent,
                                                        TurnStateMachine turnSM) {

        int opt = JOptionPane.showOptionDialog(
                parent,
                turnSM.context.turn.getTurn() + "팀이 윷을 던질 차례입니다.",
                "어떻게 던지시겠습니까?",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new String[]{"지정", "랜덤"}, "랜덤"
        );

        if (opt == 0) {
            String input = JOptionPane.showInputDialog(
                    parent, "어떤 윷을 던지시겠습니까? (빽도/도/개/걸/윷/모)");
            YutResult res = YutResult.from(input);
            return new YutGenerationRequest(YutGenerateOptions.DESIGNATED, res);
        }
        return new YutGenerationRequest(YutGenerateOptions.RANDOM, null);
    }

    // Choose Yuts
    public static YutResult chooseYutResult(Component p, List<YutResult> list) {
        String[] names = list.stream().map(YutResult::getName).toArray(String[]::new);
        int sel = JOptionPane.showOptionDialog(p, "윷 결과 선택", "선택",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, names, names[0]);
        return list.get(sel);
    }

    // Choose pieces
    public static GamePieces chooseMovingPiece(Component parent,
                                               List<GamePieces> list) {

        String[] labels = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            GamePieces gp = list.get(i);
            String joined = gp.getPieces().stream()
                    .map(pi -> String.valueOf(pi.getPieceNumber()))
                    .reduce((a,b)->a+","+b).orElse("");
            labels[i] = gp.getTeam()+"팀: "+joined;
        }
        int sel = JOptionPane.showOptionDialog(parent, "말 선택", "선택",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, labels, labels[0]);
        return list.get(sel);
    }

    // Choose Place
    public static String chooseMovingPlace(Component parent, List<String> places) {
        int sel = JOptionPane.showOptionDialog(parent, "이동 위치 선택", "선택",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, places.toArray(), places.get(0));
        return places.get(sel);
    }

    // Checking game decision
    public static GameDecision askGameDecision(Component parent) {
        int sel = JOptionPane.showConfirmDialog(parent,
                "재시작하시겠습니까?", "Game Over", JOptionPane.YES_NO_OPTION);
        return sel == JOptionPane.YES_OPTION ?
                GameDecision.RESTART : GameDecision.EXIT;
    }
}
