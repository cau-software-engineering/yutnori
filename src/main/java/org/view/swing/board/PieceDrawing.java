// PieceDrawing.java
package org.view.swing.board;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import org.core.domain.piece.GamePiece;
import org.core.domain.piece.GamePieces;
import org.core.dto.NodeViewDto;
import org.core.service.BoardService;
import org.view.swing.Store;

public class PieceDrawing extends JPanel {

  private final BoardService boardService;
  private final int r;                         // 말 반지름
  private final Store store;                   // 노드 위치 저장소

  public PieceDrawing(Store store,
      BoardService boardService,
      List<NodeViewDto> views,
      int teamCount,
      int radius) {
    this.store = store;
    this.boardService = boardService;
    this.r = radius;
    this.setOpaque(false);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

    Map<String, List<PieceInfo>> grouped = new HashMap<>();

    // 팀별 말 위치 수집
    for (int team = 1; team <= store.getTeamCount(); team++) {
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
      Point pos = store.getNodePos(e.getKey());
      if (pos == null) {
        continue;
      }

      List<PieceInfo> list = e.getValue();
      if (list.size() == 1) {
        drawOne(g2, list.get(0), pos.x, pos.y);
      } else {
        double step = 2 * Math.PI / list.size();
        for (int i = 0; i < list.size(); i++) {
          double a = i * step;
          int dx = (int) (r * 0.6 * Math.cos(a));
          int dy = (int) (r * 0.6 * Math.sin(a));
          drawOne(g2, list.get(i), pos.x + dx, pos.y + dy);
        }
      }
    }
  }

  // 말 렌더링
  private void drawOne(Graphics2D g2, PieceInfo info, int x, int y) {
    g2.setColor(store.getPalette(info.team));
    g2.fillOval(x - r, y - r, 2 * r, 2 * r);

    g2.setColor(Color.WHITE);
    String txt = String.valueOf(info.no);
    FontMetrics fm = g2.getFontMetrics();
    g2.drawString(txt,
        x - fm.stringWidth(txt) / 2,
        y + fm.getAscent() / 2 - 2);
  }

  // 말 렌더링 정보 구조체
  private static final class PieceInfo {

    final int team, no;

    PieceInfo(int team, int no) {
      this.team = team;
      this.no = no;
    }
  }
}