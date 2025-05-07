package org.view.mapper;

import org.core.domain.board.BoardType;

public interface ViewInformation {

    int getX(int margin, int size);
    int getY(int margin, int size);

    String getNodeName();

    boolean isType(BoardType type);
}