package org.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.core.domain.board.BoardType;
import org.core.domain.game.GameDecision;
import org.core.domain.piece.GamePiece;
import org.core.domain.piece.GamePieces;
import org.core.domain.yut.YutGenerateOptions;
import org.core.domain.yut.YutResult;
import org.core.dto.GameInitializeDto;
import org.core.dto.YutGenerationRequest;
import org.core.service.BoardService;
import org.core.state.game.GameStateMachine;
import org.core.state.game.event.GameOverEvent;
import org.core.state.game.event.GameStartEvent;
import org.core.state.game.state.GameOverState;
import org.core.state.turn.TurnStateMachine;
import org.core.state.turn.event.TurnGenerateYutEvent;
import org.core.state.turn.event.TurnMovePieceEvent;
import org.core.state.turn.state.TurnIdleState;
import org.core.state.turn.state.TurnKilledOtherState;
import org.core.state.turn.state.TurnRegeneratingState;
import org.core.state.turn.state.TurnTookPieceState;
import org.core.state.turn.state.TurnWaitForActionState;

public class ConsoleView {

  GameStateMachine gameStateMachine;
  TurnStateMachine turnStateMachine;

  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

  ConsoleView() {
    init();
  }

  public void init() {
    System.out.println("윷놀이 게임을 시작합니다");

    System.out.println("몇 명의 팀으로 진행할까요? (최소 2개, 최대 4개 팀) : ");
    int teamCount = Integer.parseInt(readInput());

    System.out.println("몇 개의 말로 진행할까요? (최소 2개, 최대 5개 말) : ");
    int pieceCount = Integer.parseInt(readInput());

    System.out.println("어떤 보드로 진행할까요? (4,5,6 중 선택) : ");
    int type = Integer.parseInt(readInput());
    BoardType boardType = BoardType.mapTo(type);

    GameInitializeDto dto = new GameInitializeDto(teamCount, pieceCount, boardType);

    gameStateMachine = GameStateMachine.create(dto);
    turnStateMachine = TurnStateMachine.create(gameStateMachine);

    gameStateMachine.dispatchEvent(new GameStartEvent());

    gameStateMachine.observe(state -> {
      if (state instanceof GameOverState) {
        GameDecision decision = readGameDecision();

        if (decision == GameDecision.RESTART) {
          init();
        } else {
          System.exit(0);
        }
      }
    });

    turnStateMachine.observe(state -> {
      if (state instanceof TurnIdleState) {
        if (gameStateMachine.isGameOver()) {
          gameStateMachine.dispatchEvent(new GameOverEvent());
          return;
        }

        generateYut();
      } else if (state instanceof TurnRegeneratingState) {
        System.out.println("윷을 또 던질 수 있어요");
        generateYut();
      } else if (state instanceof TurnWaitForActionState) {
        BoardService boardService = gameStateMachine.context.boardService;

        // 윷 결과 선택
        YutResult yutResult = chooseYutResult();

        // 움직일 말 선택
        GamePieces movingPiece = readMovingPiece();

        List<String> movablePlaces = boardService.findMovablePlaces(movingPiece.getPlace(), yutResult);
        String movingPlace = chooseMovingPlace(movablePlaces);

        turnStateMachine.dispatchEvent(new TurnMovePieceEvent(movingPiece.getId(), movingPlace, yutResult));
      } else if (state instanceof TurnKilledOtherState) {
        printCatchMessage();
      } else if (state instanceof TurnTookPieceState) {
        printGroupMessage();
      }
    });

    generateYut();
  }

  public void generateYut() {
    YutGenerationRequest req = readYutGenerationInfo(turnStateMachine.context.turn.getTurn());
    turnStateMachine.dispatchEvent(new TurnGenerateYutEvent(req));
  }

  //윷 던지기 요청 정보 읽기 (옵션 / 지정 윷의 경우, 윷 정보)
  private YutGenerationRequest readYutGenerationInfo(int turn) {
    System.out.println(turn + "팀이 윷을 던질 차례입니다.");
    System.out.println("어떻게 던지시겠습니까?");
    System.out.println("1. 지정 윷 던지기");
    System.out.println("2. 랜덤 윷 던지기");

    int yutGeneration = Integer.parseInt(readInput());

    if (yutGeneration == 1) {
      System.out.println("어떤 윷을 던지시겠습니까?, ex) 도, 개, 걸, 윷, 모");
      YutResult yutResult = YutResult.from(readInput());
      return new YutGenerationRequest(YutGenerateOptions.DESIGNATED, yutResult);
    }

    return new YutGenerationRequest(YutGenerateOptions.RANDOM, null);
  }

  //나의 말 중에 어떤 걸 이동시킬 것인지 gamePieces 반환
  public GamePieces readMovingPiece() {
    List<GamePieces> movablePieces = gameStateMachine.context.boardService.findAllPiecesByTeam(
        turnStateMachine.context.turn.getTurn());

    System.out.println("어떤 말을 이동하시겠습니까? ex) 0, 1 등 숫자 입력");
    for (int i = 0; i < movablePieces.size(); i++) {
      System.out.print(i + ": ");
      printGamePiece(movablePieces.get(i));
    }

    int chosenNumber = Integer.parseInt(readInput());
    return movablePieces.get(chosenNumber);
  }

  //여러개의 윷 결과 중에 무엇을 적용할 것인지
  public YutResult chooseYutResult() {
    List<YutResult> yutResults = turnStateMachine.context.getYutResults();

    System.out.println("어떤 윷 결과를 적용할까요? ex) 0, 1 등 숫자 입력");
    for (int i = 0; i < yutResults.size(); i++) {
      System.out.println(i + ": " + yutResults.get(i).getName());
    }

    int chosenNumber = Integer.parseInt(readInput());
    return yutResults.get(chosenNumber);
  }

  // 이동가능한 칸 중에 어디로 이동할 것인지 -> place 이름 반환
  public String chooseMovingPlace(List<String> movablePlaces) {
    System.out.println("어디로 이동할까요? ex) 0, 1 등 숫자 입력");
    for (int i = 0; i < movablePlaces.size(); i++) {
      System.out.println(i + ": " + movablePlaces.get(i));
    }

    int chosenNumber = Integer.parseInt(readInput());
    return movablePlaces.get(chosenNumber);
  }

  //재시작할 건지 종료할건지 선택
  public GameDecision readGameDecision() {
    System.out.println("재시작하시겠습니까? (Y/N)");
    String decision = readInput();

    if (decision.equals("Y")) {
      return GameDecision.RESTART;
    }
    return GameDecision.EXIT;
  }

  //이긴 사람 보여주기
  public void printWinner(int winnerNumber) {
    System.out.println(winnerNumber + "팀이 승리하셨습니다!");
  }

  //잡았다는 메시지
  public void printCatchMessage() {
    System.out.println("상대 말을 잡았습니다");
  }

  //업었다는 메시지
  public void printGroupMessage() {
    System.out.println("나의 말을 업었습니다");
  }

  private void printGamePiece(GamePieces gamePiece) {
    List<GamePiece> pieces = gamePiece.getPieces();
    int team = gamePiece.getTeam();

    System.out.print(team + "팀 - ");
    for (GamePiece piece : pieces) {
      System.out.print(piece.getPieceNumber() + " ");
    }
    System.out.println("번 말");
  }

  private String readInput() {
    try {
      return reader.readLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
