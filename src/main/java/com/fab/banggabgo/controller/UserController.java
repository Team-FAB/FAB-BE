package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.dto.user.ProfileDto;
import com.fab.banggabgo.dto.user.RecommendResponseDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"User Controller 유저 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;
  @ApiOperation(
      value = "추천 유저목록 불러오기",
      notes = "로그인 정보를 바탕으로 해당유저의 추천유저 목록을 반환합니다.. "
  )
  @GetMapping("/recommendation")
  public ResponseEntity<ApiResponse<RecommendResponseDto>> getRecommendUsers(
      @AuthenticationPrincipal User user,
      @RequestParam(defaultValue = "9") Integer size
  ) {
    var result = userService.getRecommendUsers(user, size);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
  @ApiOperation(
      value = "유저 프로필 가져오기",
      notes = "id 에해당하는 유저 정보를 가져옵니다. "
  )
  @GetMapping("/profile/{id}")
  public ResponseEntity<ApiResponse<ProfileDto>> getUserProfile(
      @PathVariable int id
  ) {
    var result = userService.getUserProfile(id);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
}
