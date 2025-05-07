package org.core.domain.board.creator;

import java.util.List;
import org.core.domain.board.Board;
import org.core.domain.board.BoardType;
import org.core.domain.board.CornerNode;
import org.core.domain.board.EndNode;
import org.core.domain.board.Node;
import org.core.domain.board.NormalNode;
import org.core.domain.board.CentralNode;

public class SquareBoardCreator extends AbstractBoardCreator {

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
    public Board initialize(BoardType type) {

        CornerNode s0 = new CornerNode(List.of("S0", "AO", "D5", "F5"), "S0");
        CornerNode s1 = new CornerNode(List.of("S1", "EO", "BO", "A5"), "S1");
        CornerNode s2 = new CornerNode(List.of("S2", "B5", "C0", "F0"), "S2");
        CornerNode s3 = new CornerNode(List.of("S3", "C5", "D0", "E5"), "S3");
        CornerNode s5 = new CornerNode(List.of("S5"), "S5");
        EndNode endNode = new EndNode("end");

        CentralNode s4 = new CentralNode(List.of("S4"), "S4");

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

        linkOneSide(s0, a1, a2, a3, a4, s1);
        linkOneSide(s1, b1, b2, b3, b4, s2);
        linkOneSide(s2, c1, c2, c3, c4, s3);
        linkOneSide(s2, c1, c2, c3, c4, s3);
        linkOneSide(s3, d1, d2, d3, d4, s5);
        linkCornerToCentral(s1, e1, e2, s4);
        linkCornerToCentral(s1, e1, e2, s4);
        linkCornerToCentral(s2, f1, f2, s4);
        linkCentralToCorner(s4, e3, e4, s3);
        linkCentralToCorner(s4, f3, f4, s5);

        s4.setShortestPathNode(f3);
        s4.setSecondShortestPathNode(e3);

        linkEnd(s0, s5, endNode);

        s3.setStandNext(d1); //S3의 경우는 코너에 위치해도 무조건 직진
        s0.setStandNext(a1); //s0의 경우는 무조건 a1으로 진행

        List<Node> nodes = List.of(
                a1, a2, a3, a4,
                b1, b2, b3, b4,
                c1, c2, c3, c4,
                d1, d2, d3, d4,
                e1, e2, e3, e4,
                f1, f2, f3, f4,
                s0, s1, s2, s3,
                s4, s5, endNode
        );
        return createBoard(nodes, type);
    }
}
