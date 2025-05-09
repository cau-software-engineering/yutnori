package org.core.state.turn.state;

import java.util.List;
import org.core.domain.piece.GamePieces;
import org.core.domain.yut.YutResult;
import org.core.state.turn.TurnStateContext;
import org.core.state.turn.TurnStateManager;
import org.core.state.turn.event.TurnRegenerateYutEvent;
import org.core.state.turn.event.TurnStartActionEvent;
import org.core.state.turn.event.TurnToInvalidYutEvent;

public class TurnGeneratedState extends TurnState {

  public TurnGeneratedState(TurnStateContext context, TurnStateManager stateManager) {
    super(context, stateManager);
  }

  public void onEnter() {
    // 최근에 생성된 것이 윷, 모일 경우
    if (context.lastYutResult() == YutResult.YUT ||
        context.lastYutResult() == YutResult.MO) {
      this.handleEvent(new TurnRegenerateYutEvent());
      return;
    }

    if (context.lastYutResult() == YutResult.BACK_DO) {
      int turn = context.turn.getTurn();
      List<GamePieces> placedPieces = context.getBoardService().findAllPlacedPiecesByTeam(turn);

      if (placedPieces.isEmpty()) {
        this.handleEvent(new TurnToInvalidYutEvent());
        return;
      }
    }

    this.handleEvent(new TurnStartActionEvent());
  }

  @Override
  public void handleEvent(TurnRegenerateYutEvent event) {
    stateManager.setCurrentState(new TurnRegeneratingState(context, stateManager));
  }

  @Override
  public void handleEvent(TurnStartActionEvent event) {
    stateManager.setCurrentState(new TurnWaitForActionState(context, stateManager));
  }

  @Override
  public void handleEvent(TurnToInvalidYutEvent event) {
    stateManager.setCurrentState(new TurnInvalidYutState(context, stateManager));
  }
}