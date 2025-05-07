package org.core.domain.board;

import java.util.stream.Stream;

public enum BoardType {
    SQUARE(4, "SO","S5"),
    PENTAGON(5, "S0", "S5"),
    HEXAGON(6, "S0", "S7"),
    ;

    private int type;
    private String startNodeName;
    private String finalNodeName;

    BoardType(int type, String startNodeName, String finalNodeName) {
        this.type = type;
        this.startNodeName = startNodeName;
        this.finalNodeName = finalNodeName;
    }

    public static BoardType mapTo(int boardType) {
        return Stream.of(values())
                .filter(value -> value.type == boardType)
                .findAny()
                .orElseThrow(() -> new RuntimeException("지원되지 않는 윷놀이 보드판입니다"));
    }

    public int getType() {
        return type;
    }

    public String getFinalNodeName() {
        return finalNodeName;
    }

    public String getStartNodeName() {
        return startNodeName;
    }
}
