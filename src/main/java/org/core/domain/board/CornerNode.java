package org.core.domain.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CornerNode implements Node {

    private final List<String> allNodeNames;
    private final String name;
    private Node forwardNext;
    private Node standNext;
    private Node centralBefore;
    private Node roundBefore;

    public CornerNode(List<String> allNodeNames, String name) {
        this.name = name;
        this.allNodeNames = allNodeNames;
    }

    @Override
    public List<Node> next(Node start) {
        if(start.isSame(this)) {
            return List.of(standNext);
        }
        return List.of(forwardNext);
    }

    public List<Node> before() {
        List<Node> result = new ArrayList<>();
        if(centralBefore != null) {
            result.add(centralBefore);
        }

        if(roundBefore != null) {
            result.add(roundBefore);
        }
        return result;
    }

    @Override
    public List<String> getAllNodeNames() {
        return allNodeNames;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setForwardNext(Node forwardNext) {
        this.forwardNext = forwardNext;
    }

    public Node getCentralBefore() {
        return centralBefore;
    }

    public Node getRoundBefore() {
        return roundBefore;
    }

    public void setStandNext(Node standNext) {
        this.standNext = standNext;
    }

    public void setCentralBefore(Node centralBefore) {
        this.centralBefore = centralBefore;
    }

    public void setRoundBefore(Node roundBefore) {
        this.roundBefore = roundBefore;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CornerNode that = (CornerNode) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
