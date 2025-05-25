package org.view.jfx;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Optional;

import org.core.domain.board.BoardType;
import org.core.domain.game.GameDecision;
import org.core.domain.piece.GamePieces;
import org.core.domain.yut.*;
import org.core.dto.GameInitializeDto;
import org.core.dto.YutGenerationRequest;
import org.core.state.turn.TurnStateMachine;

/**
 * DialogUtil (JavaFX)
 *
 * Swing {@code org.view.swing.DialogUtil} 과 동일한 정적 API를 제공하지만,
 * 구현은 JavaFX {@link Alert}, {@link TextInputDialog}, {@link ChoiceDialog}를 사용합니다.
 */
public final class DialogUtil {

    private DialogUtil() {}

    /* -------------------------------------------------- 초기 설정 -------------------------------------------------- */

    /**
     * 팀 수, 말 수, 보드 타입을 차례대로 입력 받아 {@link GameInitializeDto} 를 반환합니다.
     */
    public static GameInitializeDto showInitDialog(Window owner) {
        int teamCnt  = askInt(owner, "몇 명의 팀으로 진행할까요? (2~4)");
        int pieceCnt = askInt(owner, "몇 개의 말로 진행할까요? (2~5)");
        int type     = askInt(owner, "어떤 보드로 진행할까요? (4,5,6)");
        return new GameInitializeDto(teamCnt, pieceCnt, BoardType.mapTo(type));
    }

    /* -------------------------------------------------- 윷 던지기 -------------------------------------------------- */

    public static YutGenerationRequest askYutGeneration(Window owner,
                                                        TurnStateMachine turnSM) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(owner);
        alert.setHeaderText(null);
        alert.setTitle("윷 던지기 방식");
        alert.setContentText(turnSM.context.turn.getTurn() + "팀이 윷을 던질 차례입니다. 어떻게 던지시겠습니까?");

        ButtonType designated = new ButtonType("지정", ButtonData.LEFT);
        ButtonType random     = new ButtonType("랜덤",  ButtonData.RIGHT);
        alert.getButtonTypes().setAll(designated, random);

        Optional<ButtonType> opt = alert.showAndWait();
        if (opt.isPresent() && opt.get() == designated) {
            String input = askString(owner, "어떤 윷을 던지시겠습니까? (빽도/도/개/걸/윷/모)");
            YutResult res = YutResult.from(input);
            return new YutGenerationRequest(YutGenerateOptions.DESIGNATED, res);
        }
        return new YutGenerationRequest(YutGenerateOptions.RANDOM, null);
    }

    /* -------------------------------------------------- 선택 다이얼로그 -------------------------------------------------- */

    public static YutResult chooseYutResult(Window owner, List<YutResult> list) {
        ChoiceDialog<YutResult> dialog = new ChoiceDialog<>(list.get(0), list);
        dialog.initOwner(owner);
        dialog.setTitle("윷 결과 선택");
        dialog.setHeaderText(null);
        dialog.setContentText("윷 결과를 선택하세요:");
        return dialog.showAndWait().orElse(list.get(0));
    }

    public static GamePieces chooseMovingPiece(Window owner, List<GamePieces> list) {
        /* ── 라벨 문자열 생성 ───────────────────────────────────────────── */
        String[] labels = list.stream().map(gp -> {
            String joined = gp.getPieces().stream()
                    .map(pi -> String.valueOf(pi.getPieceNumber()))
                    .reduce((a, b) -> a + "," + b).orElse("");
            return gp.getTeam() + "팀: " + joined;
        }).toArray(String[]::new);

        /* ── ChoiceDialog 준비 ─────────────────────────────────────────── */
        // 선택지는 0,1,2 … 인덱스
        ChoiceDialog<Integer> dialog =
                new ChoiceDialog<>(0, createIndexList(list.size()));
        dialog.initOwner(owner);
        dialog.setHeaderText(null);
        dialog.setTitle("말 선택");
        dialog.setContentText("이동할 말을 선택하세요:");
        dialog.setGraphic(null);

        /* ── ChoiceBox 찾아서 라벨 컨버터 연결 ──────────────────────────── */
        ChoiceBox<Integer> box =
                (ChoiceBox<Integer>) dialog.getDialogPane().lookup(".choice-box");
        if (box != null) {
            box.setConverter(new StringConverter<>() {
                @Override public String toString(Integer idx) { return labels[idx]; }
                @Override public Integer fromString(String s) { return 0; }
            });
        }

        /* ── 결과 반환 ──────────────────────────────────────────────────── */
        int sel = dialog.showAndWait().orElse(0);
        return list.get(sel);
    }

    public static String chooseMovingPlace(Window owner, List<String> places) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(places.get(0), places);
        dialog.initOwner(owner);
        dialog.setHeaderText(null);
        dialog.setTitle("이동 위치 선택");
        dialog.setContentText("위치를 선택하세요:");
        return dialog.showAndWait().orElse(places.get(0));
    }

    /* -------------------------------------------------- 게임 종료 결정 -------------------------------------------------- */

    public static GameDecision askGameDecision(Window owner) {
        Alert alert = new Alert(AlertType.CONFIRMATION,
                "재시작하시겠습니까?", ButtonType.YES, ButtonType.NO);
        alert.initOwner(owner);
        alert.setHeaderText("Game Over");
        Optional<ButtonType> res = alert.showAndWait();
        return res.isPresent() && res.get() == ButtonType.YES ?
                GameDecision.RESTART : GameDecision.EXIT;
    }

    /* -------------------------------------------------- 내부 유틸 -------------------------------------------------- */

    private static int askInt(Window owner, String prompt) {
        while (true) {
            String s = askString(owner, prompt);
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException ex) {
                showError(owner, "숫자를 입력해주세요");
            }
        }
    }

    private static String askString(Window owner, String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(owner);
        dialog.setHeaderText(null);
        dialog.setContentText(prompt);
        Optional<String> res = dialog.showAndWait();
        return res.orElse("");
    }

    private static void showError(Window owner, String msg) {
        Alert alert = new Alert(AlertType.ERROR, msg, ButtonType.OK);
        alert.initOwner(owner);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private static List<Integer> createIndexList(int size) {
        return java.util.stream.IntStream.range(0, size).boxed().toList();
    }
}
