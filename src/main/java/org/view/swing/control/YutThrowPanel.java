package org.view.swing.control;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.core.domain.yut.YutGenerateOptions;
import org.core.domain.yut.YutResult;
import org.core.dto.YutGenerationRequest;
import org.core.state.turn.TurnStateMachine;
import org.core.state.turn.event.TurnGenerateYutEvent;
import org.core.state.turn.state.TurnIdleState;
import org.core.state.turn.state.TurnRegeneratingState;

public class YutThrowPanel extends JPanel {

  private final JButton fixedThrowButton;
  private final JButton randomThrowButton;

  private final TurnStateMachine turnSM;

  public YutThrowPanel(TurnStateMachine turnSM) {
    this.turnSM = turnSM;

    setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
    setOpaque(false);

    fixedThrowButton = createStyledButton("지정 윷 던지기");
    randomThrowButton = createStyledButton("랜덤 윷 던지기");

    fixedThrowButton.setEnabled(false);
    randomThrowButton.setEnabled(false);

    fixedThrowButton.addActionListener(this::handleFixedThrowButton);
    randomThrowButton.addActionListener(this::handleRandomThrowButton);

    add(fixedThrowButton);
    add(randomThrowButton);

    turnSM.observe(state -> {
      if (state instanceof TurnIdleState || state instanceof TurnRegeneratingState) {
        fixedThrowButton.setEnabled(true);
        randomThrowButton.setEnabled(true);
      }
    });
  }

  private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setFont(new Font("SansSerif", Font.BOLD, 16));
    button.setForeground(Color.WHITE);
    button.setBackground(Color.DARK_GRAY);
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(true);
    button.setOpaque(true);
    button.setPreferredSize(new Dimension(160, 50));
    button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    return button;
  }

  private void handleFixedThrowButton(ActionEvent actionEvent) {
    fixedThrowButton.setEnabled(false);
    randomThrowButton.setEnabled(false);

    String input = JOptionPane.showInputDialog(
        this, "어떤 윷을 던지시겠습니까? (빽도/도/개/걸/윷/모)");

    if (input == null || input.isEmpty() || !YutResult.isYutText(input)) {
      fixedThrowButton.setEnabled(true);
      randomThrowButton.setEnabled(true);
      return;
    }

    YutResult res = YutResult.from(input);

    YutGenerationRequest req = new YutGenerationRequest(YutGenerateOptions.DESIGNATED, res);

    turnSM.dispatchEvent(new TurnGenerateYutEvent(req));
  }

  private void handleRandomThrowButton(ActionEvent actionEvent) {
    fixedThrowButton.setEnabled(false);
    randomThrowButton.setEnabled(false);

    YutGenerationRequest req = new YutGenerationRequest(YutGenerateOptions.RANDOM, null);
    turnSM.dispatchEvent(new TurnGenerateYutEvent(req));
  }

  public void setEnabled(boolean enabled) {
    fixedThrowButton.setEnabled(enabled);
    randomThrowButton.setEnabled(enabled);
  }
}