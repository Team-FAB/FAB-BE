package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.dto.mycontent.PatchMyInfoForm;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameForm;
import com.fab.banggabgo.dto.mycontent.PostMyInfoImageRequestDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.MyContentService;
import java.io.IOException;
import javax.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/my")
@PermitAll
public class MyContentController {

  private final MyContentService myContentService;

  @GetMapping("/articles")
  public ResponseEntity<?> getMyArticle(@AuthenticationPrincipal User user) {// 내가 작성한글 불러오기
    var result = myContentService.getMyArticle(user);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @GetMapping("/favorites")
  public ResponseEntity<?> getMyFavoriteArticle(
      @AuthenticationPrincipal User user) {// 내가 좋아요 표시한글 불러오기
    var result = myContentService.getMyFavoriteArticle(user);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @GetMapping
  public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal User user) { // 내 정보 페이지
    var result = myContentService.getMyInfo(user);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @PatchMapping("/nickname")
  public ResponseEntity<?> PatchMyNickName(@AuthenticationPrincipal User user, @RequestBody
  PatchMyNicknameForm form) { //닉네임 변경
    var result = myContentService.patchNickname(user, PatchMyNicknameForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @PatchMapping
  public ResponseEntity<?> patchMyInfo(@AuthenticationPrincipal User user,
      @RequestBody PatchMyInfoForm form) { //내정보 변경
    var result = myContentService.patchMyInfo(user, PatchMyInfoForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @PostMapping(value = "/image", consumes = "multipart/form-data")
  public ResponseEntity<?> postMyInfoImage(@AuthenticationPrincipal User user,
      @RequestPart MultipartFile image) throws IOException {
    var dto=  PostMyInfoImageRequestDto.builder().image(image).build();
    var result = myContentService.postMyInfoImage(user,dto);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

}
