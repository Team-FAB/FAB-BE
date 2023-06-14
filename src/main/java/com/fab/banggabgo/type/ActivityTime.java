package com.fab.banggabgo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityTime {
  MIDNIGHT("밤"), MORNING("아침"), AFTERNOON("오후"), EVENING("저녁");

  final String value;
  public static ActivityTime fromValue(String value) {
    for (ActivityTime activityTime : ActivityTime.values()) {
      if (activityTime.getValue().equals(value)) {
        return activityTime;
      }
    }
    throw new IllegalArgumentException("Invalid AT value: " + value);
  }
}
