package com.fab.banggabgo.common.exception;

import com.fab.banggabgo.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<Object>> customExceptionResponseEntity(CustomException e) {
    return ApiResponse.builder().code(e.getErrorCode()).toEntity();
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ApiResponse<Object>> missingServletRequestParameterException(
      MissingServletRequestParameterException e) {
    return ApiResponse.builder().code(ErrorCode.INVALID_ARTICLE).toEntity();
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponse<Object>> methodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    return ApiResponse.builder().code(ErrorCode.INVALID_ARTICLE).toEntity();
  }
}
