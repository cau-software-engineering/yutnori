package org.example.domain.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.example.domain.yut.YutResult;

public class SquareBoard implements Board {

    private final Map<String, Node> boards;
    private final Node entryNode;

    public Node getEntryNode() {
        return entryNode;
    }

    public SquareBoard(Map<String, Node> boards, String entryNodeId) {
        this.boards = boards;
        this.entryNode = boards.get(entryNodeId);
    }

    public List<Node> next(String startNodeName, YutResult result) {
        Node startNode = boards.get(startNodeName);
        if(result == YutResult.BACK_DO){
            return startNode.before();
        }

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
}