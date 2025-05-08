// PieceDrawing.java
package org.view.swing;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.core.domain.piece.GamePiece;
import org.core.domain.piece.GamePieces;
import org.core.dto.NodeViewDto;
import org.core.service.BoardService;

public class PieceDrawing {

    private final BoardService boardService;
    private final Map<String, Point> nodePos;
    private final int r;                         // 말 반지름
    private final Color[] palette;               // 팀별 색상

    public PieceDrawing(BoardService boardService,
                        List<NodeViewDto> views,
                        int radius,
                        int teamCnt) {

        this.boardService = boardService;
        this.r = radius;

        nodePos = views.stream()
                .collect(Collectors.toMap(NodeViewDto::name,
                        v -> new Point(v.x(), v.y())));
        palette = buildPalette(teamCnt);
    }

    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Map<String, List<PieceInfo>> grouped = new HashMap<>();

        // 팀별 말 위치 수집
        for (int team = 1; team <= palette.length; team++) {
            for (GamePieces gp : boardService.findAllPiecesByTeam(team)) {
                String place = gp.getPlace();
                for (GamePiece p : gp.getPieces()) {
                    grouped.computeIfAbsent(place, k -> new ArrayList<>())
                            .add(new PieceInfo(team, p.getPieceNumber()));
                }
            }
        }

        // 해당하는 위치에 drawOne 호출
        for (var e : grouped.entrySet()) {
            Point pos = nodePos.get(e.getKey());
            if (pos == null) continue;

            List<PieceInfo> list = e.getValue();
            if (list.size() == 1) {
                drawOne(g2, list.get(0), pos.x, pos.y);
            } else {
                double step = 2 * Math.PI / list.size();
                for (int i = 0; i < list.size(); i++) {
                    double a  = i * step;
                    int dx = (int) (r * 0.6 * Math.cos(a));
                    int dy = (int) (r * 0.6 * Math.sin(a));
                    drawOne(g2, list.get(i), pos.x + dx, pos.y + dy);
                }
            }
        }
    }

    // 말 렌더링
    private void drawOne(Graphics2D g2, PieceInfo info, int x, int y) {
        g2.setColor(palette[info.team - 1]);
        g2.fillOval(x - r, y - r, 2 * r, 2 * r);

        g2.setColor(Color.BLACK);
        String txt = String.valueOf(info.no);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(txt,
                x - fm.stringWidth(txt) / 2,
                y + fm.getAscent() / 2 - 2);
    }

    // 말 렌더링 정보 구조체
    private static final class PieceInfo {
        final int team, no;
        PieceInfo(int team, int no) { this.team = team; this.no = no; }
    }

    // 팀 수에 맞춰 팔레트 생성
    private static Color[] buildPalette(int n) {
        Color[] arr = new Color[n];
        float step = 1f / n;
        for (int i = 0; i < n; i++)
            arr[i] = Color.getHSBColor(i * step, 0.75f, 0.90f);
        return arr;
    }
}
