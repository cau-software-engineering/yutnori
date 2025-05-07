package org.core.domain.board;

import java.util.List;
import java.util.Objects;

public class NormalNode implements Node {

    private final String name;
    private Node next;

    public NormalNode(String name) {
        this.name = name;
    }

    @Override
    public List<Node> next(Node start) {
        return List.of(next);
    }

    @Override
    public List<String> getAllNodeNames() {
        return List.of(name);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NormalNode that = (NormalNode) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }
}
