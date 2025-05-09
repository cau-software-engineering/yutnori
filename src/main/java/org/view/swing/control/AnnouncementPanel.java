package org.view.swing.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class AnnouncementPanel extends JPanel {

  private final JLabel label;
  private Timer timer;

  public AnnouncementPanel() {
    setLayout(new BorderLayout());
    setBackground(Color.WHITE);

    label = new JLabel("", SwingConstants.CENTER);
    label.setFont(new Font("SansSerif", Font.BOLD, 24));
    label.setForeground(Color.BLACK);

    add(label, BorderLayout.CENTER);
  }

  public void announce(String text, int duration) {
    // 이전 타이머 정리
    if (timer != null) {
      timer.cancel();
    }

    label.setText(text);
    setVisible(true);

    if (duration > 0) {
      timer = new Timer();
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          SwingUtilities.invokeLater(() -> {
            label.setText("");
          });
        }
      }, duration);
    }
  }
}