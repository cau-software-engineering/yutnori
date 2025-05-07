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

    // 중심
    S6("S6", 0.5000f, 0.5000f),

    // 꼭짓점
    S1("S1", 0.9755f, 0.3455f),
    S2("S2", 0.5000f, 0.0000f),
    S0("S0", 0.7939f, 0.9045f),
    S5("S5", 0.7939f, 0.9045f),
    S4("S4", 0.2061f, 0.9045f),
    S3("S3", 0.0245f, 0.3455f),

    // S2→S3
    C1("C1", 0.4049f, 0.0691f),
    C2("C2", 0.3098f, 0.1382f),
    C3("C3", 0.2147f, 0.2073f),
    C4("C4", 0.1196f, 0.2764f),

    // S1→S2
    B1("B1", 0.8804f, 0.2764f),
    B2("B2", 0.7853f, 0.2073f),
    B3("B3", 0.6902f, 0.1382f),
    B4("B4", 0.5951f, 0.0691f),

    // S3→S4
    D1("D1", 0.0608f, 0.4573f),
    D2("D2", 0.0971f, 0.5691f),
    D3("D3", 0.1335f, 0.6809f),
    D4("D4", 0.1698f, 0.7927f),

    // S4→S0
    E1("E1", 0.3237f, 0.9045f),
    E2("E2", 0.4412f, 0.9045f),
    E3("E3", 0.5588f, 0.9045f),
    E4("E4", 0.6763f, 0.9045f),

    // S0→S1
    A1("A1", 0.8302f, 0.7927f),
    A2("A2", 0.8665f, 0.6809f),
    A3("A3", 0.9029f, 0.5691f),
    A4("A4", 0.9392f, 0.4573f),

    // S2→S6
    G1("G1", 0.5000f, 0.1667f),
    G2("G2", 0.5000f, 0.3333f),

    // S3→S6
    H1("H1", 0.1830f, 0.3970f),
    H2("H2", 0.3415f, 0.4485f),

    // S1→S6
    F2("F2", 0.6585f, 0.4485f),
    F1("F1", 0.8170f, 0.3970f),

    // S4→S6
    I1("I1", 0.4020f, 0.6348f),
    I2("I2", 0.3041f, 0.7697f),

    // S5→S6
    J1("J1", 0.5980f, 0.6348f),
    J2("J2", 0.6959f, 0.7697f);



    private final String nodeName;
    private final float rx, ry;
    private static final float SPREAD = 1.3f;

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
        float dx = rx - 0.5f;
        float scaledRx = 0.5f + dx * SPREAD;
        return margin + Math.round(scaledRx * size);
    }

    @Override
    public int getY(int margin, int size) {
        float dy = ry - 0.5f;
        float scaledRy = 0.5f + dy * SPREAD;
        return margin + Math.round(scaledRy * size);
    }
}
