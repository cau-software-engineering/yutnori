package org.example.domain.board;

import java.util.List;
import org.example.domain.yut.YutResult;

public interface Board {
  List<Node> next(String startNodeName, YutResult result);
}