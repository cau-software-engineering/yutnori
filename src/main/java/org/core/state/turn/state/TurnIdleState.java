package org.core.state.turn.state;

import org.core.domain.yut.YutResult;
import org.core.state.turn.TurnStateContext;
import org.core.state.turn.TurnStateManager;
import org.core.state.turn.event.TurnGenerateYutEvent;

public class TurnIdleState extends TurnState {

  public TurnIdleState(TurnStateContext context, TurnStateManager stateManager) {
    super(context, stateManager);

  }

  @Override
  public void handleEvent(TurnGenerateYutEvent event) {
    // 윷 생성
    YutResult yutResult = context.yutGenerator.generate(
            event.request.options(),
            event.request.yutResult()
    );

    // 윷 결과를 추가
    context.addYutResult(yutResult);

    stateManager.setCurrentState(new TurnGeneratedState(context, stateManager));
  }
}
