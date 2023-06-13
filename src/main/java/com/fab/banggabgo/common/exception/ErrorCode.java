package com.fab.banggabgo.common.exception;

import com.fab.banggabgo.common.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum ErrorCode implements Code {

  /**
   * 게시글 에러코드
   */

  INVALID_ARTICLE(HttpStatus.BAD_REQUEST, "잘못된 글 양식입니다."),
  REGION_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 지역 입니다."),
  GENDER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 성별 입니다."),
  PERIOD_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 기간 입니다."),
  ARTICLE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글 입니다."),
  ARTICLE_DELETED(HttpStatus.BAD_REQUEST, "삭제된 게시글 입니다."),
  USER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "해당 게시글의 작성자가 아닙니다."),

  MAX_ARTICLE(HttpStatus.BAD_REQUEST, "게시글은 5개까지 작성할 수 있습니다."),

  /**
   * 로그인 에러코드
   */

  EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일 입니다."),
  NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임 입니다."),
  EMAIL_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일 입니다."),
  VALUES_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이메일 혹은 닉네임 검증에 실패하였습니다."),

  PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

  ACCESS_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "만료된 로그인 토큰입니다.( 로그인 만료 )"),
  PATCH_MY_INFO_CONVERT_FAIL(HttpStatus.BAD_REQUEST, "내정보 데이터 변환오류");


  private final HttpStatus status;
  private final String msg;

}
