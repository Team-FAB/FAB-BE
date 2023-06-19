package com.fab.banggabgo.common.exception;

import com.fab.banggabgo.common.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum ErrorCode implements Code {

  /**
   * 공통 에러코드
   */
  USER_IS_NULL(HttpStatus.BAD_REQUEST, "유저 정보를 불러오는데 실패했습니다."),

  /**
   * 프로필 에러코드
   */
  INVALID_PROFILE(HttpStatus.BAD_REQUEST, "프로필이 존재하지 않습니다."),

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

  /**
  * MyContent 에러코드
  */
  PATCH_MY_INFO_CONVERT_FAIL(HttpStatus.BAD_REQUEST, "내정보 데이터 변환오류"),
  FAIL_INFO_LOADING(HttpStatus.BAD_REQUEST, "정보를 불러오지 못했습니다."),
  AMAZON_S3_UPLOAD_ERROR(HttpStatus.BAD_REQUEST,"S3 이미지 업로드 중 문제가 발생했습니다." ),


  /**
   * Apply 에러코드
   */
  ALREADY_END_RECRUITING(HttpStatus.BAD_REQUEST, "이미 모집이 완료된 글입니다."),
  NOT_FOUND_APPLY_ID(HttpStatus.BAD_REQUEST, "요청 목록을 찾을수 없습니다."),
  ALREADY_REFUSE(HttpStatus.BAD_REQUEST, "이미 거절된 상태입니다."),
  ALREADY_APPROVE(HttpStatus.BAD_REQUEST, "승인된 상대를 거절할수 없습니다."),
  ALREADY_APPLY(HttpStatus.BAD_REQUEST, "이미 요청하였습니다."),
  INVALID_APPLY_USER_ID(HttpStatus.BAD_REQUEST, "신청자의 id가 존재하지않습니다.");

  private final HttpStatus status;
  private final String msg;

}
