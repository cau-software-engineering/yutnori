package org.view.mapper;

import java.util.List;
import java.util.stream.Stream;
import org.core.domain.board.BoardType;
import org.core.dto.NodeViewDto;

public enum SquareBoardViewInformation implements ViewInformation {

     /*
    S2  -  B4  -  B3  -  B2  -  B1  -    S1
     |  F1                         E1    |
    C1                                   A4
     |       F2               E2         |
    C2                                   A3
     |                S4                 |
    C3                                   A2
     |       E3               F3         |
    C4                                   A1
     |  E4                         F4    |
    S3  -  D1  -  D2  -  D3  -  D4  - S5 S0
                                       |
                                      END
     */

    S0("S0", 1f, 1f),
    S1("S1", 1f, 0f),
    S2("S2", 0f, 0f),
    S3("S3", 0f, 1f),

    B1("B1", 4/5f, 0f),
    B2("B2", 3/5f, 0f),
    B3("B3", 2/5f, 0f),
    B4("B4", 1/5f, 0f),

    A1("A1", 1f,   4/5f),
    A2("A2", 1f,   3/5f),
    A3("A3", 1f,   2/5f),
    A4("A4", 1f,   1/5f),

    D1("D1", 1/5f, 1f),
    D2("D2", 2/5f, 1f),
    D3("D3", 3/5f, 1f),
    D4("D4", 4/5f, 1f),

    C1("C1", 0f,   1/5f),
    C2("C2", 0f,   2/5f),
    C3("C3", 0f,   3/5f),
    C4("C4", 0f,   4/5f),

    F1("F1", 1/6f, 1/6f),
    F2("F2", 2/6f, 2/6f),
    F3("F3", 4/6f, 4/6f),
    F4("F4", 5/6f, 5/6f),

    E1("E1", 5/6f, 1/6f),
    E2("E2", 4/6f, 2/6f),
    E3("E3", 2/6f, 4/6f),
    E4("E4", 1/6f, 5/6f),

    S4("S4", 0.5f, 0.5f),
    S5("S5", 1f, 1f);

    private final String nodeName;
    private final float rx, ry;  // 보드 크기(size)에 대한 상대 위치 (0~1)

    SquareBoardViewInformation(String nodeName, float rx, float ry) {
        this.nodeName = nodeName;
        this.rx = rx;
        this.ry = ry;
    }

    @Override
    public String getNodeName() {
        return nodeName;
    }

    @Override
    public boolean isType(BoardType type) {
        return type == BoardType.SQUARE;
    }

    @Override
    public int getX(int margin, int size) {
        // margin + (상대위치 * size)
        return margin + Math.round(rx * size);
    }

    @Override
    public int getY(int margin, int size) {
        return margin + Math.round(ry * size);
    }
}