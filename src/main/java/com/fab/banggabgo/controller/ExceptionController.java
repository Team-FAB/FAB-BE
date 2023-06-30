package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Api(tags = {"Exception Controller 예외컨트롤 API"})
@RestController
@RequestMapping("/api/exception")
public class ExceptionController {
  @ApiOperation(
      value = "유저 정보가 없는경우",
      notes = "USER_IS_NULL 을 반환합니다. "
  )
  @GetMapping
  public ResponseEntity<ApiResponse<Object>> getException() {
    throw new CustomException(ErrorCode.USER_IS_NULL);
  }
}
