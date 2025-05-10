package org.core.domain.yut;

import java.util.stream.Stream;

public enum YutResult {

  DO(1, "도"),
  GAE(2, "개"),
  GUL(3, "걸"),
  YUT(4, "윷"),
  MO(5, "모"),
  BACK_DO(-1, "빽도"),
  ;

  private final int step;
  private final String name;

  YutResult(int step, String name) {
    this.step = step;
    this.name = name;
  }

  public static YutResult from(String targetName) {
    return Stream.of(values())
        .filter(value -> value.name.equals(targetName))
        .findAny()
        .orElseThrow(() -> new RuntimeException("해당하는 윷 결과가 없습니다."));
  }

  public static boolean isYutText(String text) {
    return Stream.of(values())
        .anyMatch(value -> value.name.equals(text));
  }

  public int getStep() {
    return step;
  }

  public String getName() {
    return name;
  }
}