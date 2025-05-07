package org.core.service;

import java.util.List;
import org.core.domain.board.Board;
import org.core.domain.board.BoardType;
import org.core.domain.board.Node;
import org.core.domain.board.creator.BoardCreator;
import org.core.domain.piece.GamePieceInitializer;
import org.core.domain.piece.GamePieces;
import org.core.domain.piece.GamePiecesManager;
import org.core.domain.yut.YutResult;

public class BoardService {

    private final Board board;
    private final GamePiecesManager gamePiecesManager;

    public BoardService(int teamCount, int pieceCount, BoardType boardType) {
        this.gamePiecesManager = new GamePiecesManager(new GamePieceInitializer(), teamCount, pieceCount);
        this.board = BoardCreator.create(boardType);
    }

    public List<GamePieces> findAllPiecesByTeam(int team) {
        return gamePiecesManager.findAllPiecesByTeam(team);
    }

    public List<String> findMovablePlaces(String from, YutResult yutResult) {
        if (yutResult == YutResult.BACK_DO) {
            GamePieces piece = gamePiecesManager.findByPlace(from);
            Node beforeNode = board.findBeforeNode(from, piece.getBeforePlace());
            return List.of(beforeNode.getName());
        }

        return board.next(from, yutResult).stream()
                .map(Node::getName)
                .toList();
    }

    public List<GamePieces> findCatchablePieces(String place, int team) {
        List<GamePieces> catchablePieces = gamePiecesManager.findCatchablePieces(place, team);

        // 시작 노드라면 -> 마지막 노드를 잡을 수 있어야 함
        if (place.equals(board.startNode())) {
            catchablePieces.addAll(gamePiecesManager.findCatchablePieces(board.endNode(), team));
        }

        // 마지막 노드라면 -> 시작 노드를 잡을 수 있어야 함
        if(place.equals(board.endNode())) {
            catchablePieces.addAll(gamePiecesManager.findCatchablePieces(board.startNode(), team));
        }

        return catchablePieces;
    }

    public List<GamePieces> findGroupablePieces(String place, int team) {
        List<GamePieces> groupablePieces = gamePiecesManager.findGroupablePieces(place, team);

        // 시작 노드라면 -> 마지막 노드를 잡을 수 있어야 함
        if (place.equals(board.startNode())) {
            groupablePieces.addAll(gamePiecesManager.findGroupablePieces(board.endNode(), team));
        }

        // 마지막 노드라면 -> 시작 노드를 잡을 수 있어야 함
        if(place.equals(board.endNode())) {
            groupablePieces.addAll(gamePiecesManager.findGroupablePieces(board.startNode(), team));
        }

        return groupablePieces;
    }

    public GamePieces findPieces(String piecesId) {
        return gamePiecesManager.findById(piecesId);
    }

    public void catchPieces(String piecesId) {
        gamePiecesManager.catchPiece(piecesId);
    }

    public GamePieces groupPieces(String piecesId1, String piecesId2) {
        return gamePiecesManager.groupPieces(piecesId1, piecesId2, board.endNode());
    }

    public void moveTo(String pieceId, String place) {
        gamePiecesManager.moveTo(pieceId, place);
    }
}
