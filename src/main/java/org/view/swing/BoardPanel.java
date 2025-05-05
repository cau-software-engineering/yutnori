package org.view.swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.core.state.game.GameStateMachine;

public class BoardPanel extends JPanel {

    private static final int NORMAL_R = 15;
    private static final int CORNER_R = 30;

    /* 게임 상태(말 위치 표시용) */
    private final GameStateMachine gameSM;

    /* 노드 좌표 캐시 */
    private final List<Node> corner = new ArrayList<>();
    private final List<Node> edge   = new ArrayList<>();
    private final List<Node> diag   = new ArrayList<>();
    private Node center;

    public BoardPanel(GameStateMachine gameSM) {
        this.gameSM = gameSM;
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int MARGIN = 100;
        int SIZE   = 400;

        /* 노드 좌표 계산 */
        corner.clear();
        corner.add(new Node(MARGIN,         MARGIN,         NodeType.CORNER));
        corner.add(new Node(MARGIN+SIZE,    MARGIN,         NodeType.CORNER));
        corner.add(new Node(MARGIN,         MARGIN+SIZE,    NodeType.CORNER));
        corner.add(new Node(MARGIN+SIZE,    MARGIN+SIZE,    NodeType.CORNER));

        edge.clear();
        int seg = 5, gap = SIZE/seg;
        for(int i = 1; i < seg; i++){
            edge.add(new Node(MARGIN+i*gap,       MARGIN,           NodeType.NORMAL));
            edge.add(new Node(MARGIN+i*gap,       MARGIN+SIZE,      NodeType.NORMAL));
            edge.add(new Node(MARGIN,             MARGIN+i*gap,     NodeType.NORMAL));
            edge.add(new Node(MARGIN+SIZE,        MARGIN+i*gap,     NodeType.NORMAL));
        }

        int cx = MARGIN + SIZE/2, cy = cx;
        center = new Node(cx, cy, NodeType.CENTER);

        diag.clear();
        for(int i = 1; i <= 5; i++){
            if(i == 3) continue;
            int dx = SIZE * i / 6;
            diag.add(new Node(MARGIN+dx,       MARGIN+dx,       NodeType.NORMAL));
            diag.add(new Node(MARGIN+SIZE-dx,  MARGIN+dx,       NodeType.NORMAL));
        }

        /* 보드·노드 그리기 */
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.BLACK);

        // 외곽 + 대각선
        g2.drawRect(MARGIN, MARGIN, SIZE, SIZE);
        g2.drawLine(MARGIN, MARGIN, MARGIN+SIZE, MARGIN+SIZE);
        g2.drawLine(MARGIN+SIZE, MARGIN, MARGIN, MARGIN+SIZE);

        // 원
        corner.forEach(n -> drawCircle(g2, n, CORNER_R, true));
        edge  .forEach(n -> drawCircle(g2, n, NORMAL_R, true));
        diag  .forEach(n -> drawCircle(g2, n, NORMAL_R, true));
        drawCircle(g2, center, CORNER_R, true);
        drawCircle(g2, center, NORMAL_R, false);

    }

    private void drawCircle(Graphics2D g2, Node n, int r, boolean fill) {
        if (fill) g2.fillOval(n.x - r, n.y - r, 2*r, 2*r);
        else      g2.drawOval(n.x - r, n.y - r, 2*r, 2*r);
    }

    /* 내부 클래스 */
    static class Node { final int x,y; final NodeType type; Node(int x,int y,NodeType t){this.x=x;this.y=y;this.type=t;} }
    enum NodeType { CORNER, NORMAL, CENTER }
}
