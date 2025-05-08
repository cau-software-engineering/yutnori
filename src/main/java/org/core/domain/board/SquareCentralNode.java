package org.core.domain.board;

import java.util.List;

public class SquareCentralNode implements Node {

    private final List<String> allNodeNames;
    private final String name;
    private Node shortestPathNode;
    private Node secondShortestPathNode;

    public SquareCentralNode(
            List<String> allNodeNames,
            String name
    ) {
        this.allNodeNames = allNodeNames;
        this.name = name;
    }

    @Override
    public List<Node> next(Node start) {
        if(start.isSame(this) || start.isSameLine(shortestPathNode)) {
            return List.of(shortestPathNode);
        }
        //그게 아니면 같은 라인인 경우만
        return List.of(secondShortestPathNode);
    }

    public void setSecondShortestPathNode(Node secondShortestPathNode) {
        this.secondShortestPathNode = secondShortestPathNode;
    }

    public void setShortestPathNode(Node shortestPathNode) {
        this.shortestPathNode = shortestPathNode;
    }

    @Override
    public List<String> getAllNodeNames() {
        return allNodeNames;
    }

    @Override
    public String getName() {
        return name;
    }
}
