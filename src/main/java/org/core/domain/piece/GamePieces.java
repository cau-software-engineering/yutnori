package org.core.domain.piece;

import java.util.List;
import java.util.UUID;

public class GamePieces {

    private static final int MIN_TEAM_NUMBER = 1;
    private static final int MAX_TEAM_NUMBER = 4;

    private final String id;
    private final int team;
    private final List<GamePiece> pieces;
    private String place;
    private String beforePlace;
    private boolean isStart;

    public GamePieces(int team, String place, List<GamePiece> pieces) {
        validateTeamNumber(team);
        this.place = place;
        this.beforePlace = place;
        this.id = UUID.randomUUID().toString();
        this.team = team;
        this.pieces = pieces;
        this.isStart = false;
    }

    private void validateTeamNumber(int teamNumber) {
        if(teamNumber < MIN_TEAM_NUMBER || teamNumber > MAX_TEAM_NUMBER) {
            throw new RuntimeException("팀 번호가 잘못되었습니다.");
        }
    }

    public void moveTo(String place) {
        this.isStart = true;
        this.beforePlace = this.place;
        this.place = place;
    }

    public boolean isSameTeam(int teamNumber) {
        return this.team == teamNumber;
    }

    public boolean isSame(String pieceId){
        return this.id.equals(pieceId);
    }

    public void groupWith(GamePieces gamePiece) {
        pieces.addAll(gamePiece.pieces);
    }

    public List<GamePiece> getPieces() {
        return pieces;
    }

    public int getCount() {
        return pieces.size();
    }

    public String getPlace() {
        return place;
    }

    public String getBeforePlace() {
        return beforePlace;
    }

    public int getTeam() {
        return team;
    }

    public boolean isStart() {return isStart;}

    public String getId() {
        return id;
    }
}
