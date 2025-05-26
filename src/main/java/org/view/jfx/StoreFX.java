package org.view.jfx;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.core.dto.NodeViewDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class StoreFX {

    private final int teamCount;
    private Map<String, Point2D> nodePos;      // ← 항상 null 아님
    private final Color[] palette;

    public StoreFX(int teamCount) {
        this.teamCount = teamCount;
        this.palette   = buildPalette(teamCount);         // ← 여기서 nodePos 초기화
    }

    // 팀 수에 맞춰 팔레트 생성
    private static Color[] buildPalette(int n) {
        Color[] arr = new Color[n];
        double step = 1.0 / n;
        for (int i = 0; i < n; i++) {
            arr[i] = Color.hsb(i * step * 360, 0.75, 0.90);
        }
        return arr;
    }
    public void setNodePos(List<NodeViewDto> nodes) {
        this.nodePos = nodes == null
                ? Collections.emptyMap()
                : nodes.stream().collect(Collectors.toUnmodifiableMap(
                NodeViewDto::name,
                n -> new Point2D(n.x(), n.y())));
    }

    public Point2D getNodePos(String id) {
        if (nodePos == null)
            throw new IllegalStateException("Store#setNodePos 가 호출되지 않았습니다.");
        return nodePos.get(id);
    }

    public Color getPalette(int team) { return palette[team - 1]; }

    public int getTeamCount() {
        return teamCount;
    }
}


