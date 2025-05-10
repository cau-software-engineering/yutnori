package org.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import org.core.state.game.GameStateMachine;
import org.core.state.game.event.GameStartEvent;
import org.core.state.turn.TurnStateMachine;
import org.view.swing.board.BoardPanel;
import org.view.swing.control.ControlPanel;

public class SwingView extends JFrame {

  private GameStateMachine gameSM;
  private TurnStateMachine turnSM;

  private Store store;

  public SwingView() {
    init();
  }

  private void init() {
    // Init
    SetupPanel setupPanel = new SetupPanel();

    setupPanel.startSetup().thenAccept(dto -> {
      gameSM = GameStateMachine.create(dto);
      turnSM = TurnStateMachine.create(gameSM);
      store = new Store(dto.teamCount());

      // UI
      JSplitPane split = new JSplitPane(
          JSplitPane.HORIZONTAL_SPLIT,
          new BoardPanel(gameSM, turnSM, store, gameSM.context.teamCount),
          new ControlPanel(gameSM, turnSM, this)
      );
      split.setBackground(Color.WHITE);
      split.setResizeWeight(0);
      split.setDividerLocation(600);

      setTitle("윷놀이 (Swing)");
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setSize(1220, 640);
      setLocationRelativeTo(null);
      add(split, BorderLayout.CENTER);

      //Game start event
      gameSM.dispatchEvent(new GameStartEvent());

      setVisible(true);
    });

  }
}