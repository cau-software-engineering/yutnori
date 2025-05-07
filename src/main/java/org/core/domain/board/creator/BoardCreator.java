package org.core.domain.board.creator;

import java.util.stream.Stream;
import org.core.domain.board.Board;
import org.core.domain.board.BoardType;

public enum BoardCreator {

    SQUARE_CREATOR(BoardType.SQUARE, new SquareBoardCreator()),
    PENTAGON_CREATOR(BoardType.PENTAGON, new PentagonBoardCreator()),
    HEXAGON_CREATOR(BoardType.HEXAGON, new HexagonBoardCreator()),
    ;

    private final BoardType boardType;
    private final AbstractBoardCreator boardCreator;

    BoardCreator(BoardType boardType, AbstractBoardCreator boardCreator) {
        this.boardType = boardType;
        this.boardCreator = boardCreator;
    }

    public static Board create(BoardType boardType){
        return Stream.of(values())
                .filter(value -> value.boardType == boardType)
                .findAny()
                .orElseThrow(() -> new RuntimeException("일치하는 보드 타입이 없습니다"))
                .boardCreator
                .initialize();
    }
}
