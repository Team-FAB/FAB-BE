package com.fab.banggabgo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Test Controller 테스트 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/test")
public class TestController {
  @ApiOperation(
      value = "권한 검증 테스트",
      notes = "로그인 토큰이 있을경우 =\"OK\" 아닐경우 유저정보 not Found 반환"
  )
  @GetMapping
  public ResponseEntity<String> getTest() {
    return ResponseEntity.ok().body("ok");
  }
}
