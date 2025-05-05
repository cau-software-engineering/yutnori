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

    S0("SO", 0, 0),
    S1("S1", 0, 0),
    S2("S2", 0, 0),
    S3("S3", 0, 0),
    S4("S4", 0, 0),
    S5("S5", 0, 0),
    S6("S6", 0, 0),

    A1("A1", 0, 0),
    A2("A2", 0, 0),
    A3("A3", 0, 0),
    A4("A4", 0, 0),

    B1("B1", 0, 0),
    B2("B2", 0, 0),
    B3("B3", 0, 0),
    B4("B4", 0, 0),

    C1("C1", 0, 0),
    C2("C2", 0, 0),
    C3("C3", 0, 0),
    C4("C4", 0, 0),

    D1("D1", 0, 0),
    D2("D2", 0, 0),
    D3("D3", 0, 0),
    D4("D4", 0, 0),

    E1("E1", 0, 0),
    E2("E2", 0, 0),
    E3("E3", 0, 0),
    E4("E4", 0, 0),

    F1("F1", 0, 0),
    F2("F2", 0, 0),
    F3("F3", 0, 0),
    F4("F4", 0, 0),

    G1("G1", 0, 0),
    G2("G2", 0, 0),
    G3("G3", 0, 0),
    G4("G4", 0, 0),

    H1("H1", 0, 0),
    H2("H2", 0, 0),
    H3("H3", 0, 0),
    H4("H4", 0, 0),

    I1("I1", 0, 0),
    I2("I2", 0, 0),
    I3("I3", 0, 0),
    I4("I4", 0, 0),
    ;

    private final String nodeName;
    private final int x;
    private final int y;

    HexagonBoardViewInformation(String nodeName, int x, int y) {
        this.nodeName = nodeName;
        this.x = x;
        this.y = y;
    }

    public List<NodeViewDto> getNodeViews() {
        return Stream.of(values())
                .map(value -> new NodeViewDto(value.nodeName, value.x, value.y))
                .toList();
    }

    @Override
    public int getX(int margin, int size) {
        return x;
    }

    @Override
    public int getY(int margin, int size) {
        return y;
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