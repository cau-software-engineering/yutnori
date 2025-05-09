package org.core.state.turn.state;

import org.core.state.State;
import org.core.state.turn.TurnStateContext;
import org.core.state.turn.TurnStateManager;
import org.core.state.turn.event.TurnEvent;
import org.core.state.turn.event.TurnGenerateYutEvent;
import org.core.state.turn.event.TurnKillOtherEvent;
import org.core.state.turn.event.TurnMovePieceEvent;
import org.core.state.turn.event.TurnNextActionEvent;
import org.core.state.turn.event.TurnNextTurnEvent;
import org.core.state.turn.event.TurnRegenerateYutEvent;
import org.core.state.turn.event.TurnStartActionEvent;
import org.core.state.turn.event.TurnTakeMyPiecesEvent;
import org.core.state.turn.event.TurnToIdleEvent;
import org.core.state.turn.event.TurnToInvalidYutEvent;
import org.core.state.turn.event.TurnYutGeneratedEvent;

public abstract class TurnState extends State<TurnStateContext, TurnStateManager, TurnEvent> {

  public TurnState(TurnStateContext context, TurnStateManager stateManager) {
    super(context, stateManager);
  }

  public void handleEvent(TurnYutGeneratedEvent event) {
  }

  public void handleEvent(TurnGenerateYutEvent event) {
  }

  public void handleEvent(TurnRegenerateYutEvent event) {
  }

  public void handleEvent(TurnStartActionEvent event) {
  }

  public void handleEvent(TurnKillOtherEvent event) {
  }

  public void handleEvent(TurnTakeMyPiecesEvent event) {
  }

  public void handleEvent(TurnMovePieceEvent event) {
  }

  public void handleEvent(TurnNextActionEvent event) {
  }

  public void handleEvent(TurnNextTurnEvent event) {
  }

  public void handleEvent(TurnToIdleEvent event) {
  }

  public void handleEvent(TurnToInvalidYutEvent event) {
  }
}