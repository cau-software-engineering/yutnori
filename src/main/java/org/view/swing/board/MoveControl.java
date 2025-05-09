package org.view.swing.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.core.domain.piece.GamePieces;
import org.core.domain.yut.YutResult;
import org.core.service.BoardService;
import org.core.state.game.GameStateMachine;
import org.core.state.turn.TurnStateMachine;
import org.core.state.turn.event.TurnMovePieceEvent;
import org.core.state.turn.state.TurnWaitForActionState;
import org.view.swing.Store;
import org.view.swing.component.RingButton;
import org.view.swing.component.RoundButton;

public class MoveControl extends JPanel {

  private final GameStateMachine gameSM;
  private final TurnStateMachine turnSM;
  private final Store store;

  private List<RingButton> placedPieceButtons = new ArrayList<>();
  private List<RoundButton> unstartedPieceButtons = new ArrayList<>();
  private List<RoundButton> movablePlaceButtons = new ArrayList<>();

  MoveControl(
      Store store,
      GameStateMachine gameSM,
      TurnStateMachine turnSM) {
    this.store = store;
    this.gameSM = gameSM;
    this.turnSM = turnSM;

    this.setOpaque(false);
    this.setLayout(null);

    turnSM.observe(state -> {
      if (state instanceof TurnWaitForActionState) {
        showActions();
      } else {
        clear();
      }
    });
  }

  private void showActions() {
    clear();

    int turn = turnSM.context.turn.getTurn();
    List<GamePieces> list = gameSM.context.boardService.findAllPiecesByTeam(turn);

    for (int index = 0; index < list.size(); index++) {
      GamePieces gamePieces = list.get(index);
      JButton button;

      if (!gamePieces.getPlace().equals("start")) {
        button = createPlacedPieceButton(gamePieces);
        placedPieceButtons.add((RingButton) button);
      } else {
        button = createUnstartedPieceSelectionButton(index, list.size(), gamePieces);
        unstartedPieceButtons.add((RoundButton) button);
      }

      this.add(button);
    }

    this.revalidate();
    this.repaint();
  }

  private void clear() {
    if (movablePlaceButtons != null) {
      movablePlaceButtons.forEach(this::remove);
    }
    if (placedPieceButtons != null) {
      unselectPlacedPieceButtons();
    }
    placedPieceButtons.clear();
    this.removeAll();
  }

  private void unselectPlacedPieceButtons() {
    placedPieceButtons.forEach(b -> b.setRingColor());
  }

  private RingButton createPlacedPieceButton(GamePieces gamePieces) {
    Point point = store.getNodePos(gamePieces.getPlace());

    RingButton button = new RingButton();
    button.setBounds(point.x - 20, point.y - 20, 40, 40);

    button.addActionListener(e -> {
      showMovablePlaces(gamePieces);
      button.setRingColor(Color.GREEN);
      placedPieceButtons.forEach(b -> {
        if (b != button) {
          b.setRingColor();
        }
      });
    });

    return button;
  }

  private JButton createUnstartedPieceSelectionButton(int i, int listSize,
      GamePieces gamePieces) {
    RoundButton button = new RoundButton(i + 1 + "");
    button.setFont(new Font("Arial", Font.BOLD, 16));
    button.setBackground(store.getPalette(gamePieces.getTeam()));
    button.setForeground(Color.WHITE);
    button.setPreferredSize(new Dimension(50, 50));
    button.setBounds(540 - ((listSize - i - 1) * 60), 540, 50, 50);
    button.setShowRing(true);

    button.addActionListener(e -> {
      button.setRingColor(Color.GREEN);
      unstartedPieceButtons.forEach(b -> {
        if (b != button) {
          b.setRingColor();
        }
      });

      showMovablePlaces(gamePieces);
      unselectPlacedPieceButtons();
    });
    return button;
  }

  private void showMovablePlaces(GamePieces gamePieces) {
    if (movablePlaceButtons != null) {
      movablePlaceButtons.forEach(this::remove);
    }

    List<YutResult> yutResults = turnSM.context.getYutResults();
    int turn = turnSM.context.turn.getTurn();

    BoardService bs = gameSM.context.boardService;
    List<GamePieces> allPlacedPieces = gameSM.context.boardService.findAllPiecesByTeam(turn);

    for (YutResult yutResult : yutResults) {
      List<String> movablePlaces = bs.findMovablePlaces(gamePieces.getPlace(), yutResult);

      for (String place : movablePlaces) {
        if (bs.getNode(place).isEnd()) {
          RoundButton endButton = new RoundButton();

          endButton.setBounds(20, 20, 40, 40);
          endButton.setBackground(Color.DARK_GRAY);
          endButton.setForeground(Color.WHITE);
          endButton.setText("끝");

          endButton.addActionListener(e1 -> turnSM.dispatchEvent(
              new TurnMovePieceEvent(gamePieces.getId(), place, yutResult)));

          movablePlaceButtons.add(endButton);
          this.add(endButton);

          continue;
        }

        RoundButton moveButton = new RoundButton();
        Point movePoint = store.getNodePos(place);
        boolean isOverlapping = allPlacedPieces.stream().map(pieces -> pieces.getPlace())
            .anyMatch(i -> i == place);
        int margin = isOverlapping ? 40 : 0;

        moveButton.setBounds(movePoint.x - 10 + margin, movePoint.y - 10, 20, 20);
        moveButton.setBackground(store.getPalette(gamePieces.getTeam()));
        moveButton.setOpacity(0.8f);
        moveButton.addActionListener(e1 -> turnSM.dispatchEvent(
            new TurnMovePieceEvent(gamePieces.getId(), place, yutResult)));

        movablePlaceButtons.add(moveButton);
        this.add(moveButton);
      }
    }

    this.revalidate();
    this.repaint();
  }


}