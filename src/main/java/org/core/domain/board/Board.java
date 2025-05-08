package org.core.domain.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.core.domain.yut.YutResult;

public class Board {

    private final Map<String, Node> boards;
    private final BoardType type;

    public Board(Map<String, Node> boards, BoardType type) {
        this.boards = boards;
        this.type = type;
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

    public Node findBeforeNode(String currentNode, String beforeNode, List<String> history) {
        //아직 보드에 말이 없을 경우, 무효화
        if(currentNode.equals("start")) {
            return boards.get("start");
        }

        //되돌아 갈 수 있는 길이 하나라면 -> 바로 반환
        Node cur = boards.get(currentNode);
        if(cur.before().size() ==1){
            return cur.before().get(0);
        }

        //마지막 노드라면
        if(currentNode.equals(type.getFinalNodeName())) {
            CornerNode node = (CornerNode) boards.get(currentNode);

            //연속 빽도 -> 끝을 따라 이동
            if(beforeNode.equals(type.getStartNodeName())) {
                return node.getRoundBefore();
            }
            //아니라면 -> 왔던 루트를 따라 이동
            return getNodeByPath(beforeNode, currentNode);
        }

        //history로 추적이 되면 -> history로 반환
        for (int i = history.size()-1; i >=0; i--) {
            Node foundBefore = getNodeByPath(history.get(i), currentNode);
            if(foundBefore != null) {
                return foundBefore;
            }
        }

        //추적이 되지 않는 경우 -> 코너 노드의 연속 빽도
        CornerNode node = (CornerNode) boards.get(currentNode);
        return node.getRoundBefore();
    }

    private Node getNodeByPath(String beforeNode, String currentNode) {
        Node startNode = boards.get(beforeNode);
        List<Node> nextNodes = new ArrayList<>();
        nextNodes.add(startNode);

        int count = 0;

        while(count++ < 50) {
            if(nextNodes.size() == 1 && nextNodes.get(0).isEnd()) {
                break;
            }

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

        return null;
    }

    public String startNode() {
        return type.getStartNodeName();
    }

    public String endNode() {
        return type.getFinalNodeName();
    }
}
