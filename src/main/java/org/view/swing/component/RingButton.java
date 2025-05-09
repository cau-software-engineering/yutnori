package org.view.swing.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

public class RingButton extends JButton {

  private Color ringColor = Color.DARK_GRAY;

  public RingButton() {
    this("");
  }

  public RingButton(String label) {
    super(label);
    setOpaque(false);
    setContentAreaFilled(false);
    setBorderPainted(false);
    setFocusPainted(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    // 배경 없음 (투명 버튼이므로)
    super.paintComponent(g);
  }

  @Override
  protected void paintBorder(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(ringColor);
    g2.setStroke(new BasicStroke(4));

    int size = Math.min(getWidth(), getHeight());
    int padding = 2;

    g2.drawOval(padding, padding, size - 2 * padding, size - 2 * padding);

    g2.dispose();
  }

  public void setRingColor(Color color) {
    this.ringColor = color;
    repaint();
  }

  public void setRingColor() {
    this.ringColor = Color.DARK_GRAY;
    repaint();
  }
}