package org.example.domain.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.example.domain.YutResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SquareBoardTest {

    /*
    * : 출발지
    $ : 도착지
    S2  -  B4  -  B3  -  B2  -  $  -     S1
     |  F1                         E1    |
    C1                                   *
     |       F2               E2         |
    C2                                   A3
     |                S4                 |
    C3                                   A2
     |       E3               F3         |
    C4                                   A1
     |  E4                         F4    |
    S3  -  D1  -  D2  -  D3  -  D4  - S5 S0
                                      |
                                      END
     */
    @DisplayName("A4 -> B1 : 코너를 지나쳐 가는 경우에 하나의 다음 노드만 반환된다")
    @Test
    void return_one_node_when_passed_away_corner() {
        SquareBoardCreator creator = new SquareBoardCreator();
        SquareBoard board = creator.initialize();

        List<Node> nextNodes = board.next("A4", YutResult.GAE);

        assertAll(
                () -> assertThat(nextNodes).hasSize(1),
                () -> assertThat(nextNodes.get(0).getName()).isEqualTo("B1")
        );
    }

    /*
    * : 출발지
    $ : 도착지
    S2  -  B4  -  B3  -  B2  -  $  -     *
     |  F1                         $     |
    C1                                   A4
     |       F2               E2         |
    C2                                   A3
     |                S4                 |
    C3                                   A2
     |       E3               F3         |
    C4                                   A1
     |  E4                         F4    |
    S3  -  D1  -  D2  -  D3  -  D4  - S5 S0
                                      |
                                      END
     */
    @DisplayName("S1 -> B1, E1 : 코너에서 시작하는 경우 두 개의 다음 노드를 반환된다")
    @Test
    void return_two_node_when_start_from_corner() {
        SquareBoardCreator creator = new SquareBoardCreator();
        SquareBoard board = creator.initialize();

        List<String> nextNodeNames = board.next("S1", YutResult.DO)
                .stream()
                .map(Node::getName)
                .toList();

        assertAll(
                () -> assertThat(nextNodeNames).hasSize(2),
                () -> assertThat(nextNodeNames).containsExactly("B1", "E1")
        );
    }

    /*
    * : 출발지
    $ : 도착지
    S2  -  B4  -  B3  -  B2  -  B1  -    S1
     |  F1                         *     |
    C1                                   A4
     |       F2               *          |
    C2                                   A3
     |                S4                 |
    C3                                   A2
     |       $               F3          |
    C4                                   A1
     |  E4                         F4    |
    S3  -  D1  -  D2  -  D3  -  D4  - S5 S0
                                      |
                                      END
     */
    @DisplayName("E1, E2 -> E3 : 중앙에서 직진하는 경우 같은 라인의 다음 노드를 반환한다")
    @Test
    void return_straight_node_when_passed_away_central() {
        SquareBoardCreator creator = new SquareBoardCreator();
        SquareBoard board = creator.initialize();

        List<Node> nextNode2 = board.next("E1", YutResult.GUL);
        List<Node> nextNode3 = board.next("E2", YutResult.GAE);

        assertAll(
                () -> assertThat(nextNode2).hasSize(1),
                () -> assertThat(nextNode3).hasSize(1),
                () -> assertThat(nextNode2.get(0).getName()).isEqualTo("E3"),
                () -> assertThat(nextNode3.get(0).getName()).isEqualTo("E3")
        );
    }

    /*
    * : 출발지
    $ : 도착지
    S2  -  $  -  B3  -  B2  -  B1  -  *
     |  F1                         E1  |
    C1                                 A4
     |       F2              E2        |
    C2                                 A3
     |                S4               |
    C3                                 A2
     |       $                F3       |
    C4                                 A1
     |  E4                         F4  |
    S3  -  D1  -  D2  -  D3  -  D4  -  S0
     */
    @DisplayName("S1 -> E3, B4 : 코너에서 직진하는 경우 같은 라인의 다음 노드와 직진하는 루트를 반환한다")
    @Test
    void return_straight_node_when_passed_away_central_from_corner() {
        SquareBoardCreator creator = new SquareBoardCreator();
        SquareBoard board = creator.initialize();

        List<String> nextNodeNames = board.next("S1", YutResult.YUT)
                .stream()
                .map(Node::getName)
                .toList();

        assertAll(
                () -> assertThat(nextNodeNames).hasSize(2),
                () -> assertThat(nextNodeNames).containsExactly("B4", "E3")
        );
    }

    /*
    * : 출발지
    $ : 도착지
    S2  -  *  -  B3  -  B2  -  B1  -       S1
     |  F1                         E1      |
    C1                                     A4
     |       F2              E2            |
    C2                                     A3
     |                *                    |
    C3                                     A2
     |       E3              F3            |
    C4                                     A1
     |  $                           $      |
    S3  -  D1  -  D2  -  D3  -  D4  - S5   S0
                                      |
                                      END
     */
    @DisplayName("S4 -> E4, F4 : 중앙에서 시작하는 경우 2개의 루트를 반환한다")
    @Test
    void return_two_node_when_start_from_central() {
        SquareBoardCreator creator = new SquareBoardCreator();
        SquareBoard board = creator.initialize();

        List<String> nextNodeNames = board.next("S4", YutResult.GAE)
                .stream()
                .map(Node::getName)
                .toList();

        assertAll(
                () -> assertThat(nextNodeNames).hasSize(2),
                () -> assertThat(nextNodeNames).containsExactly("E4", "F4")
        );
    }

    /*
    * : 출발지
    $ : 도착지
    S2  -  *  -  B3  -  B2  -  B1  -       S1
     |  F1                         E1      |
    C1                                     A4
     |       F2              E2            |
    C2                                     A3
     |                S4                   |
    C3                                     A2
     |       E3              *            |
    C4                                     A1
     |  E4                          F4     |
    S3  -  D1  -  D2  -  D3  -  D4  - $   S0
                                      |
                                      END
     */
    @DisplayName("F3 -> S5 : 다시 시작점으로 들어가는 경우 S5노드에 도착한다")
    @Test
    void arrive_s5_node_when_arrive() {
        SquareBoardCreator creator = new SquareBoardCreator();
        SquareBoard board = creator.initialize();

        List<String> nextNodeNames = board.next("F3", YutResult.GAE)
                .stream()
                .map(Node::getName)
                .toList();

        assertAll(
                () -> assertThat(nextNodeNames).hasSize(1),
                () -> assertThat(nextNodeNames).containsExactly("S5")
        );
    }

    /*
   * : 출발지
   $ : 도착지
   S2  -  *  -  B3  -  B2  -  B1  -       S1
    |  F1                         E1      |
   C1                                     A4
    |       F2              E2            |
   C2                                     A3
    |                S4                   |
   C3                                     A2
    |       E3              *            |
   C4                                     A1
    |  E4                          F4     |
   S3  -  D1  -  D2  -  D3  -  D4  - S5   S0
                                     |
                                     $
    */
    @DisplayName("F3 -> END : 주어진 행마 안에 도착하는 경우 EndNode를 반환한다")
    @Test
    void return_end_node_when_arrive() {
        SquareBoardCreator creator = new SquareBoardCreator();
        SquareBoard board = creator.initialize();

        List<Node> nextNode = board.next("F3", YutResult.MO);

        assertAll(
                () -> assertThat(nextNode).hasSize(1),
                () -> assertThat(nextNode.get(0).isEnd()).isTrue()
        );
    }

    /*
    * : 출발지
    $ : 도착지
    S2  -  *  -  B3  -  B2  -  B1  -       S1
     |  F1                         E1      |
    C1                                     A4
     |       F2              E2            |
    C2                                     A3
     |                S4                   |
    C3                                     A2
     |       E3              *            |
    C4                                     A1
     |  E4                          F4     |
    S3  -  D1  -  D2  -  D3  -  D4  - S5   S0
                                      |
                                      $
   */
    @DisplayName("S0 -> END : 시작노드에서 백도가 나오면 도착으로 취급한다")
    @Test
    void return_end_node_when_backdo_from_start() {
        SquareBoardCreator creator = new SquareBoardCreator();
        SquareBoard board = creator.initialize();

        List<Node> nextNode = board.next("S0", YutResult.BACK_DO);

        assertAll(
                () -> assertThat(nextNode).hasSize(1),
                () -> assertThat(nextNode.get(0).isEnd()).isTrue()
        );
    }
}
