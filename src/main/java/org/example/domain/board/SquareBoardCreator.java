package org.example.domain.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SquareBoardCreator {

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
    public SquareBoard initialize() {

        CornerNode s0 = new CornerNode(List.of("S0", "AO", "D5", "F5"), "S0");
        CornerNode s1 = new CornerNode(List.of("S1", "EO", "BO", "A5"), "S1");
        CornerNode s2 = new CornerNode(List.of("S2", "B5", "C0", "F0"), "S2");
        CornerNode s3 = new CornerNode(List.of("S3", "C5", "D0", "E5"), "S3");
        CornerNode s5 = new CornerNode(List.of("S5"), "S5");
        EndNode endNode = new EndNode("end");

        CentralNode s4 = new CentralNode(List.of("S4"), "S4", new ArrayList<>(), new ArrayList<>());

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
        linkCentral(s1, e1, e2, s4, e3, e4, s3);
        linkCentral(s2, f1, f2, s4, f3, f4, s5);
        linkEnd(s0, s5, endNode);

        List<Node> nodes = List.of(
                a1, a2, a3, a4,
                b1, b2, b3, b4,
                c1, c2, c3, c4,
                d1, d2, d3, d4,
                e1, e2, e3, e4,
                f1, f2, f3, f4,
                s0, s1, s2, s3,
                s4, s5
        );
        return createBoard(nodes, "S0");
    }

    private SquareBoard createBoard(List<Node> nodes, String entryNodeId) {
        Map<String, Node> map = nodes.stream()
                .collect(Collectors.toMap(Node::getName, node -> node));
        return new SquareBoard(map, entryNodeId);
    }

    private void linkEnd(CornerNode startNode, CornerNode cornerNode, EndNode end) {
        startNode.setBefore(end);
        cornerNode.setForwardNext(end);
        cornerNode.setStandNext(end);
    }

    private void linkOneSide(
            CornerNode start,
            NormalNode node1,
            NormalNode node2,
            NormalNode node3,
            NormalNode node4,
            CornerNode end
    ) {
        start.setForwardNext(node1);
        node1.setBefore(start);
        node1.setNext(node2);
        node2.setBefore(node1);
        node2.setNext(node3);
        node3.setBefore(node2);
        node3.setNext(node4);
        node4.setBefore(node3);
        node4.setNext(end);
        end.setBefore(node4);
    }

    private void linkCentral(
            CornerNode start,
            NormalNode node1,
            NormalNode node2,
            CentralNode central,
            NormalNode node3,
            NormalNode node4,
            CornerNode end
    ) {
        start.setStandNext(node1);
        node1.setBefore(start);
        node1.setNext(node2);
        node2.setBefore(start);
        node2.setNext(central);
        central.addBefore(node2);
        central.addNext(node3);
        node3.setBefore(central);
        node3.setNext(node4);
        node4.setBefore(node3);
        node4.setNext(end);
    }
}