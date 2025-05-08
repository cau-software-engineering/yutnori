package org.core.domain.board;

import java.util.List;

public class SquareCentralNode extends CentralNode {

    public SquareCentralNode(
            List<String> allNodeNames,
            String name
    ) {
        super(allNodeNames, name);
    }

    @Override
    public List<Node> next(Node start) {
        if (start.isSame(this) || start.isSameLine(getShortestPathNode())) {
            return List.of(getShortestPathNode());
        }
        return List.of(getSecondShortestPathNode());
    }
}
