package com.fab.banggabgo.common.exception;

import com.fab.banggabgo.common.ApiResponse;
import javax.security.sasl.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<?> customExceptionResponseEntity(CustomException e) {
    return ApiResponse.builder().code(e.getErrorCode()).toEntity();
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<?> authenticationExceptionResponseEntity(AuthenticationException e) {
    return ApiResponse.builder().code(ErrorCode.USER_IS_NULL).toEntity();
  }
}
