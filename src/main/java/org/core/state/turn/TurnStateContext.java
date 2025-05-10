package org.core.state.turn;

import java.util.ArrayList;
import java.util.List;
import org.core.domain.game.ScoreBoard;
import org.core.domain.game.Turn;
import org.core.domain.yut.RandomYutGenerateStrategy;
import org.core.domain.yut.YutGenerator;
import org.core.domain.yut.YutResult;
import org.core.service.BoardService;
import org.core.state.game.GameStateMachine;

public class TurnStateContext {

  public final Turn turn;
  public final YutGenerator yutGenerator;
  private final GameStateMachine gameStateMachine;
  private List<YutResult> yutResults = new ArrayList<>();

  public TurnStateContext(GameStateMachine gameStateMachine) {
    this.gameStateMachine = gameStateMachine;
    this.turn = new Turn(gameStateMachine.context.teamCount);
    this.yutGenerator = new YutGenerator(new RandomYutGenerateStrategy());
  }

  public BoardService getBoardService() {
    return gameStateMachine.context.boardService;
  }

  public ScoreBoard getScoreBoard() {
    return gameStateMachine.context.scoreBoard;
  }

  public List<YutResult> getYutResults() {
    return yutResults;
  }

  // shouldn't be used outside machine
  public void addYutResult(YutResult yutResult) {
    this.yutResults.add(yutResult);
  }

  // shouldn't be used outside machine
  public void useYutResult(YutResult yutResult) {
    this.yutResults.remove(yutResult);
  }

  public void clearYutResults() {
    this.yutResults.clear();
  }

  public YutResult lastYutResult() {
    if (yutResults.isEmpty()) {
      return null;
    }

    return yutResults.get(yutResults.size() - 1);
  }

  public boolean hasYutResult() {
    return !yutResults.isEmpty();
  }

}