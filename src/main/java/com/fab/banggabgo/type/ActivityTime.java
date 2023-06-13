package com.fab.banggabgo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityTime {
  MIDNIGHT("밤"), MORNING("아침"), AFTERNOON("오후"), EVENING("저녁");

  final String value;
}
