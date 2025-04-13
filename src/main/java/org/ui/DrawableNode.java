package org.ui;

import org.example.domain.board.Node;

public class DrawableNode {
  private final Position position;
  private final Node node;

  public DrawableNode(Node node, Position position) {
    this.node = node;
    this.position = position;
  }

  public Position getPosition() {
    return position;
  }

  public Node getNode() {
    return node;
  }
}