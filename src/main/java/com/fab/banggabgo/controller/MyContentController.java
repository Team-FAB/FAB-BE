package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.dto.apply.ApplyListResultDto;
import com.fab.banggabgo.dto.mycontent.FavoriteArticleDto;
import com.fab.banggabgo.dto.mycontent.MyArticleDto;
import com.fab.banggabgo.dto.mycontent.MyInfoDto;
import com.fab.banggabgo.dto.mycontent.PatchMyInfoForm;
import com.fab.banggabgo.dto.mycontent.PatchMyInfoResultDto;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameForm;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameResult;
import com.fab.banggabgo.dto.mycontent.PostMyInfoImageRequestDto;
import com.fab.banggabgo.dto.mycontent.PostMyInfoImageResultDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.ArticleService;
import com.fab.banggabgo.service.MyContentService;
import com.fab.banggabgo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;
import javax.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
@Api(tags = {"My Controller 내정보 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("api/my")
@Validated
public class MyContentController {

  private final MyContentService myContentService;

  @ApiOperation(
      value = "작성한글 불러오기",
      notes = "로그인정보(토큰)를 바탕으로 자신이 작성한 글들 을 가져옵니다."
  )
  @GetMapping("/articles")
  public ResponseEntity<ApiResponse<MyArticleDto>> getMyArticle(@AuthenticationPrincipal User user) {// 내가 작성한글 불러오기
    var result = myContentService.getMyArticle(user);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @ApiOperation(
      value = "좋아요한글 불러오기",
      notes = "로그인정보(토큰)를 바탕으로 자신이 좋아요 표시한 글들 을 가져옵니다."
  )
  @GetMapping("/favorites")
  public ResponseEntity<ApiResponse<FavoriteArticleDto>> getMyFavoriteArticle(
      @AuthenticationPrincipal User user) {// 내가 좋아요 표시한글 불러오기
    var result = myContentService.getMyFavoriteArticle(user);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @ApiOperation(
      value = "내정보 불러오기",
      notes = "로그인정보(토큰)를 바탕으로 자신의 정보를 가져옵니다.."
  )
  @GetMapping
  public ResponseEntity<ApiResponse<MyInfoDto>> getMyInfo(@AuthenticationPrincipal User user) { // 내 정보 페이지
    var result = myContentService.getMyInfo(user);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @ApiOperation(
      value = "닉네임 변경",
      notes = "로그인정보(토큰)를 을통해 로그인한 닉네임을 변경합니다."
  )
  @PatchMapping("/nickname")
  public ResponseEntity<ApiResponse<PatchMyNicknameResult>> patchMyNickName(@AuthenticationPrincipal User user, @RequestBody
  PatchMyNicknameForm form) { //닉네임 변경
    var result = myContentService.patchNickname(user, PatchMyNicknameForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @ApiOperation(
      value = "사용자 정보 변경",
      notes = "로그인정보(토큰)를 을 통해 내 정보 변경"
  )
  @PatchMapping
  public ResponseEntity<ApiResponse<PatchMyInfoResultDto>> patchMyInfo(@AuthenticationPrincipal User user,
      @RequestBody PatchMyInfoForm form) { //내정보 변경
    var result = myContentService.patchMyInfo(user, PatchMyInfoForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @ApiOperation(
      value = "프로필 이미지 변경",
      notes = "로그인정보(토큰)의 프로필 이미지를 변경합니다. (이미지업로드)"
  )
  @PostMapping(value = "/image", consumes = "multipart/form-data")
  public ResponseEntity<ApiResponse<PostMyInfoImageResultDto>> postMyInfoImage(@AuthenticationPrincipal User user,
      @RequestPart MultipartFile image) throws IOException {
    var dto=  PostMyInfoImageRequestDto.builder().image(image).build();
    var result = myContentService.postMyInfoImage(user,dto);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @ApiOperation(
      value = "내가 신청한 신청목록 가져오기",
      notes = "내가 신청한 신청목록 리스트를 가져옵니다."
  )
  @GetMapping("/from-applicants")
  public ResponseEntity<ApiResponse<List<ApplyListResultDto>>> getMyFromApplicant(@AuthenticationPrincipal User user,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer size) {
    var result = myContentService.getMyFromApplicantList(user, page, size);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
  
  @ApiOperation(
      value = "내게 신청한 신청목록 가져오기",
      notes = "내게 신청한 신청목록을 가져옵니다. "
  )
  @GetMapping("/to-applicants")
  public ResponseEntity<ApiResponse<List<ApplyListResultDto>>> getMyToApplicantList(@AuthenticationPrincipal User user,
      @RequestParam(defaultValue = "1") Integer page,
      @RequestParam(defaultValue = "10") Integer size) {
    var result = myContentService.getMyToApplicantList(user, page, size);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
}
