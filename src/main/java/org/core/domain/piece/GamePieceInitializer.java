package org.core.domain.piece;

import java.util.ArrayList;
import java.util.List;

public class GamePieceInitializer {

    public List<GamePieces> initialize(int teamCount, int pieceCount) {
        List<GamePieces> gamePieces = new ArrayList<>();
        for (int i = 1; i <= teamCount; i++) {
            for (int j = 1; j <= pieceCount; j++) {
                List<GamePiece> pieces = new ArrayList<>();
                pieces.add(new GamePiece(j));
                gamePieces.add(new GamePieces(i, null, pieces));
            }
        }
        return gamePieces;
    }
}
