package org.view.swing;

import java.awt.Color;
import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.core.dto.NodeViewDto;

public class Store {

  private final int teamCount;

  private Map<String, Point> nodePos;
  private Color[] pallete;

  public Store(int teamCount) {
    this.teamCount = teamCount;
    this.pallete = buildPalette(teamCount);
  }

  // 팀 수에 맞춰 팔레트 생성
  private static Color[] buildPalette(int n) {
    Color[] arr = new Color[n];
    float step = 1f / n;
    for (int i = 0; i < n; i++) {
      arr[i] = Color.getHSBColor(i * step, 0.75f, 0.90f);
    }
    return arr;
  }

  public void setNodePos(List<NodeViewDto> views) {
    nodePos = views.stream()
        .collect(Collectors.toMap(NodeViewDto::name,
            v -> new Point(v.x(), v.y())));
  }

  public Point getNodePos(String nodeName) {
    return nodePos.get(nodeName);
  }

  public Color getPalette(int team) {
    return pallete[team - 1];
  }

  public int getTeamCount() {
    return teamCount;
  }
}