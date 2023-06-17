package com.fab.banggabgo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApproveStatus {
  WAIT("대기"), APPROVAL("승인"), REFUSE("거절");

  final String value;
}
