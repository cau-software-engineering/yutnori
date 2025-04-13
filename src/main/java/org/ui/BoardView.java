package org.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;
import org.example.domain.board.Node;

public class BoardView extends JPanel {
  private final DrawableBoard drawableBoard;

  public BoardView(DrawableBoard drawableBoard) {
    this.drawableBoard = drawableBoard;

    setPreferredSize(new Dimension(600, 600));
    setBackground(new Color(255, 255, 230));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawNodes(g, drawableBoard.getEntry(), new HashSet<>());
  }

  private void drawNodes(Graphics g, DrawableNode current, Set<DrawableNode> visited) {
    if (visited.contains(current)) return;
    visited.add(current);

    DrawableNode curr = current;
    Position pos = curr.getPosition();

    g.setColor(Color.BLUE);
    g.fillOval(pos.x - 10, pos.y - 10, 20, 20);
    g.setColor(Color.BLACK);

    for (Node next : curr.next(curr)) {
      if (next instanceof DrawableNode) {
        DrawableNode nextNode = (DrawableNode) next;
        Position nextPos = nextNode.getPosition();

        g.setColor(Color.GRAY);
        g.drawLine(pos.x, pos.y, nextPos.x, nextPos.y);

        drawNodes(g, next, visited); // 재귀 호출
      }
    }
  }
}