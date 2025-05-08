package org.view.swing.component;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class RoundButton extends RingButton {

  private boolean isShowRing = false;
  private float opacity = 1f;

  public RoundButton() {
    this("");
  }

  public RoundButton(String label) {
    super(label);
    setOpaque(false); // 배경 투명
    setContentAreaFilled(false);
    setFocusPainted(false);
    setBorderPainted(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // 투명도
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

    // 배경 원
    g2.setColor(getBackground());
    g2.fillOval(0, 0, getWidth(), getHeight());

    // 🔽 커스텀 폰트 설정
    g2.setFont(getFont()); // getFont()가 기대한 폰트인지 확인 필요

    // 텍스트 중앙 정렬
    FontMetrics fm = g2.getFontMetrics();
    int stringWidth = fm.stringWidth(getText());
    int stringHeight = fm.getAscent();

    g2.setColor(getForeground());
    g2.drawString(getText(), (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2 - 2);

    g2.dispose();
  }

  @Override
  public Dimension getPreferredSize() {
    int size = Math.max(super.getPreferredSize().width, super.getPreferredSize().height);
    return new Dimension(size, size);
  }

  public void setOpacity(float opacity) {
    if (opacity < 0f || opacity > 1f) {
      throw new IllegalArgumentException("Opacity must be between 0.0 and 1.0");
    }
    this.opacity = opacity;
    repaint();
  }

  @Override
  protected void paintBorder(Graphics g) {
    if (!isShowRing) {
      return;
    }
    super.paintBorder(g);
  }

  public void setShowRing(boolean show) {
    this.isShowRing = show;
    this.repaint();
  }
}