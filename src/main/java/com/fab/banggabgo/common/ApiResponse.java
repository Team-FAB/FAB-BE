package com.fab.banggabgo.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * The {@code ApiResponse} 클래스는 코드, 상태, 메시지 및 데이터를 포함하는 제네릭 API 응답을 나타냅니다.
 *
 * @param <T> 는 반환할 데이터를 담는 매개변수입니다.
 */
@Data
@AllArgsConstructor
@Getter
public class ApiResponse<T> {
  private String code;
  private HttpStatus status;
  private String msg;
  private T data;

  /**
   * 제공된 코드와 데이터로 {@code ApiResponse} 객체를 생성합니다.
   *
   * @param code 응답 상태를 나타내는 코드입니다.
   * @param data 응답과 연관된 데이터입니다.
   */
  @Builder
  public ApiResponse(Code code,T data){
    this.code=code.getCode();
    this.status=code.getStatus();
    this.msg=code.getMsg();
    this.data=data;
  }

}
