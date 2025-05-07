package org.core.domain.board;

import java.util.List;

public interface Node {

    List<Node> next(Node start);

    List<String> getAllNodeNames();

    String getName();

    default boolean isEnd() {
        return false;
    }

    default boolean isSame(Node node) {
        return this.equals(node);
    }

    default boolean isSameLine(Node node) {
        List<String> names = this.getAllNodeNames();
        List<String> targetNames = node.getAllNodeNames();
        for(String name : names){
            char lineChar = name.charAt(0);
            for(String targetName : targetNames){
                char targetLine = targetName.charAt(0);
                if(lineChar == targetLine){
                    return true;
                }
            }
        }
        return false;
    }
}
