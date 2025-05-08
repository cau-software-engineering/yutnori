package org.core.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class GamePiecesManagerTest {

    private static final String GAME_START_PLACE_NAME = "start";

    @DisplayName("내 팀의 모든 게임말을 찾는다")
    @Test
    void findAllPiecesByTeam() {
        GamePiecesManager gamePiecesManager = new GamePiecesManager(new GamePieceInitializer(), 2, 1);

        List<GamePieces> oneTeamPieces = gamePiecesManager.findAllPiecesByTeam(1);

        assertAll(
                () -> assertThat(oneTeamPieces).hasSize(1),
                () -> assertThat(oneTeamPieces.get(0).getTeam()).isEqualTo(1)
        );
    }

    @DisplayName("특정 위치에 있는 잡을 수 있는 말들(상대팀 말)을 모두 반환한다")
    @Test
    void findCatchablePieces() {
        int teamNumber = 1;
        int oppositeTeamNumber = 2;
        GamePiecesManager gamePiecesManager = new GamePiecesManager(new GamePieceInitializer(), 2, 2);

        List<GamePieces> oppositeTeamPieces = gamePiecesManager.findCatchablePieces(GAME_START_PLACE_NAME, teamNumber);

        assertAll(
                () -> assertThat(oppositeTeamPieces).hasSize(2),
                () -> assertThat(oppositeTeamPieces.get(0).getTeam()).isEqualTo(oppositeTeamNumber),
                () -> assertThat(oppositeTeamPieces.get(1).getTeam()).isEqualTo(oppositeTeamNumber)
        );
    }

    @DisplayName("특정 위치에 있는 업을 수 있는 말들(우리팀 말)을 모두 반환한다")
    @Test
    void findGroupablePieces() {
        int teamNumber = 1;
        GamePiecesManager gamePiecesManager = new GamePiecesManager(new GamePieceInitializer(), 2, 2);

        List<GamePieces> sameTeamPieces = gamePiecesManager.findGroupablePieces(GAME_START_PLACE_NAME, teamNumber);

        assertAll(
                () -> assertThat(sameTeamPieces).hasSize(2),
                () -> assertThat(sameTeamPieces.get(0).getTeam()).isEqualTo(teamNumber),
                () -> assertThat(sameTeamPieces.get(1).getTeam()).isEqualTo(teamNumber)
        );
    }

    @DisplayName("특정 말을 다른 말에 업을 수 있다")
    @Test
    void groupPieces() {
        GamePiecesManager gamePiecesManager = new GamePiecesManager(new GamePieceInitializer(), 2, 2);
        List<GamePieces> oneTeamPieces = gamePiecesManager.findAllPiecesByTeam(1);
        String groupPieceId1 = oneTeamPieces.get(0).getId();
        String groupPieceId2 = oneTeamPieces.get(1).getId();

        gamePiecesManager.groupPieces(groupPieceId1, groupPieceId2, "S0");

        List<GamePieces> afterGroupPiece = gamePiecesManager.findAllPiecesByTeam(1);
        assertThat(afterGroupPiece).hasSize(1);
    }

    @DisplayName("특정 말의 위치를 옮길 수 있다")
    @Test
    void moveTo() {
        GamePiecesManager gamePiecesManager = new GamePiecesManager(new GamePieceInitializer(), 2, 1);
        List<GamePieces> oneTeamPieces = gamePiecesManager.findAllPiecesByTeam(1);
        String movingPieceId = oneTeamPieces.get(0).getId();

        gamePiecesManager.moveTo(movingPieceId, "A3");

        GamePieces movingPiece = gamePiecesManager.findAllPiecesByTeam(1)
                .stream()
                .filter(gamePieces -> gamePieces.getId().equals(movingPieceId))
                .findAny()
                .orElseThrow(RuntimeException::new);

        assertThat(movingPiece.getPlace()).isEqualTo("A3");
    }

    @DisplayName("특정 말을 잡는다")
    @Test
    void catchPiece() {
        GamePiecesManager gamePiecesManager = new GamePiecesManager(new GamePieceInitializer(), 2, 1);
        List<GamePieces> oneTeamPieces = gamePiecesManager.findAllPiecesByTeam(1);
        String catchedPieceId = oneTeamPieces.get(0).getId();
        gamePiecesManager.moveTo(catchedPieceId, "A3");

        gamePiecesManager.catchPiece(catchedPieceId);

        List<GamePieces> afterCatchPiece = gamePiecesManager.findAllPiecesByTeam(1);
        assertAll(
                () -> assertThat(afterCatchPiece).hasSize(1),
                () -> assertThat(afterCatchPiece.get(0).getPlace()).isEqualTo(GAME_START_PLACE_NAME)
        );
    }

    @DisplayName("말 잡기 시나리오 테스트")
    @TestFactory
    Stream<DynamicTest> catchPieces() {
        GamePiecesManager gamePiecesManager = new GamePiecesManager(new GamePieceInitializer(), 2, 2);
        List<GamePieces> oneTeamPieces = gamePiecesManager.findAllPiecesByTeam(1);
        String oneTeamPiece1 = oneTeamPieces.get(0).getId();
        String oneTeamPiece2 = oneTeamPieces.get(1).getId();

        return Stream.of(
                dynamicTest("1팀의 첫번째 말을 A3으로 이동한다", () -> {
                    gamePiecesManager.moveTo(oneTeamPiece1, "A3");
                }),
                dynamicTest("1팀의 두번째 말을 A3으로 이동한다", () -> {
                    gamePiecesManager.moveTo(oneTeamPiece2, "A3");
                }),
                dynamicTest("1팀의 첫번째 말과 두번째 말을 업는다", () -> {
                    gamePiecesManager.groupPieces(oneTeamPiece1, oneTeamPiece2, "S0");
                    List<GamePieces> afterGroupPiece = gamePiecesManager.findAllPiecesByTeam(1);

                    assertAll(
                            () -> assertThat(afterGroupPiece).hasSize(1),
                            () -> assertThat(afterGroupPiece.get(0).getPlace()).isEqualTo("A3")
                    );
                }),
                dynamicTest("1팀의 업은 말을 잡는다 -> 업었던 두 말은 각각 쪼개져 시작지점으로 돌아간다", () -> {
                    List<GamePieces> afterGroupPiece = gamePiecesManager.findAllPiecesByTeam(1);
                    String catchedPieceId = afterGroupPiece.get(0).getId();

                    gamePiecesManager.catchPiece(catchedPieceId);

                    List<GamePieces> afterCatchPiece = gamePiecesManager.findAllPiecesByTeam(1);

                    assertAll(
                            () -> assertThat(afterCatchPiece).hasSize(2),
                            () -> assertThat(afterCatchPiece.get(0).getPlace()).isEqualTo(GAME_START_PLACE_NAME),
                            () -> assertThat(afterCatchPiece.get(1).getPlace()).isEqualTo(GAME_START_PLACE_NAME)
                    );
                })
        );
    }
}
