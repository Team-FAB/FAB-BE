package com.fab.banggabgo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ResponseCode implements Code {

  RESPONSE_DELETED(HttpStatus.NO_CONTENT, "DELETED"),
  RESPONSE_CREATED(HttpStatus.CREATED, "CREATED"),
  RESPONSE_SUCCESS(HttpStatus.OK, "SUCCESS"),
  RESPONSE_FAIL(HttpStatus.BAD_REQUEST, "FAIL");
  private final HttpStatus status;
  private final String msg;
}
