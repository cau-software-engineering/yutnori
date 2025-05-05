package org.view.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.core.domain.board.BoardType;
import org.core.dto.NodeViewDto;

public class BoardViewMapper {

    private final List<ViewInformation> pool;

    public BoardViewMapper() {
        this.pool = initializePool();
    }

    private List<ViewInformation> initializePool() {
        List<ViewInformation> pool = new ArrayList<>();
        pool.addAll(Arrays.asList(SquareBoardViewInformation.values()));
        pool.addAll(Arrays.asList(HexagonBoardViewInformation.values()));
        pool.addAll(Arrays.asList(PentagonBoardViewInformation.values()));
        return pool;
    }

    public List<NodeViewDto> mapTo(BoardType boardType, int margin, int size) {
        return pool.stream()
                .filter(v -> v.isType(boardType))
                .map(v -> new NodeViewDto(
                        v.getNodeName(),
                        v.getX(margin, size),
                        v.getY(margin, size)
                ))
                .collect(Collectors.toList());
    }
}