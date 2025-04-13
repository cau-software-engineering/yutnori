package org.ui;

import java.util.ArrayList;
import java.util.List;
import org.example.domain.board.Node;
import org.example.domain.board.SquareBoard;

public class DrawableBoard {
  private List<DrawableNode> nodes;
  private DrawableNode entry;

  public DrawableNode getEntry() {
    return entry;
  }

  public DrawableBoard(
      List<DrawableNode> nodes,
      DrawableNode entry
  ) {
    this.nodes = nodes;
    this.entry = entry;
  }

  public static DrawableBoard fromSquareBoard(SquareBoard squareBoard) {
    Node cur = squareBoard.getEntryNode();
    List<DrawableNode> nodes = new ArrayList<>();

    DrawableNode entry = new DrawableNode(cur, new Position(500, 500));
    nodes.add(entry);

    // Right Side
    for (int i = 0; i < 3; i++) {
      var next = cur.next(squareBoard.getEntryNode()).get(i);
      Position position = new Position(500, 500 - (i + 1) * 50);
      DrawableNode drawableNode = new DrawableNode(next, position);
      nodes.add(new DrawableNode(next, position));
    }

    // Down Side
    return new DrawableBoard(nodes, entry);
  }
}