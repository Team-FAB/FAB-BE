package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.dto.ArticleEditForm;
import com.fab.banggabgo.dto.ArticleRegisterForm;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.ArticleService;
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
  public ResponseEntity<?> postArticle(
      @AuthenticationPrincipal User user,
      @RequestBody ArticleRegisterForm form
  ) {
    articleService.postArticle(user, ArticleRegisterForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).toEntity();
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> putArticle(
      @AuthenticationPrincipal User user,
      @PathVariable int id,
      @RequestBody ArticleEditForm form
  ) {
    articleService.putArticle(user, id, ArticleEditForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).toEntity();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteArticle(
      @AuthenticationPrincipal User user,
      @PathVariable int id
  ) {
    articleService.deleteArticle(user, id);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).toEntity();
  }

  @GetMapping
  public ResponseEntity<?> getArticleByPageable(
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size,
      @RequestParam(value = "isRecruiting", defaultValue = "true") boolean isRecruiting
  ) {
    var result = articleService.getArticleByPageable(page, size, isRecruiting);
    return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
  }

  @GetMapping("/filter")
  public ResponseEntity<?> getArticleByFilter(
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
}
