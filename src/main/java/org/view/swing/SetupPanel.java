package org.view.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.concurrent.CompletableFuture;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import org.core.domain.board.BoardType;
import org.core.dto.GameInitializeDto;

public class SetupPanel extends JPanel {

  private int teamCount = 2;
  private final JLabel teamLabel = new JLabel(String.valueOf(teamCount), SwingConstants.CENTER);
  private int malCount = 2;
  private final JLabel malLabel = new JLabel(String.valueOf(malCount), SwingConstants.CENTER);
  private int boardSize = 4;

  public CompletableFuture<GameInitializeDto> startSetup() {
    JFrame frame = new JFrame("윷놀이 게임 설정");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(this);
    frame.setSize(400, 360);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    CompletableFuture<GameInitializeDto> promise = new CompletableFuture<>();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    add(Box.createVerticalStrut(20));

    // 팀 수 선택
    add(createCounterPanel("몇 명의 팀으로 진행할까요? (2-4)", teamLabel, 2, 4, true));

    // 말 수 선택
    add(createCounterPanel("몇 개의 말로 진행할까요? (2-5)", malLabel, 2, 5, false));

    // 보드 선택
    add(createBoardSelector());

    // 확정 버튼
    JButton confirmButton = new JButton("확정");

    confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    confirmButton.addActionListener(e -> {
      promise.complete(new GameInitializeDto(
          Integer.parseInt(teamLabel.getText()),
          Integer.parseInt(malLabel.getText()),
          BoardType.mapTo(boardSize)));
      frame.dispose();
    });

    add(Box.createVerticalStrut(10));
    add(confirmButton);
    add(Box.createVerticalStrut(20));

    return promise;
  }

  private JPanel createCounterPanel(String title, JLabel valueLabel, int min, int max,
      boolean isTeam) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JLabel label = new JLabel(title, SwingConstants.CENTER);
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(label);
    panel.add(Box.createVerticalStrut(10));

    JPanel counter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    JButton plus = new JButton("+");
    JButton minus = new JButton("-");

    valueLabel.setPreferredSize(new Dimension(30, 30));
    counter.add(minus);
    counter.add(valueLabel);
    counter.add(plus);

    plus.addActionListener((ActionEvent e) -> {
      int val = Integer.parseInt(valueLabel.getText());
      if (val < max) {
        val++;
        valueLabel.setText(String.valueOf(val));
        if (isTeam) {
          teamCount = val;
        } else {
          malCount = val;
        }
      }
    });

    minus.addActionListener((ActionEvent e) -> {
      int val = Integer.parseInt(valueLabel.getText());
      if (val > min) {
        val--;
        valueLabel.setText(String.valueOf(val));
        if (isTeam) {
          teamCount = val;
        } else {
          malCount = val;
        }
      }
    });

    panel.add(counter);
    return panel;
  }

  private JPanel createBoardSelector() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JLabel label = new JLabel("어떤 보드로 진행할까요?", SwingConstants.CENTER);
    label.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(label);

    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
    int[] sizes = {4, 5, 6};
    ButtonGroup group = new ButtonGroup();

    for (int size : sizes) {
      JToggleButton button = new JToggleButton(String.format("%dx%d", size, size));
      if (size == boardSize) {
        button.setSelected(true);
      }
      group.add(button);
      buttons.add(button);

      button.addActionListener(e -> boardSize = size);
    }

    panel.add(buttons);
    return panel;
  }
}