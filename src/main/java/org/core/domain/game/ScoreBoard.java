package org.core.domain.game;

import java.util.HashMap;
import java.util.Map;

public class ScoreBoard {

    private static final int MIN_PIECE_NUMBER = 2;
    private static final int MAX_PIECE_NUMBER = 5;
    private static final int MIN_TEAM_NUMBER = 2;
    private static final int MAX_TEAM_NUMBER = 4;
    private static final int INITIAL_SCORE = 0;

    private final int pieceCount;
    private final Map<Integer, Integer> scores;

    public ScoreBoard(int teamNumber, int pieceCount) {
        validateTeamNumber(teamNumber);
        validatePieceNumber(pieceCount);

        this.pieceCount = pieceCount;
        this.scores = initializeScores(teamNumber);
    }

    private Map<Integer, Integer> initializeScores(int teamNumber) {
        Map<Integer, Integer> scores = new HashMap<>();
        for (int i = 1; i <= teamNumber; i++) {
            scores.put(i, INITIAL_SCORE);
        }
        return scores;
    }

    private void validatePieceNumber(int pieceNumber) {
        if (pieceNumber < MIN_PIECE_NUMBER || pieceNumber > MAX_PIECE_NUMBER) {
            throw new RuntimeException("전체 게임 말 개수가 잘못되었습니다.");
        }
    }

    private void validateTeamNumber(int teamNumber) {
        if (teamNumber < MIN_TEAM_NUMBER || teamNumber > MAX_TEAM_NUMBER) {
            throw new RuntimeException("팀 번호가 잘못되었습니다.");
        }
    }

    public void score(int teamNumber, int score) {
        int currentScore = scores.get(teamNumber);
        scores.put(teamNumber, currentScore + score);
    }

    public boolean isEnd() {
        return scores.entrySet().stream()
                .anyMatch(entry -> entry.getValue() == pieceCount);
    }

    public int getWinner() {
        return scores.entrySet().stream()
                .filter(entry -> entry.getValue() == pieceCount)
                .findAny()
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException("게임이 아직 끝나지 않았습니다"));
    }
}
