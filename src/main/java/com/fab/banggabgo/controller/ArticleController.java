package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.dto.apply.ApplyUserResultDto;
import com.fab.banggabgo.dto.article.ArticleEditForm;
import com.fab.banggabgo.dto.article.ArticleInfoDto;
import com.fab.banggabgo.dto.article.ArticlePageDto;
import com.fab.banggabgo.dto.article.ArticlePageResultDto;
import com.fab.banggabgo.dto.article.ArticleRegisterForm;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Api(tags = {"Article Controller 게시물 API"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

  private final ArticleService articleService;
  @ApiOperation(
      value = "게시물 작성",
      notes = "게시물을 작성합니다."
  )
  @PostMapping
  public ResponseEntity<ApiResponse<Object>> postArticle(
      @AuthenticationPrincipal User user,
      @RequestBody ArticleRegisterForm form
  ) {
    articleService.postArticle(user, ArticleRegisterForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).toEntity();
  }
  @ApiOperation(
      value = "게시물 가져오기",
      notes = "id 에 해당하는 게시물을 가져옵니다."
  )
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ArticlePageDto>> getArticle(
      @PathVariable int id
  ) {
    var result = articleService.getArticle(id);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
  @ApiOperation(
      value = "작성자 작성글 보기",
      notes = "id 에 해당하는 유저의 작성글을 불러옵니다."
  )
  @GetMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<List<ArticleInfoDto>>> getUserArticles(
      @PathVariable int userId
  ) {
    var result = articleService.getUserArticles(userId);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
  @ApiOperation(
      value = "게시물 수정",
      notes = "id 에 해당하는 게시물의 내용을 수정합니다."
  )
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> putArticle(
      @AuthenticationPrincipal User user,
      @PathVariable int id,
      @RequestBody ArticleEditForm form
  ) {
    articleService.putArticle(user, id, ArticleEditForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).toEntity();
  }
  @ApiOperation(
      value = "게시물 삭제",
      notes = "id 에 해당하는 게시물을 삭제합니다."
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> deleteArticle(
      @AuthenticationPrincipal User user,
      @PathVariable int id
  ) {
    articleService.deleteArticle(user, id);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).toEntity();
  }
  @ApiOperation(
      value = "게시물 조회",
      notes = "모집 마감 여부 에 해당하는 글들을 최신순으로 반환합니다."
  )
  @GetMapping
  public ResponseEntity<ApiResponse<ArticlePageResultDto>> getArticleByPageable(
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size,
      @RequestParam(value = "isRecruiting", defaultValue = "true") boolean isRecruiting
  ) {
    var result = articleService.getArticleByPageable(page, size, isRecruiting);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
  @ApiOperation(
      value = "게시물 검색(필터)",
      notes = "검색 조건(파라미터) 에 해당하는 글들을 반환합니다."
  )
  @GetMapping("/filter")
  public ResponseEntity<ApiResponse<ArticlePageResultDto>> getArticleByFilter(
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size,
      @RequestParam(value = "isRecruiting", defaultValue = "true") boolean isRecruiting,
      @RequestParam("region") String region,
      @RequestParam("period") String period,
      @RequestParam("price") String price,
      @RequestParam("gender") String gender
  ) {
    var result = articleService.getArticleByFilter(page, size, isRecruiting, region, period, price,
        gender);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }
  @ApiOperation(
      value = "게시물 좋아요",
      notes = "id 에 해당하는 게시물을 좋아요합니다."
  )
  @PostMapping("/favorites/{id}")
  public ResponseEntity<ApiResponse<String>> postArticleFavorite(
      @AuthenticationPrincipal User user,
      @PathVariable int id
  ) {
    var result = articleService.postArticleFavorite(user, id);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).data(result).toEntity();
  }
  @ApiOperation(
      value = "게시물 좋아요 여부 확인",
      notes = "id 에 해당하는 게시물을 좋아요 여부를 가져옵니다."
  )
  @GetMapping("/favorites/{id}")
  public ResponseEntity<ApiResponse<Boolean>> getArticleFavorite(
      @AuthenticationPrincipal User user,
      @PathVariable int id
  ) {
    var result = articleService.getArticleFavorite(user, id);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @ApiOperation(
      value = "룸메이트 지원하기",
      notes = "id 에 해당하는 게시물에 룸메이트를 지원합니다."
  )
  @PostMapping("/apply/{articleId}")
  public ResponseEntity<ApiResponse<ApplyUserResultDto>> postApply(@AuthenticationPrincipal User user,
      @PathVariable Integer articleId) {
    var result = articleService.applyUser(user, articleId);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).data(result).toEntity();
  }

  @ApiOperation(
      value = "룸메이트 지원여부 확인",
      notes = "id 에 해당하는 게시물에 룸메이트를 지원 했는지 확인합니다."
  )
  @GetMapping("/apply/{articleId}")
  public ResponseEntity<?> getApply(@AuthenticationPrincipal User user,
      @PathVariable Integer articleId) {
    var result = articleService.isApply(user, articleId);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).data(result).toEntity();
  }
}
