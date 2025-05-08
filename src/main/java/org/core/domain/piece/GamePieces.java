package org.core.domain.piece;

import java.util.ArrayList;
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
    private List<String> history = new ArrayList<>();

    public GamePieces(int team, String place, List<GamePiece> pieces) {
        validateTeamNumber(team);
        this.place = place;
        this.beforePlace = place;
        this.id = UUID.randomUUID().toString();
        this.team = team;
        this.pieces = pieces;
    }

    private void validateTeamNumber(int teamNumber) {
        if(teamNumber < MIN_TEAM_NUMBER || teamNumber > MAX_TEAM_NUMBER) {
            throw new RuntimeException("팀 번호가 잘못되었습니다.");
        }
    }

    public void moveTo(String place) {
        if(!this.place.equals("start")) {
            history.add(place);
        }
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

    public List<String> getHistory() {
        return history;
    }

    public int getTeam() {
        return team;
    }

    public String getId() {
        return id;
    }
}
