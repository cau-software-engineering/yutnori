package org.core.domain.board.creator;

import java.util.ArrayList;
import java.util.List;
import org.core.domain.board.Board;
import org.core.domain.board.CornerNode;
import org.core.domain.board.EndNode;
import org.core.domain.board.Node;
import org.core.domain.board.NormalNode;
import org.core.domain.board.PolygonCentralNode;

public class HexagonBoardCreator extends AbstractBoardCreator {

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

    @Override
    public Board initialize() {

        CornerNode s0 = new CornerNode(List.of(), "S0");
        CornerNode s1 = new CornerNode(List.of(), "S1");
        CornerNode s2 = new CornerNode(List.of(), "S2");
        CornerNode s3 = new CornerNode(List.of(), "S3");
        CornerNode s4 = new CornerNode(List.of(), "S4");
        CornerNode s5 = new CornerNode(List.of(), "S5");
        CornerNode s7 = new CornerNode(List.of(), "S7");
        EndNode endNode = new EndNode("end");

        PolygonCentralNode s6 = new PolygonCentralNode(List.of(), "S6");

        NormalNode a1 = new NormalNode("A1");
        NormalNode a2 = new NormalNode("A2");
        NormalNode a3 = new NormalNode("A3");
        NormalNode a4 = new NormalNode("A4");

        NormalNode b1 = new NormalNode("B1");
        NormalNode b2 = new NormalNode("B2");
        NormalNode b3 = new NormalNode("B3");
        NormalNode b4 = new NormalNode("B4");

        NormalNode c1 = new NormalNode("C1");
        NormalNode c2 = new NormalNode("C2");
        NormalNode c3 = new NormalNode("C3");
        NormalNode c4 = new NormalNode("C4");

        NormalNode d1 = new NormalNode("D1");
        NormalNode d2 = new NormalNode("D2");
        NormalNode d3 = new NormalNode("D3");
        NormalNode d4 = new NormalNode("D4");

        NormalNode e1 = new NormalNode("E1");
        NormalNode e2 = new NormalNode("E2");
        NormalNode e3 = new NormalNode("E3");
        NormalNode e4 = new NormalNode("E4");

        NormalNode f1 = new NormalNode("F1");
        NormalNode f2 = new NormalNode("F2");
        NormalNode f3 = new NormalNode("F3");
        NormalNode f4 = new NormalNode("F4");

        NormalNode g1 = new NormalNode("G1");
        NormalNode g2 = new NormalNode("G2");
        NormalNode g3 = new NormalNode("G3");
        NormalNode g4 = new NormalNode("G4");

        NormalNode h1 = new NormalNode("H1");
        NormalNode h2 = new NormalNode("H2");
        NormalNode h3 = new NormalNode("H3");
        NormalNode h4 = new NormalNode("H4");

        NormalNode i1 = new NormalNode("I1");
        NormalNode i2 = new NormalNode("I2");
        NormalNode i3 = new NormalNode("I3");
        NormalNode i4 = new NormalNode("I4");

        linkOneSide(s0, a1, a2, a3, a4, s1);
        linkOneSide(s1, b1, b2, b3, b4, s2);
        linkOneSide(s2, c1, c2, c3, c4, s3);
        linkOneSide(s2, c1, c2, c3, c4, s3);
        linkOneSide(s3, d1, d2, d3, d4, s4);
        linkOneSide(s4, e1, e2, e3, e4, s5);
        linkOneSide(s5, f1, f2, f3, f4, s7);

        s5.setStandNext(f1); //s5인 경우에 위치해도 직진 거리가 더 짧기 때문에
        s0.setStandNext(a1); //s0의 경우는 무조건 a1으로 진행
        s6.setShortestPathNode(i3);
        s6.setSecondShortestPathNode(h3);

        linkCornerToCentral(s1, g1, g2, s6);
        linkCornerToCentral(s2, h1, h2, s6);
        linkCornerToCentral(s3, i1, i2, s6);
        linkCornerToCentral(s4, g4, g3, s6);

        linkCentralToCorner(s6, h3, h4, s5);
        linkCentralToCorner(s6, i3, i4, s7);

        linkEnd(s0, s7, endNode);

        List<Node> nodes = List.of(
                a1, a2, a3, a4,
                b1, b2, b3, b4,
                c1, c2, c3, c4,
                d1, d2, d3, d4,
                e1, e2, e3, e4,
                f1, f2, f3, f4,
                g1, g2, g3, g4,
                h1, h2, h3, h4,
                i1, i2, i3, i4,
                s0, s1, s2, s3,
                s4, s5, s6, s7,
                endNode
        );
        return createBoard(nodes);
    }

    private void linkCornerToCentral(
            CornerNode start,
            NormalNode node1,
            NormalNode node2,
            PolygonCentralNode central
    ) {
        start.setStandNext(node1);
        node1.setNext(node2);
        node2.setNext(central);
    }

    private void linkCentralToCorner(
            PolygonCentralNode central,
            NormalNode node1,
            NormalNode node2,
            CornerNode corner
    ) {
        node1.setNext(node2);
        node2.setNext(corner);
    }
}
