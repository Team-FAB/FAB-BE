package com.fab.banggabgo.type;

public enum Price {
  MINPRICE(1000000),
  MAXPRICE(20000000);

  private final Integer value;

  Price(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }
}
