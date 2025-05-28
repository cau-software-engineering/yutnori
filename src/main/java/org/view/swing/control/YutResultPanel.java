package org.view.swing.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.core.domain.yut.YutResult;
import org.core.state.turn.TurnStateMachine;

public class YutResultPanel extends JPanel {

  private final JLabel label;
  private final TurnStateMachine turnSM;

  public YutResultPanel(TurnStateMachine turnSM) {
    this.turnSM = turnSM;

    setOpaque(false);

    setLayout(new BorderLayout());
    setBackground(Color.WHITE);

    label = new JLabel("", SwingConstants.CENTER);
    label.setFont(new Font("SansSerif", Font.BOLD, 50));
    label.setForeground(Color.BLACK);

    add(label, BorderLayout.CENTER);
  }

  public void update() {
    List<YutResult> results = turnSM.context.getYutResults();

    if (results == null) {
      label.setText("");
    } else {
      label.setText(results.stream().map(YutResult::getName).collect(Collectors.joining(", ")));
    }
  }
}