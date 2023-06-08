package com.fab.banggabgo.common.exception;

import com.fab.banggabgo.common.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum ErrorCode implements Code {

  /**
   * 로그인 에러코드
   */

  EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일 입니다."),
  NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임 입니다."),
  EMAIL_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일 입니다."),
  VALUES_ALREADY_EXISTS(HttpStatus.BAD_REQUEST ,"이메일 혹은 닉네임 검증에 실패하였습니다." ),

  PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

  ACCESS_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "만료된 로그인 토큰입니다.( 로그인 만료 )");


  private final HttpStatus status;
  private final String msg;

}
