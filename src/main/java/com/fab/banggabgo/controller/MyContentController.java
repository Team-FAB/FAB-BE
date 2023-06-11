package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.dto.mycontent.MyInfoDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.MyContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/my")
public class MyContentController {

  private final MyContentService myContentService;
  @GetMapping("/articles")
  public ResponseEntity<?> getMyArticle(@AuthenticationPrincipal User user){// 내가 작성한글 불러오기
    var result= myContentService.getMyArticle(user);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @GetMapping("/favorites")
  public ResponseEntity<?> getMyFavoriteArticle(@AuthenticationPrincipal User user){// 내가 좋아요 표시한글 불러오기
    var result= myContentService.getMyFavoriteArticle(user);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @GetMapping
  public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal User user){ // 내 정보 페이지
    var result= myContentService.getMyInfo(user);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

}
