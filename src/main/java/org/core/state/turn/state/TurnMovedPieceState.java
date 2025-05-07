package org.core.state.turn.state;

import java.util.List;
import org.core.domain.piece.GamePieces;
import org.core.service.BoardService;
import org.core.state.turn.TurnStateContext;
import org.core.state.turn.TurnStateManager;
import org.core.state.turn.event.TurnKillOtherEvent;
import org.core.state.turn.event.TurnMovePieceEvent;
import org.core.state.turn.event.TurnNextActionEvent;
import org.core.state.turn.event.TurnNextTurnEvent;
import org.core.state.turn.event.TurnTakeMyPiecesEvent;

public class TurnMovedPieceState extends TurnState {

  private final TurnMovePieceEvent fromEvent;

  public TurnMovedPieceState(
      TurnStateContext context,
      TurnStateManager stateManager,
      TurnMovePieceEvent fromEvent
  ) {
    super(context, stateManager);

    this.fromEvent = fromEvent;
  }

  public void onEnter() {
    List<GamePieces> catchablePieces = findCatchablePieces();
    List<GamePieces> groupablePieces = findGroupablePieces();

    if (!catchablePieces.isEmpty()) {
      this.handleEvent(new TurnKillOtherEvent(catchablePieces.get(0).getId()));
      return;
    }

    if (!groupablePieces.isEmpty()) {
      this.handleEvent(new TurnTakeMyPiecesEvent(groupablePieces.get(0).getId()));
      return;
    }

    // 남은 윷이 있을 경우 + 게임이 안 끝났을 경우
    if (context.hasYutResult() && !context.getScoreBoard().isEnd()) {
      this.handleEvent(new TurnNextActionEvent());
    } else {
      this.handleEvent(new TurnNextTurnEvent());
    }
  }

  @Override
  public void handleEvent(TurnNextTurnEvent event) {
    stateManager.setCurrentState(new TurnBeforeNextTurnState(context, stateManager));
  }

  @Override
  public void handleEvent(TurnNextActionEvent event) {
    stateManager.setCurrentState(new TurnWaitForActionState(context, stateManager));
  }

  @Override
  public void handleEvent(TurnTakeMyPiecesEvent event) {
    BoardService boardService = context.getBoardService();

    // @TODO double check
    boardService.groupPieces(fromEvent.pieceId, event.pieceId);
    stateManager.setCurrentState(new TurnTookPieceState(context, stateManager));
  }

  @Override
  public void handleEvent(TurnKillOtherEvent event) {
    BoardService boardService = context.getBoardService();

    // @TODO double check
    boardService.catchPieces(event.pieceId);
    stateManager.setCurrentState(new TurnKilledOtherState(context, stateManager));
  }

  private List<GamePieces> findCatchablePieces() {
    BoardService boardService = context.getBoardService();
    List<GamePieces> catchablePieces = boardService.findCatchablePieces(fromEvent.placeId,
        context.turn.getTurn());

    return catchablePieces;
  }


  private List<GamePieces> findGroupablePieces() {
    BoardService boardService = context.getBoardService();
    List<GamePieces> groupablePieces = boardService.findGroupablePieces(fromEvent.placeId,
            context.turn.getTurn()).stream().filter(piece -> !piece.getId().equals(fromEvent.pieceId))
        .toList();

    return groupablePieces;
  }

}
