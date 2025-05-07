package org.core.domain.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.core.domain.yut.YutResult;

public class Board {

    private final Map<String, Node> boards;

    public Board(Map<String, Node> boards) {
        this.boards = boards;
    }

    public List<Node> next(String startNodeName, YutResult result) {
        Node startNode = boards.get(startNodeName);

        int count = result.getStep();
        List<Node> nextNodes = new ArrayList<>();
        nextNodes.add(startNode);

        while(count > 0) {
            List<Node> tempNodes = new ArrayList<>();
            for (Node node : nextNodes) {
                tempNodes.addAll(node.next(startNode));
            }
            nextNodes = tempNodes;
            count--;
        }
        return nextNodes.stream()
                .distinct()
                .toList();
    }

    public Node findBeforeNode(String currentNode, String beforeNode) {
        System.out.println(currentNode + " " + beforeNode);
        if(currentNode.equals("S0")) {
            return boards.get("end");
        }

        Node startNode = boards.get(beforeNode);
        List<Node> nextNodes = new ArrayList<>();
        nextNodes.add(startNode);

        while(true) {
            List<Node> tempNodes = new ArrayList<>();
            for (Node node : nextNodes) {
                List<Node> next = node.next(startNode);
                List<String> names = next.stream().map(Node::getName).toList();
                if(names.contains(currentNode)) {
                    return node;
                }
                tempNodes.addAll(next);
            }
            nextNodes = tempNodes;
        }
    }
}
