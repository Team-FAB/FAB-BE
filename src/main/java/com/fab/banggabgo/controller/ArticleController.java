package com.fab.banggabgo.controller;

import com.fab.banggabgo.dto.ArticleRegisterForm;
import com.fab.banggabgo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {

  private final ArticleService articleService;

  @PostMapping
  public ResponseEntity<?> registerArticle(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @RequestBody ArticleRegisterForm form
  ) {
    articleService.registerArticle(token, ArticleRegisterForm.toDto(form));
    return ResponseEntity.created(null).build();
  }
}
