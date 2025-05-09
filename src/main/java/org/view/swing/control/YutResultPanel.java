package org.view.swing.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.core.domain.yut.YutResult;

public class YutResultPanel extends JPanel {

  private final JLabel label;

  public YutResultPanel() {
    setLayout(new BorderLayout());
    setOpaque(false);

    label = new JLabel("", SwingConstants.CENTER);
    label.setFont(new Font("SansSerif", Font.BOLD, 50));
    label.setForeground(Color.BLACK);

    add(label, BorderLayout.CENTER);
  }

  public void display(YutResult result) {
    label.setText(result.getName());
  }

  public void clear() {
    label.setText("");
  }
}