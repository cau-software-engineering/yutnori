package org.core.domain.board;

import java.util.List;

public class EndNode implements Node {

    private final String name;

    public EndNode(String name) {
        this.name = name;
    }

    @Override
    public List<Node> next(Node start) {
        return List.of(this);
    }

    @Override
    public List<Node> before() {
        throw new RuntimeException("이 노드는 엔드 노드입니다.");
    }

    @Override
    public List<String> getAllNodeNames() {
        return List.of(name);
    }

    @Override
    public boolean isEnd() {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }
}
