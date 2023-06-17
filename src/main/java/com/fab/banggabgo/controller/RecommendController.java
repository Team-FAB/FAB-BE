package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class RecommendController {

  private final RecommendService recommendService;

  @GetMapping("/recommend")
  public ResponseEntity<?> getRecommendUsers(
      @AuthenticationPrincipal User user,
      @RequestParam(defaultValue = "9") Integer size
  ) {
    var result = recommendService.getRecommendUsers(user, size);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
}
