package org.view.swing.control;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Timer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.core.domain.game.GameDecision;
import org.core.state.game.GameStateMachine;
import org.core.state.game.event.GameOverEvent;
import org.core.state.game.state.GameOverState;
import org.core.state.turn.TurnStateMachine;
import org.core.state.turn.state.TurnIdleState;
import org.core.state.turn.state.TurnRegeneratingState;
import org.core.state.turn.state.TurnWaitForActionState;
import org.view.swing.DialogUtil;
import org.view.swing.SwingView;

public class ControlPanel extends JPanel {

  private final GameStateMachine gameSM;
  private final TurnStateMachine turnSM;
  private final JFrame owner;

  private final AnnouncementPanel announcementPanel;
  private final YutResultPanel yutResultPanel;
  private final YutThrowPanel yutThrowPanel;

  public ControlPanel(GameStateMachine gameSM,
      TurnStateMachine turnSM,
      JFrame owner) {
    super();
    this.gameSM = gameSM;
    this.turnSM = turnSM;
    this.owner = owner;

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setBackground(Color.WHITE);

    announcementPanel = new AnnouncementPanel();
    yutResultPanel = new YutResultPanel();
    yutThrowPanel = new YutThrowPanel(turnSM);

    this.add(announcementPanel);
    announcementPanel.setPreferredSize(new Dimension(400, 80));  // 너비 400, 높이 80
    announcementPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // full width

    announcementPanel.announce(
        "게임을 시작합니다. " + gameSM.context.teamCount + "팀이 참여합니다.", 1500);

    new Timer().schedule(new java.util.TimerTask() {
      @Override
      public void run() {
        SwingUtilities.invokeLater(() -> {
          announcementPanel.announce("1팀이 먼저 시작합니다. 윷을 던져주세요.", 0);
        });
        yutThrowPanel.setEnabled(true);
      }
    }, 1800);

    this.add(Box.createVerticalStrut(40));
    this.add(yutResultPanel);
    this.add(Box.createVerticalStrut(10));
    this.add(yutThrowPanel);
    this.add(Box.createVerticalStrut(20));

    registerObservers();
  }

  // Observers
  private void registerObservers() {

    /* GameState */
    gameSM.observe(st -> {
      if (st instanceof GameOverState) {
        GameDecision d = DialogUtil.askGameDecision(owner);
        if (d == GameDecision.RESTART) {
          owner.dispose();
          new SwingView();
        } else {
          System.exit(0);
        }
      }
    });

    /* TurnState */
    turnSM.observe(st -> SwingUtilities.invokeLater(() -> handleTurnState(st)));
  }

  // handle turn state
  private void handleTurnState(Object st) {

    if (st instanceof TurnIdleState) {
      if (gameSM.isGameOver()) {
        gameSM.dispatchEvent(new GameOverEvent());
        return;
      }

      announcementPanel.announce(
          turnSM.context.turn.getTurn() + "팀 차례입니다. 윷을 던져주세요.", 0);
      yutThrowPanel.setEnabled(true);

    } else if (st instanceof TurnRegeneratingState) {
      announcementPanel.announce(
          turnSM.context.turn.getTurn() + "팀이 윷을 한 번 더 던질 수 있어요!", 0);
      yutThrowPanel.setEnabled(true);
    } else if (st instanceof TurnWaitForActionState) {
      announcementPanel.announce("이동할 " +
          turnSM.context.turn.getTurn() + "팀 말을 선택해주세요", 0);
    }
  }
}