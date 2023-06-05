package com.fab.banggabgo.type;

public enum Period {
  ONETOTHREE("1개월 ~ 3개월"),
  THREETOSIX("3개월 ~ 6개월"),
  SIXTONINE("6개월 ~ 9개월"),
  NINETOTWELVE("9개월 ~ 12개월"),
  OVERTWELVE("1년 이상");

  private final String value;

  Period(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static Period fromValue(String value) {
    for (Period period : Period.values()) {
      if (period.getValue().equals(value)) {
        return period;
      }
    }
    throw new IllegalArgumentException("Invalid Period value: " + value);
  }
}
