package org.core.domain.board.creator;

import java.util.List;
import org.core.domain.board.Board;
import org.core.domain.board.BoardType;
import org.core.domain.board.CornerNode;
import org.core.domain.board.EndNode;
import org.core.domain.board.Node;
import org.core.domain.board.NormalNode;
import org.core.domain.board.CentralNode;

public class PentagonBoardCreator extends AbstractBoardCreator {

    private static final BoardType BOARD_TYPE = BoardType.PENTAGON;

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
    @Override
    public Board initialize() {

        CornerNode s0 = new CornerNode(List.of(), "S0");
        CornerNode s1 = new CornerNode(List.of(), "S1");
        CornerNode s2 = new CornerNode(List.of(), "S2");
        CornerNode s3 = new CornerNode(List.of(), "S3");
        CornerNode s4 = new CornerNode(List.of(), "S4");
        CornerNode s5 = new CornerNode(List.of(), "S5");
        EndNode endNode = new EndNode("end");

        CentralNode s6 = new CentralNode(List.of(), "S6");

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

        NormalNode g1 = new NormalNode("G1");
        NormalNode g2 = new NormalNode("G2");

        NormalNode h1 = new NormalNode("H1");
        NormalNode h2 = new NormalNode("H2");

        NormalNode i1 = new NormalNode("I1");
        NormalNode i2 = new NormalNode("I2");

        NormalNode j1 = new NormalNode("J1");
        NormalNode j2 = new NormalNode("J2");

        linkOneSide(s0, a1, a2, a3, a4, s1);
        linkOneSide(s1, b1, b2, b3, b4, s2);
        linkOneSide(s2, c1, c2, c3, c4, s3);
        linkOneSide(s2, c1, c2, c3, c4, s3);
        linkOneSide(s3, d1, d2, d3, d4, s4);
        linkOneSide(s4, e1, e2, e3, e4, s5);

        s4.setStandNext(e1); //s4에 위치하면 직진이 더 거리가 짧기 때문에
        s0.setStandNext(a1); //s0의 경우는 무조건 a1으로 진행
        s6.setShortestPathNode(j1);
        s6.setSecondShortestPathNode(i1);

        linkCornerToCentral(s1, f1, f2, s6);
        linkCornerToCentral(s2, g1, g2, s6);
        linkCornerToCentral(s3, h1, h2, s6);

        linkCentralToCorner(s6, i1, i2, s4);
        linkCentralToCorner(s6, j1, j2, s5);

        linkEnd(s0, s5, endNode);

        List<Node> nodes = List.of(
                a1, a2, a3, a4,
                b1, b2, b3, b4,
                c1, c2, c3, c4,
                d1, d2, d3, d4,
                e1, e2, e3, e4,
                f1, f2, g1, g2,
                h1, h2, i1, i2,
                j1, j2,
                s0, s1, s2, s3,
                s4, s5, s6,
                endNode
        );

        return createBoard(nodes, BOARD_TYPE);
    }
}
