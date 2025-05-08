package org.core.domain.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.core.domain.board.creator.HexagonBoardCreator;
import org.core.domain.board.creator.PentagonBoardCreator;
import org.core.domain.board.creator.SquareBoardCreator;
import org.core.domain.yut.YutResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BoardTest {

    @Nested
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
            Board board = creator.initialize();

            List<Node> nextNodes = board.next("A4", YutResult.GAE);

            assertAll(
                    () -> assertThat(nextNodes).hasSize(1),
                    () -> assertThat(nextNodes.get(0).getName()).isEqualTo("B1")
            );
        }

        /*
        * : 출발지
        $ : 도착지
        S2  -  B4  -  B3  -  B2  -  B1  -     *
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
        @DisplayName("S1 -> E1 : 코너에서 시작하는 경우 꺾는 루트가 빠르면 무조건 꺾는다")
        @Test
        void return_center_path_when_center_path_is_shorter() {
            SquareBoardCreator creator = new SquareBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("S1", YutResult.DO)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactly("E1")
            );
        }

        /*
        * : 출발지
        $ : 도착지
        S2  -  B4  -  B3  -  B2  -  B1  -     *
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
        @DisplayName("S3 -> D1 : 코너에서 시작하는 경우 직진 루트가 빠르면 직진한다")
        @Test
        void return_straight_path_when_straight_path_is_shorter() {
            SquareBoardCreator creator = new SquareBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("S3", YutResult.DO)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactly("D1")
            );
        }

        /*
        * : 출발지
        $ : 도착지
        *  -  B4  -  B3  -  B2  -  B1  -     S1
         |  F1                         $     |
        C1                                   A4
         |       F2               E2         |
        C2                                   A3
         |                S4                 |
        C3                                   A2
         |       E3               F3         |
        C4                                   A1
         |  E4                         $    |
        S3  -  D1  -  D2  -  D3  -  D4  - S5 S0
                                          |
                                          END
         */
        @DisplayName("S2 -> F4 : 코너에서 시작하는 경우, 같은 라인의 말을 반환한다")
        @Test
        void return_same_line_path_when_start_from_corner() {
            SquareBoardCreator creator = new SquareBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("S2", YutResult.MO)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactly("F4")
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
            Board board = creator.initialize();

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
        S2  -  B4  -  B3  -  B2  -  B1  -  *
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
        @DisplayName("S1 -> E3 : 코너에서 직진하는 경우 같은 라인의 다음 노드와 직진하는 루트를 반환한다")
        @Test
        void return_straight_node_when_passed_away_central_from_corner() {
            SquareBoardCreator creator = new SquareBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("S1", YutResult.YUT)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactly( "E3")
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
         |  E4                           $      |
        S3  -  D1  -  D2  -  D3  -  D4  - S5   S0
                                          |
                                          END
         */
        @DisplayName("S4 -> E4, F4 : 중앙에서 시작하는 경우 가장 빠른 루트를 선택한다")
        @Test
        void return_shortest_node_when_start_from_central() {
            SquareBoardCreator creator = new SquareBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("S4", YutResult.GAE)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactly("F4")
            );
        }

        /*
        * : 출발지
        $ : 도착지
        S2  -  *  -  B3  -  B2  -  B1  -       S1
         |  F1                         E1      |
        C1                                     A4
         |       F2               *            |
        C2                                     A3
         |               S4                    |
        C3                                     A2
         |       E3              F3            |
        C4                                     A1
         |  $                           F4      |
        S3  -  D1  -  D2  -  D3  -  D4  - S5   S0
                                          |
                                          END
         */
        @DisplayName("E2 -> E4 : 중앙에서 시작하는 경우 가장 두 번째로 루트를 선택한다")
        @Test
        void return_second_shortest_node_when_start_from_central() {
            SquareBoardCreator creator = new SquareBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("E2", YutResult.GUL)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactly("E4")
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
            Board board = creator.initialize();

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
            Board board = creator.initialize();

            List<Node> nextNode = board.next("F3", YutResult.MO);

            assertAll(
                    () -> assertThat(nextNode).hasSize(1),
                    () -> assertThat(nextNode.get(0).isEnd()).isTrue()
            );
        }
    }

    @Nested
    class HexagonBoardTest {

        /*
        * : 출발지
        $ : 도착지

             S3  -  C4  -  C3  -  C2  -  C1  -   S2
            |                                       |
           D1                                       B4
          |       I1                      H1          |
         D2                                           B3
        |                                               |
       D3                I2         H2                  B2
      |                                                   |
     D4                                                   $
    |                                                      |
   S4        G4       G3      S6      G2         G1        S1
    |                                                      |
     E1                                                   *
      |                   H3       I3                    |
       E2                                               A3
        |                                              |
         E3        H4                     I4          A2
          |                                          |
           E4                                       A1
            |                                      |
             S5  -  F1  -  F2  -  F3  -  F4  - S7 S0
                                                |
                                               END
       */
        @DisplayName("A4 -> B1 : 코너를 지나쳐 가는 경우에 하나의 다음 노드만 반환된다")
        @Test
        void return_one_node_when_passed_away_corner() {
            HexagonBoardCreator creator = new HexagonBoardCreator();
            Board board = creator.initialize();

            List<Node> nextNodes = board.next("A4", YutResult.GAE);

            assertAll(
                    () -> assertThat(nextNodes).hasSize(1),
                    () -> assertThat(nextNodes.get(0).getName()).isEqualTo("B1")
            );
        }

        /*
         * : 출발지
         $ : 도착지

              S3  -  C4  -  C3  -  C2  -  C1  -   S2
             |                                       |
            D1                                       B4
           |       I1                      H1          |
          D2                                           B3
         |                                               |
        D3                I2         H2                  B2
       |                                                   |
      D4                                                   B1
     |                                                      |
    *         $       G3      S6      G2        G1          S1
     |                                                      |
      E1                                                   A4
       |                   H3       I3                    |
        E2                                               A3
         |                                              |
          E3        H4                     I4          A2
           |                                          |
            E4                                       A1
             |                                      |
              S5  -  F1  -  F2  -  F3  -  F4  - S7 S0
                                                 |
                                                END
        */
        @DisplayName("S4 -> G4 : 코너에서 시작하는 경우, 중앙으로 꺾는게 빠르면 꺾는다")
        @Test
        void return_center_path_when_center_is_shorter() {
            HexagonBoardCreator creator = new HexagonBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("S4", YutResult.DO)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactly("G4")
            );
        }

        /*
        * : 출발지
        $ : 도착지

             S3  -  C4  -  C3  -  C2  -  C1  -   S2
            |                                       |
           D1                                       B4
          |       I1                      H1          |
         D2                                           B3
        |                                               |
       D3                I2         H2                  B2
      |                                                   |
     D4                                                   B1
    |                                                      |
   *         $       G3      S6      G2        G1          S1
    |                                                      |
     E1                                                   A4
      |                   H3       I3                    |
       E2                                               A3
        |                                              |
         E3        H4                     I4          A2
          |                                          |
           E4                                       A1
            |                                      |
             S5  -  F1  -  F2  -  F3  -  F4  - S7 S0
                                                |
                                               END
       */
        @DisplayName("S4 -> G4 : 코너에서 시작하는 경우, 직진이 빠르면 직진한다")
        @Test
        void return_straight_path_when_straight_is_shorter() {
            HexagonBoardCreator creator = new HexagonBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("S5", YutResult.DO)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactly("F1")
            );
        }

        /*
         * : 출발지
         $ : 도착지

              S3  -  C4  -  C3  -  C2  -  C1  -   S2
             |                                       |
            D1                                       B4
           |       I1                      H1          |
          D2                                           B3
         |                                               |
        D3                *         *                  B2
       |                                                   |
      D4                                                   B1
     |                                                      |
    S4        G4       *      S6      *        G1          S1
     |                                                      |
      E1                                                   A4
       |                   H3       I3                    |
        E2                                               A3
         |                                              |
          E3        H4                     I4          A2
           |                                          |
            E4                                       A1
             |                                      |
              S5  -  F1  -  F2  -  F3  -  F4  - S7 S0
                                                 |
                                                END
        */
        @DisplayName("H2, I2, G2, G3 -> H3 : 다른 노드에서 중앙노드를 거쳐가는 경우, 두번째 최단 거리로 진행한다")
        @Test
        void return_second_shortest_path_node_when_passed_away_central() {
            HexagonBoardCreator creator = new HexagonBoardCreator();
            Board board = creator.initialize();

            List<Node> nextNode1 = board.next("H2", YutResult.GAE);
            List<Node> nextNode2 = board.next("I2", YutResult.GAE);
            List<Node> nextNode3 = board.next("G2", YutResult.GAE);
            List<Node> nextNode4 = board.next("G3", YutResult.GAE);

            assertAll(
                    () -> assertThat(nextNode1).hasSize(1),
                    () -> assertThat(nextNode2).hasSize(1),
                    () -> assertThat(nextNode3).hasSize(1),
                    () -> assertThat(nextNode4).hasSize(1),
                    () -> assertThat(nextNode1.get(0).getName()).isEqualTo("H3"),
                    () -> assertThat(nextNode2.get(0).getName()).isEqualTo("H3"),
                    () -> assertThat(nextNode3.get(0).getName()).isEqualTo("H3"),
                    () -> assertThat(nextNode4.get(0).getName()).isEqualTo("H3")
            );
        }

        /*
               * : 출발지
               $ : 도착지

                    S3  -  C4  -  C3  -  C2  -  C1  -   S2
                   |                                       |
                  D1                                       B4
                 |       I1                      H1          |
                D2                                           B3
               |                                               |
              D3                I2         H2                  B2
             |                                                   |
            D4                                                   B1
           |                                                      |
          S4        G4       G3      *      G2         G1          S1
           |                                                      |
            E1                                                   A4
             |                   H3       I3                    |
              E2                                               A3
               |                                              |
                E3        H4                     $           A2
                 |                                          |
                  E4                                       A1
                   |                                      |
                    S5  -  F1  -  F2  -  F3  -  F4  - S7 S0
                                                       |
                                                      END
              */
        @DisplayName("S6 -> I4 중앙에서 시작하는 경우 가장 짧은 루트를 반환한다")
        @Test
        void return_two_node_when_start_from_central() {
            HexagonBoardCreator creator = new HexagonBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("S6", YutResult.GAE)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactly("I4")
            );
        }

        /*
       * : 출발지
       $ : 도착지

            S3  -  C4  -  C3  -  C2  -  C1  -   S2
           |                                       |
          D1                                       B4
         |       I1                      H1          |
        D2                                           B3
       |                                               |
      D3                I2         H2                  B2
     |                                                   |
    D4                                                   B1
   |                                                      |
  S4        G4       G3      S6      G2         G1        S1
   |                                                      |
    E1                                                   A4
     |                   H3       *                     |
      E2                                               A3
       |                                              |
        E3        H4                     I4          A2
         |                                          |
          E4                                       A1
           |                                      |
            S5  -  F1  -  F2  -  F3  -  F4  - $  S0
                                               |
                                              END
      */
        @DisplayName("I3 -> S7 : 다시 시작점으로 들어가는 경우 S7노드에 도착한다")
        @Test
        void arrive_s7_node_when_arrive() {
            HexagonBoardCreator creator = new HexagonBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("I3", YutResult.GAE)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactly("S7")
            );
        }

        /*
        * : 출발지
        $ : 도착지

             S3  -  C4  -  C3  -  C2  -  C1  -   S2
            |                                       |
           D1                                       B4
          |       I1                      H1          |
         D2                                           B3
        |                                               |
       D3                I2         H2                  B2
      |                                                   |
     D4                                                   B1
    |                                                      |
   S4        G4       G3      S6      G2         G1        S1
    |                                                      |
     E1                                                   A4
      |                   H3       *                     |
       E2                                               A3
        |                                              |
         E3        H4                     I4          A2
          |                                          |
           E4                                       A1
            |                                      |
             S5  -  F1  -  F2  -  F3  -  F4  - S7  S0
                                                |
                                               $
       */
        @DisplayName("I3 -> END : 주어진 행마 안에 도착하는 경우 EndNode를 반환한다")
        @Test
        void return_end_node_when_arrive() {
            HexagonBoardCreator creator = new HexagonBoardCreator();
            Board board = creator.initialize();

            List<Node> nextNode = board.next("I3", YutResult.MO);

            assertAll(
                    () -> assertThat(nextNode).hasSize(1),
                    () -> assertThat(nextNode.get(0).isEnd()).isTrue()
            );
        }
    }

    @Nested
    class PentagonBoardTest {

        /*'
        * : 출발지
        $ : 도착지
                                  S2
                               |      |
                            C1     G1    B4
                          |                  |
                      C2           |            B3
                   |                                |
                C3                 G2                  B2
             |                                            |
         C4                        |                         $
       |                                                         |
     S3    -   H1    -   H2    -   S6   -   F2    -   F1    -      S1
       |                                                          |
        D1                     /      \                         *
          |                  I1        J1                      |
           D2                                                A3
            |             /                \                |
             D3                                           A2
              |       I2                       J2        |
               D4                                       A1
                |  /                               \   |
                 S4  -  E1  -  E2  -  E3  -  E4  - S5 S0
                                                |
                                               END
       */

        @DisplayName("A4 -> B1 : 코너를 지나쳐 가는 경우에 하나의 다음 노드만 반환된다")
        @Test
        void return_one_node_when_passed_away_corner() {
            PentagonBoardCreator creator = new PentagonBoardCreator();
            Board board = creator.initialize();

            List<Node> nextNodes = board.next("A4", YutResult.GAE);

            assertAll(
                    () -> assertThat(nextNodes).hasSize(1),
                    () -> assertThat(nextNodes.get(0).getName()).isEqualTo("B1")
            );
        }

        /*'
       * : 출발지
       $ : 도착지
                                 S2
                              |      |
                           C1     G1    B4
                         |                  |
                     C2           |            B3
                  |                                |
               C3                 G2                  B2
            |                                            |
        C4                        |                         B1
      |                                                         |
    S3    -   H1    -   H2    -   S6   -   F2    -   F1    -      S1
      |                                                          |
       D1                     /      \                         A4
         |                  I1        J1                      |
          D2                                                A3
           |             /                \                |
            D3                                           A2
             |       I2                       J2        |
              D4                                       A1
               |  /                               \   |
                S4  -  E1  -  E2  -  E3  -  E4  - S5 S0
                                               |
                                              END
      */
        @DisplayName("S3 -> H1 코너에서 시작하는 경우, 중앙으로 꺾는 것이 더 빠르면 무조건 중앙으로 꺾는다")
        @Test
        void return_center_path_when_get_into_center_is_shorter() {
            PentagonBoardCreator creator = new PentagonBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("S3", YutResult.DO)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactlyInAnyOrder("H1")
            );
        }

        /*'
      * : 출발지
      $ : 도착지
                                S2
                             |      |
                          C1     G1    B4
                        |                  |
                    C2           |            B3
                 |                                |
              C3                 G2                  B2
           |                                            |
       C4                        |                         B1
     |                                                         |
   S3    -   H1    -   H2    -   S6   -   F2    -   F1    -      S1
     |                                                          |
      D1                     /      \                         A4
        |                  I1        J1                      |
         D2                                                A3
          |             /                \                |
           D3                                           A2
            |       I2                       J2        |
             D4                                       A1
              |  /                               \   |
               S4  -  E1  -  E2  -  E3  -  E4  - S5 S0
                                              |
                                             END
     */
        @DisplayName("S4 -> E2 코너에서 시작하는 경우, 직진으로 진행하는 것이 더 빠르면 직진으로 간다")
        @Test
        void return_straight_path_when_get_into_center_is_longer() {
            PentagonBoardCreator creator = new PentagonBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("S4", YutResult.GAE)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactlyInAnyOrder("E2")
            );
        }

        /*'
       * : 출발지
       $ : 도착지
                                 S2
                              |      |
                           C1     G1    B4
                         |                  |
                     C2           |            B3
                  |                                |
               C3                 G2                  B2
            |                                            |
        C4                        |                         $
      |                                                         |
    S3    -   H1    -   H2    -   S6   -   F2    -   F1    -      S1
      |                                                          |
       D1                     /      \                         *
         |                  I1        J1                      |
          D2                                                A3
           |             /                \                |
            D3                                           A2
             |       I2                       J2        |
              D4                                       A1
               |  /                               \   |
                S4  -  E1  -  E2  -  E3  -  E4  - S5 S0
                                               |
                                              END
      */
        @DisplayName("F2, G2, H2 -> I1 : 다른 노드에서 중앙노드를 거쳐가는 경우, 두번째 최단 거리로 진행한다")
        @Test
        void return_second_shortest_path_node_when_passed_away_central() {
            PentagonBoardCreator creator = new PentagonBoardCreator();
            Board board = creator.initialize();

            List<Node> nextNode1 = board.next("F2", YutResult.GAE);
            List<Node> nextNode2 = board.next("G2", YutResult.GAE);
            List<Node> nextNode3 = board.next("H2", YutResult.GAE);

            assertAll(
                    () -> assertThat(nextNode1).hasSize(1),
                    () -> assertThat(nextNode2).hasSize(1),
                    () -> assertThat(nextNode3).hasSize(1),
                    () -> assertThat(nextNode1.get(0).getName()).isEqualTo("I1"),
                    () -> assertThat(nextNode2.get(0).getName()).isEqualTo("I1"),
                    () -> assertThat(nextNode3.get(0).getName()).isEqualTo("I1")
            );
        }

        /*'
       * : 출발지
       $ : 도착지
                                 S2
                              |      |
                           C1     G1    B4
                         |                  |
                     C2           |            B3
                  |                                |
               C3                 G2                  B2
            |                                            |
        C4                        |                         B1
      |                                                         |
    S3    -   H1    -   H2    -   *   -   F2    -   F1    -      S1
      |                                                          |
       D1                     /      \                         A4
         |                  I1        J1                      |
          D2                                                A3
           |             /                \                |
            D3                                           A2
             |       I2                       $        |
              D4                                       A1
               |  /                               \   |
                S4  -  E1  -  E2  -  E3  -  E4  - S5 S0
                                               |
                                              END
      */
        @DisplayName("S6 -> J2 중앙에서 시작하는 경우 가장 짧은 루트를 반환한다")
        @Test
        void return_two_node_when_start_from_central() {
            PentagonBoardCreator creator = new PentagonBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("S6", YutResult.GAE)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactly("J2")
            );
        }

        /*'
       * : 출발지
       $ : 도착지
                                 S2
                              |      |
                           C1     G1    B4
                         |                  |
                     C2           |            B3
                  |                                |
               C3                 G2                  B2
            |                                            |
        C4                        |                         $
      |                                                         |
    S3    -   H1    -   H2    -   S6   -   F2    -   F1    -      S1
      |                                                          |
       D1                     /      \                         *
         |                  I1        J1                      |
          D2                                                A3
           |             /                \                |
            D3                                           A2
             |       I2                       J2        |
              D4                                       A1
               |  /                               \   |
                S4  -  E1  -  E2  -  E3  -  E4  - S5 S0
                                               |
                                              END
      */
        @DisplayName("J1 -> S5 : 다시 시작점으로 들어가는 경우 S7노드에 도착한다")
        @Test
        void arrive_s7_node_when_arrive() {
            PentagonBoardCreator creator = new PentagonBoardCreator();
            Board board = creator.initialize();

            List<String> nextNodeNames = board.next("J1", YutResult.GAE)
                    .stream()
                    .map(Node::getName)
                    .toList();

            assertAll(
                    () -> assertThat(nextNodeNames).hasSize(1),
                    () -> assertThat(nextNodeNames).containsExactly("S5")
            );
        }

        /*'
        * : 출발지
        $ : 도착지
                                  S2
                               |      |
                            C1     G1    B4
                          |                  |
                      C2           |            B3
                   |                                |
                C3                 G2                  B2
             |                                            |
         C4                        |                         B1
       |                                                         |
     S3    -   H1    -   H2    -   S6   -   F2    -   F1    -      S1
       |                                                          |
        D1                     /      \                         A4
          |                  I1        J1                      |
           D2                                                A3
            |             /                \                |
             D3                                           A2
              |       I2                       J2        |
               D4                                       A1
                |  /                               \   |
                 S4  -  E1  -  E2  -  E3  -  E4  - S5 S0
                                                |
                                               END
       */
        @DisplayName("J1 -> END : 주어진 행마 안에 도착하는 경우 EndNode를 반환한다")
        @Test
        void return_end_node_when_arrive() {
            PentagonBoardCreator creator = new PentagonBoardCreator();
            Board board = creator.initialize();

            List<Node> nextNode = board.next("J1", YutResult.MO);

            assertAll(
                    () -> assertThat(nextNode).hasSize(1),
                    () -> assertThat(nextNode.get(0).isEnd()).isTrue()
            );
        }
    }
}
