package org.core.domain.board.creator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.core.domain.board.Board;
import org.core.domain.board.BoardType;
import org.core.domain.board.CornerNode;
import org.core.domain.board.EndNode;
import org.core.domain.board.Node;
import org.core.domain.board.NormalNode;
import org.core.domain.board.CentralNode;

public abstract class AbstractBoardCreator {

    protected final void linkEnd(CornerNode startNode, CornerNode cornerNode, EndNode end) {
        startNode.setRoundBefore(cornerNode);
        cornerNode.setForwardNext(end);
        cornerNode.setStandNext(end);
    }

    protected final void linkOneSide(
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
        end.setRoundBefore(node4);
    }

    protected final Board createBoard(List<Node> nodes, BoardType boardType) {
        Map<String, Node> map = nodes.stream()
                .collect(Collectors.toMap(Node::getName, node -> node));
        return new Board(map, boardType);
    }

    protected void linkCornerToCentral(
            CornerNode start,
            NormalNode node1,
            NormalNode node2,
            CentralNode central
    ) {
        start.setStandNext(node1);
        node1.setBefore(start);
        node1.setNext(node2);
        node2.setBefore(node1);
        node2.setNext(central);
        central.addBefore(node2);
    }

    protected void linkCentralToCorner(
            CentralNode central,
            NormalNode node1,
            NormalNode node2,
            CornerNode corner
    ) {
        node1.setBefore(central);
        node1.setNext(node2);
        node2.setBefore(node1);
        node2.setNext(corner);
        corner.setCentralBefore(node2);
    }

    public abstract Board initialize();
}
