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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

  private final ArticleService articleService;

  @PostMapping
  public ResponseEntity<ApiResponse<Object>> postArticle(
      @AuthenticationPrincipal User user,
      @RequestBody ArticleRegisterForm form
  ) {
    articleService.postArticle(user, ArticleRegisterForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).toEntity();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<ArticlePageDto>> getArticle(
      @PathVariable int id
  ) {
    var result = articleService.getArticle(id);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<ApiResponse<List<ArticleInfoDto>>> getUserArticles(
      @PathVariable int userId
  ) {
    var result = articleService.getUserArticles(userId);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> putArticle(
      @AuthenticationPrincipal User user,
      @PathVariable int id,
      @RequestBody ArticleEditForm form
  ) {
    articleService.putArticle(user, id, ArticleEditForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).toEntity();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Object>> deleteArticle(
      @AuthenticationPrincipal User user,
      @PathVariable int id
  ) {
    articleService.deleteArticle(user, id);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).toEntity();
  }

  @GetMapping
  public ResponseEntity<ApiResponse<ArticlePageResultDto>> getArticleByPageable(
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size,
      @RequestParam(value = "isRecruiting", defaultValue = "true") boolean isRecruiting
  ) {
    var result = articleService.getArticleByPageable(page, size, isRecruiting);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

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

  @PostMapping("/favorites/{id}")
  public ResponseEntity<ApiResponse<String>> postArticleFavorite(
      @AuthenticationPrincipal User user,
      @PathVariable int id
  ) {
    var result = articleService.postArticleFavorite(user, id);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).data(result).toEntity();
  }

  @GetMapping("/favorites/{id}")
  public ResponseEntity<ApiResponse<Boolean>> getArticleFavorite(
      @AuthenticationPrincipal User user,
      @PathVariable int id
  ) {
    var result = articleService.getArticleFavorite(user, id);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @PostMapping("/apply")
  public ResponseEntity<ApiResponse<ApplyUserResultDto>> getApply(@AuthenticationPrincipal User user,
      @RequestParam Integer articleId) {
    var result = articleService.applyUser(user, articleId);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).data(result).toEntity();
  }
}
