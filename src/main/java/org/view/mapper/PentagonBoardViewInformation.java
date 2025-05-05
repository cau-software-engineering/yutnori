package org.view.mapper;

import org.core.domain.board.BoardType;

/*
                              S2
                           |      |
                        C1     G1    B4
                      |                  |
                  C2           |            B3
               |                                |
            C3                 G2                  B2
         |                                            |
     C4                        |                         B1
   |                                                         |
 S3    -   H1    -   H2    -   S6   -   F2    -   F1    -      S1
   |                                                          |
    D1                     /      \                         A4
      |                  I1        J1                      |
       D2                                                A3
        |             /                \                |
         D3                                           A2
          |       I2                       J2        |
           D4                                       A1
            |  /                               \   |
             S4  -  E1  -  E2  -  E3  -  E4  - S5 S0
                                                |
                                               END
   */

public enum PentagonBoardViewInformation implements ViewInformation {
    // ─── 꼭짓점 & 중앙 분기 ────────────────────────────────
    S0("S0", 1f,      1f     ),
    S1("S1", 1f,      4f/9f  ),
    S2("S2", 0.5f,    0f     ),
    S3("S3", 0f,      4f/9f  ),
    S4("S4", 0f,      1f     ),
    S6("S6", 0.5f,    4f/9f  ),

    // ─── 오른쪽 엣지 A1~A4 ───────────────────────────────
    A1("A1", 1f,      8f/9f  ),
    A2("A2", 1f,      7f/9f  ),
    A3("A3", 1f,      6f/9f  ),
    A4("A4", 1f,      5f/9f  ),

    // ─── 오른쪽 분기 B1~B4 ───────────────────────────────
    B1("B1", 1f,      4f/9f  ),
    B2("B2", 1f,      3f/9f  ),
    B3("B3", 1f,      2f/9f  ),
    B4("B4", 1f,      1f/9f  ),

    // ─── 왼쪽 분기 C1~C4 ────────────────────────────────
    C1("C1", 0f,      1f/9f  ),
    C2("C2", 0f,      2f/9f  ),
    C3("C3", 0f,      3f/9f  ),
    C4("C4", 0f,      4f/9f  ),

    // ─── 왼쪽 엣지 D1~D4 ────────────────────────────────
    D1("D1", 0f,      5f/9f  ),
    D2("D2", 0f,      6f/9f  ),
    D3("D3", 0f,      7f/9f  ),
    D4("D4", 0f,      8f/9f  ),

    // ─── 바닥 가로 분기 E1~E4 ────────────────────────────
    E1("E1", 1f/6f,   1f     ),
    E2("E2", 2f/6f,   1f     ),
    E3("E3", 3f/6f,   1f     ),
    E4("E4", 4f/6f,   1f     ),

    // ─── 중간 가로 분기 F1~F2 ────────────────────────────
    F1("F1", 5f/6f,   4f/9f  ),
    F2("F2", 4f/6f,   4f/9f  ),

    // ─── 세로 분기 G1~G2 ────────────────────────────────
    G1("G1", 3f/6f,   1f/9f  ),
    G2("G2", 3f/6f,   2f/9f  ),

    // ─── 가로 분기 H1~H2 ────────────────────────────────
    H1("H1", 1f/6f,   4f/9f  ),
    H2("H2", 2f/6f,   4f/9f  ),

    // ─── 대각선 분기 I1~I2, J1~J2 ───────────────────────
    I1("I1", 2f/6f,   5f/9f  ),
    I2("I2", 2f/6f,   8f/9f  ),
    J1("J1", 4f/6f,   5f/9f  ),
    J2("J2", 4f/6f,   8f/9f  );

    private final String nodeName;
    private final float rx, ry;  // 보드 크기에 대한 상대위치 (0~1)

    PentagonBoardViewInformation(String nodeName, float rx, float ry) {
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
        return type == BoardType.PENTAGON;
    }

    @Override
    public int getX(int margin, int size) {
        return margin + Math.round(rx * size);
    }

    @Override
    public int getY(int margin, int size) {
        return margin + Math.round(ry * size);
    }
}
