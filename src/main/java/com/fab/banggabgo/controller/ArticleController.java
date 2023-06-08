package com.fab.banggabgo.controller;

import com.fab.banggabgo.common.ApiResponse;
import com.fab.banggabgo.common.ResponseCode;
import com.fab.banggabgo.dto.ArticleEditForm;
import com.fab.banggabgo.dto.ArticleRegisterForm;
import com.fab.banggabgo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {

  private final ArticleService articleService;

  @PostMapping
  public ResponseEntity<?> postArticle(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @RequestBody ArticleRegisterForm form
  ) {
    articleService.postArticle(token, ArticleRegisterForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).toEntity();
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> putArticle(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @PathVariable long id,
      @RequestBody ArticleEditForm form
  ) {
    articleService.putArticle(token, id, ArticleEditForm.toDto(form));
    return ApiResponse.builder().code(ResponseCode.RESPONSE_CREATED).toEntity();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteArticle(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @PathVariable long id
  ) {
    articleService.deleteArticle(token, id);
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
}
