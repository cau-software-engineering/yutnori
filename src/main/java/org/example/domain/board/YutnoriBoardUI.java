package org.example.domain.board;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class YutnoriBoardUI extends JFrame {

    public YutnoriBoardUI() {
        setTitle("Yutnori Board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);

        add(new BoardPanel());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new YutnoriBoardUI().setVisible(true);
        });
    }

    class BoardPanel extends JPanel {

        // 노드 정보를 담는 내부 클래스
        class Node {
            int x, y;         // 노드의 중심 좌표
            NodeType type;    // Corner, Normal, Center

            public Node(int x, int y, NodeType type) {
                this.x = x;
                this.y = y;
                this.type = type;
            }
        }

        // 노드 종류
        enum NodeType {
            CORNER, NORMAL, CENTER
        }

        // 노드 크기 조절
        private final int NORMAL_RADIUS = 15;
        private final int CORNER_RADIUS = 30;

        private List<Node> cornerNodes = new ArrayList<>();
        private List<Node> edgeNormalNodes = new ArrayList<>();
        private List<Node> diagNormalNodes = new ArrayList<>();
        private Node centerNode;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);

            // 판을 그릴 위치
            int MARGIN = 100;
            int SIZE = 400;

            // --------------------------------
            // 1) CornerNode(각 꼭짓점 노드)
            // --------------------------------
            cornerNodes.clear();
            cornerNodes.add(new Node(MARGIN, MARGIN, NodeType.CORNER));                      // 왼쪽 상단
            cornerNodes.add(new Node(MARGIN + SIZE, MARGIN, NodeType.CORNER));               // 오른쪽 상단
            cornerNodes.add(new Node(MARGIN, MARGIN + SIZE, NodeType.CORNER));               // 왼쪽 하단
            cornerNodes.add(new Node(MARGIN + SIZE, MARGIN + SIZE, NodeType.CORNER));        // 오른쪽 하단

            // --------------------------------
            // 2) 정사각형 변의 NormalNode
            //    Corner 제외한 구간을 5등분 → 내부 4개씩
            // --------------------------------
            edgeNormalNodes.clear();
            int segments = 5;               // 5등분 (코너 포함 5구간 → 내부 노드 4개)
            int gap = SIZE / segments;      // 각 구간 길이

            // 윗변 (왼쪽상단 -> 오른쪽상단)
            for(int i = 1; i < segments; i++) {
                edgeNormalNodes.add(new Node(MARGIN + i * gap, MARGIN, NodeType.NORMAL));
            }
            // 아랫변 (왼쪽하단 -> 오른쪽하단)
            for(int i = 1; i < segments; i++) {
                edgeNormalNodes.add(new Node(MARGIN + i * gap, MARGIN + SIZE, NodeType.NORMAL));
            }
            // 왼쪽변 (왼쪽상단 -> 왼쪽하단)
            for(int i = 1; i < segments; i++) {
                edgeNormalNodes.add(new Node(MARGIN, MARGIN + i * gap, NodeType.NORMAL));
            }
            // 오른쪽변 (오른쪽상단 -> 오른쪽하단)
            for(int i = 1; i < segments; i++) {
                edgeNormalNodes.add(new Node(MARGIN + SIZE, MARGIN + i * gap, NodeType.NORMAL));
            }

            // --------------------------------
            // 3) CenterNode(중앙 노드)
            // --------------------------------
            int centerX = MARGIN + SIZE / 2;
            int centerY = MARGIN + SIZE / 2;
            centerNode = new Node(centerX, centerY, NodeType.CENTER);

            // --------------------------------
            // 4) 대각선 위의 NormalNode
            //    CenterNode 기준으로 절반마다 2개씩, 즉 대각선 한 줄에 4개
            // --------------------------------
            diagNormalNodes.clear();

            // 왼상단 -> 오른하단 대각선
            // i=0 => (MARGIN, MARGIN)
            // i=3 => (centerX, centerY)
            // i=6 => (MARGIN+SIZE, MARGIN+SIZE)
            // => i=1,2,4,5 위치에 NormalNode
            for (int i = 1; i <= 5; i++) {
                if (i == 3) continue; // i=3은 CenterNode
                int dx = (SIZE * i) / 6;
                int x = MARGIN + dx;
                int y = MARGIN + dx;
                diagNormalNodes.add(new Node(x, y, NodeType.NORMAL));
            }

            // 오른상단 -> 왼하단 대각선
            // i=0 => (MARGIN+SIZE, MARGIN)
            // i=3 => (centerX, centerY)
            // i=6 => (MARGIN, MARGIN+SIZE)
            // => i=1,2,4,5 위치에 NormalNode
            for (int i = 1; i <= 5; i++) {
                if (i == 3) continue; // i=3은 CenterNode
                int dx = (SIZE * i) / 6;
                // x는 (MARGIN+SIZE)에서 왼쪽으로 dx만큼
                // y는 (MARGIN)에서 아래로 dx만큼
                int x = (MARGIN + SIZE) - dx;
                int y = MARGIN + dx;
                diagNormalNodes.add(new Node(x, y, NodeType.NORMAL));
            }

            // 보드판 그리기
            // --------------------------------
            // 선 그리기
            // --------------------------------
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2.0f));

            // 정사각형 테두리
            g2.drawLine(MARGIN, MARGIN, MARGIN + SIZE, MARGIN);               // 윗변
            g2.drawLine(MARGIN, MARGIN + SIZE, MARGIN + SIZE, MARGIN + SIZE); // 아랫변
            g2.drawLine(MARGIN, MARGIN, MARGIN, MARGIN + SIZE);               // 왼쪽변
            g2.drawLine(MARGIN + SIZE, MARGIN, MARGIN + SIZE, MARGIN + SIZE); // 오른쪽변

            // 대각선 2줄
            g2.drawLine(MARGIN, MARGIN, MARGIN + SIZE, MARGIN + SIZE);
            g2.drawLine(MARGIN + SIZE, MARGIN, MARGIN, MARGIN + SIZE);

            // --------------------------------
            // 노드 그리기
            // --------------------------------
            // CornerNode: 큰 원
            for (Node c : cornerNodes) {
                drawCornerNode(g2, c.x, c.y);
            }
            // Edge NormalNode: 작은 원
            for (Node n : edgeNormalNodes) {
                drawNormalNode(g2, n.x, n.y);
            }
            // Diagonal NormalNode: 작은 원
            for (Node n : diagNormalNodes) {
                drawNormalNode(g2, n.x, n.y);
            }
            // CenterNode: 큰 원 + 그 위에 작은 원
            drawCenterNode(g2, centerNode.x, centerNode.y);
        }

        // CornerNode: 큰 원
        private void drawCornerNode(Graphics2D g2, int centerX, int centerY) {
            int r = CORNER_RADIUS;
            g2.fillOval(centerX - r, centerY - r, 2 * r, 2 * r);
        }

        // NormalNode: 작은 원
        private void drawNormalNode(Graphics2D g2, int centerX, int centerY) {
            int r = NORMAL_RADIUS;
            g2.fillOval(centerX - r, centerY - r, 2 * r, 2 * r);
        }

        // CenterNode: 큰 원 + 작은 원(이중 원)
        private void drawCenterNode(Graphics2D g2, int centerX, int centerY) {
            // 큰 원
            int rBig = CORNER_RADIUS;
            g2.fillOval(centerX - rBig, centerY - rBig, 2 * rBig, 2 * rBig);

            // 그 위에 작은 원
            int rSmall = NORMAL_RADIUS;
            g2.setColor(Color.WHITE);
            g2.fillOval(centerX - rSmall, centerY - rSmall, 2 * rSmall, 2 * rSmall);

        }
    }
}
