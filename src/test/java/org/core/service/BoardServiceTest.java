package org.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.List;
import java.util.stream.Stream;
import org.core.domain.board.BoardType;
import org.core.domain.piece.GamePieces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class BoardServiceTest {

    @DisplayName("말 잡기 예외상황 테스트")
    @TestFactory
    Stream<DynamicTest> catchPieces() {

        BoardService boardService = new BoardService(2, 2, BoardType.SQUARE);
        GamePieces firstTeamPiece1 = boardService.findAllPiecesByTeam(1).get(0);
        GamePieces firstTeamPiece2 = boardService.findAllPiecesByTeam(1).get(1);

        GamePieces secondTeamPiece1 = boardService.findAllPiecesByTeam(2).get(0);
        GamePieces secondTeamPiece2 = boardService.findAllPiecesByTeam(2).get(1);

        return Stream.of(
                dynamicTest("1팀 말을 시작노드인 S0으로, 2팀 말을 종료 노드로 이동한다", () -> {
                    boardService.moveTo(firstTeamPiece1.getId(), "S0");
                    boardService.moveTo(secondTeamPiece1.getId(), "S5");
                }),
                dynamicTest("1팀은 종료노드로 이동할 때 시작노드에 있는 말을 업을 수 있다", () -> {
                    List<GamePieces> groupablPieces = boardService.findGroupablePieces("S5", 1);
                    assertAll(
                            () -> assertThat(groupablPieces).hasSize(1),
                            () -> assertThat(groupablPieces.get(0).getPlace()).isEqualTo("S0")
                    );
                }),
                dynamicTest("1팀은 시작노드로 이동할 때 종료노드에 있는 2팀의 말을 잡을 수 있다", () -> {
                    List<GamePieces> groupablPieces = boardService.findCatchablePieces("S0", 1);
                    assertAll(
                            () -> assertThat(groupablPieces).hasSize(1),
                            () -> assertThat(groupablPieces.get(0).getPlace()).isEqualTo("S5")
                    );
                }),
                dynamicTest("2팀은 종료노드로 이동할 때 시작노드에 있는 1팀의 말을 잡을 수 있다", () -> {
                    List<GamePieces> groupablPieces = boardService.findCatchablePieces("S5", 2);
                    assertAll(
                            () -> assertThat(groupablPieces).hasSize(1),
                            () -> assertThat(groupablPieces.get(0).getPlace()).isEqualTo("S0")
                    );
                }),
                dynamicTest("2팀은 시작노드로 이동할 때 종료노드에 있는 자신의 말에 업을 수 있다", () -> {
                    List<GamePieces> groupablPieces = boardService.findGroupablePieces("S0", 2);
                    assertAll(
                            () -> assertThat(groupablPieces).hasSize(1),
                            () -> assertThat(groupablPieces.get(0).getPlace()).isEqualTo("S5")
                    );
                }),
                dynamicTest("1팀의 두번째 말을 종료노드 S5로 옮긴다", () -> {
                    boardService.moveTo(firstTeamPiece2.getId(), "S5");
                }),
                dynamicTest("1팀의 말을 업으면 종료노드를 기준으로 업혀진다", () -> {
                    GamePieces gamePieces = boardService.groupPieces(
                            firstTeamPiece1.getId(),
                            firstTeamPiece2.getId()
                    );
                    assertThat(gamePieces.getPlace()).isEqualTo("S5");
                })
        );
    }

}
