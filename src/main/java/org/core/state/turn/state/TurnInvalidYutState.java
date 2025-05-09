package org.core.state.turn.state;

import org.core.state.turn.TurnStateContext;
import org.core.state.turn.TurnStateManager;
import org.core.state.turn.event.TurnNextTurnEvent;

public class TurnInvalidYutState extends TurnState {

  public TurnInvalidYutState(TurnStateContext context, TurnStateManager stateManager) {
    super(context, stateManager);
  }

  @Override
  public void handleEvent(TurnNextTurnEvent event) {
    context.clearYutResults();
    stateManager.setCurrentState(new TurnBeforeNextTurnState(context, stateManager));
  }
}