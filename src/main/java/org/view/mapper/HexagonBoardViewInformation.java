package org.view.mapper;

import java.util.List;
import java.util.stream.Stream;
import org.core.domain.board.BoardType;
import org.core.dto.NodeViewDto;

public enum HexagonBoardViewInformation implements ViewInformation {

    /*
              S3  -  C4  -  C3  -  C2  -  C1  -   S2
             |                                       |
            D1                                       B4
           |       I1                      H1          |
          D2                                           B3
         |                                               |
        D3                I2         H2                  B2
       |                                                   |
      D4                                                   B1
     |                                                      |
    S4        G4       G3      S6      G2         G1        S1
     |                                                      |
      E1                                                   A4
       |                   H3       I3                    |
        E2                                               A3
         |                                              |
          E3        H4                     I4          A2
           |                                          |
            E4                                       A1
             |                                      |
              S5  -  F1  -  F2  -  F3  -  F4  - S7 S0
                                                 |
                                                END
        */

    S6("S6", 0.5000f, 0.5000f),

    // 꼭짓점
    S0("S0", 0.5000f, 0.0000f),
    S1("S1", 0.9330f, 0.2500f),
    S2("S2", 0.9330f, 0.7500f),
    S3("S3", 0.5000f, 1.0000f),
    S4("S4", 0.0670f, 0.7500f),
    S5("S5", 0.0670f, 0.2500f),

    // S0→S1
    A1("A1", 0.5866f, 0.0500f),
    A2("A2", 0.6732f, 0.1000f),
    A3("A3", 0.7599f, 0.1500f),
    A4("A4", 0.8464f, 0.2000f),

    // S1→S2
    B1("B1", 0.9330f, 0.3500f),
    B2("B2", 0.9330f, 0.4500f),
    B3("B3", 0.9330f, 0.5500f),
    B4("B4", 0.9330f, 0.6500f),

    // S2→S3
    C1("C1", 0.8464f, 0.8000f),
    C2("C2", 0.7599f, 0.8500f),
    C3("C3", 0.6731f, 0.9000f),
    C4("C4", 0.5866f, 0.9500f),

    // S3→S4
    D1("D1", 0.4134f, 0.9500f),
    D2("D2", 0.3268f, 0.9000f),
    D3("D3", 0.2401f, 0.8500f),
    D4("D4", 0.1534f, 0.8000f),

    // S4→S5
    E1("E1", 0.0670f, 0.6500f),
    E2("E2", 0.0670f, 0.5500f),
    E3("E3", 0.0670f, 0.4500f),
    E4("E4", 0.0670f, 0.3500f),

    // S5→S0
    F1("F1", 0.1534f, 0.2000f),
    F2("F2", 0.2402f, 0.1500f),
    F3("F3", 0.3269f, 0.1000f),
    F4("F4", 0.4134f, 0.0500f),

    // S0→S6
    I4("I4", 0.5000f, 0.1667f),
    I3("I3", 0.5000f, 0.3333f),

    // S1→S6
    G1("G1", 0.7887f, 0.3333f),
    G2("G2", 0.6443f, 0.4167f),

    // S2→S6
    H1("H1", 0.7887f, 0.6667f),
    H2("H2", 0.6443f, 0.5833f),

    // S3→S6
    I1("I1", 0.5000f, 0.8333f),
    I2("I2", 0.5000f, 0.6667f),

    // S4→S6
    G4("G4", 0.2113f, 0.6667f),
    G3("G3", 0.3557f, 0.5833f),

    // S5→S6
    H4("H4", 0.2113f, 0.3333f),
    H3("H3", 0.3557f, 0.4167f);

    private final String nodeName;
    private final float rx, ry;
    private static final float SPREAD = 1.3f;

    HexagonBoardViewInformation(String nodeName, float rx, float ry) {
        this.nodeName = nodeName;
        this.rx = rx;
        this.ry = ry;
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

    @Override
    public String getNodeName() {
        return nodeName;
    }

    @Override
    public boolean isType(BoardType type) {
        return BoardType.HEXAGON == type;
    }
}