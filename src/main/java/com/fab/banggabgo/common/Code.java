package com.fab.banggabgo.common;

import org.springframework.http.HttpStatus;

/**
 * {@code Code}
 * 인터페이스는 API에서 예외 코드 또는 응답 코드를 구현하기 위한 인터페이스를 나타냅니다.
 * 이 인터페이스는 코드, 상태 및 코드와 관련된 메시지를 검색하기 위한 메서드를 제공하는 인터페이스입니다.
 */
public interface Code {
  String code =null;
  HttpStatus status =null;
  String msg =null;


  default String getCode(){
    return this.toString();
  }
  HttpStatus getStatus();
  String getMsg();
}
