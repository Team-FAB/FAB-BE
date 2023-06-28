package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.dto.user.ProfileDto;
import com.fab.banggabgo.dto.user.RecommendResponseDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @GetMapping("/recommendation")
  public ResponseEntity<ApiResponse<RecommendResponseDto>> getRecommendUsers(
      @AuthenticationPrincipal User user,
      @RequestParam(defaultValue = "9") Integer size
  ) {
    var result = userService.getRecommendUsers(user, size);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @GetMapping("/profile/{id}")
  public ResponseEntity<ApiResponse<ProfileDto>> getUserProfile(
      @PathVariable int id
  ) {
    var result = userService.getUserProfile(id);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
}
