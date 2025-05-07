package org.view.swing;

import javax.swing.*;
import java.awt.*;

import org.core.dto.GameInitializeDto;
import org.core.state.game.GameStateMachine;
import org.core.state.game.event.GameStartEvent;
import org.core.state.turn.TurnStateMachine;

public class SwingView extends JFrame {

    private GameStateMachine gameSM;
    private TurnStateMachine turnSM;

    public SwingView() {
        init();
    }

    private void init() {
        // Init
        GameInitializeDto dto = DialogUtil.showInitDialog(this);

        gameSM = GameStateMachine.create(dto);
        turnSM = TurnStateMachine.create(gameSM);

        // UI
        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new BoardPanel(gameSM),
                new ControlPanel(gameSM, turnSM, this)
        );
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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingView::new);
    }
}
