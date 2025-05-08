package org.core.domain.board;

import java.util.List;

public class CentralNode implements Node {

    private final List<String> allNodeNames;
    private final String name;
    private Node shortestPathNode;
    private Node secondShortestPathNode;

    public CentralNode(
            List<String> allNodeNames,
            String name
    ) {
        this.allNodeNames = allNodeNames;
        this.name = name;
    }

    @Override
    public List<Node> next(Node start) {
        if(start.isSame(this)) {
            return List.of(shortestPathNode);
        }
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
