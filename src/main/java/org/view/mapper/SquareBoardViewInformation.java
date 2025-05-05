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

    // ─── 코너 4개 ───────────────────────────────────────────────
    S0("S0", 0f, 0f),
    S1("S1", 1f, 0f),
    S2("S2", 0f, 1f),
    S3("S3", 1f, 1f),

    // ─── 상단 엣지 A1~A4 ───────────────────────────────────────
    A1("A1", 1/5f, 0f),
    A2("A2", 2/5f, 0f),
    A3("A3", 3/5f, 0f),
    A4("A4", 4/5f, 0f),

    // ─── 우측 엣지 B1~B4 ───────────────────────────────────────
    B1("B1", 1f,   1/5f),
    B2("B2", 1f,   2/5f),
    B3("B3", 1f,   3/5f),
    B4("B4", 1f,   4/5f),

    // ─── 하단 엣지 C1~C4 (역순 X) ──────────────────────────────
    C1("C1", 4/5f, 1f),
    C2("C2", 3/5f, 1f),
    C3("C3", 2/5f, 1f),
    C4("C4", 1/5f, 1f),

    // ─── 좌측 엣지 D1~D4 ───────────────────────────────────────
    D1("D1", 0f,   4/5f),
    D2("D2", 0f,   3/5f),
    D3("D3", 0f,   2/5f),
    D4("D4", 0f,   1/5f),

    // ─── 대각선 상단↘ 하단 (F) ─────────────────────────────────
    F1("F1", 1/6f, 1/6f),
    F2("F2", 2/6f, 2/6f),
    F3("F3", 4/6f, 4/6f),
    F4("F4", 5/6f, 5/6f),

    // ─── 반대 대각선 (E) ──────────────────────────────────────
    E1("E1", 5/6f, 1/6f),
    E2("E2", 4/6f, 2/6f),
    E3("E3", 2/6f, 4/6f),
    E4("E4", 1/6f, 5/6f),

    // ─── 중앙 ─────────────────────────────────────────────────
    S4("S4", 0.5f, 0.5f);

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