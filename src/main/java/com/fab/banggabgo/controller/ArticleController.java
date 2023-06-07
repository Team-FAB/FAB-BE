package com.fab.banggabgo.controller;

import com.fab.banggabgo.dto.ArticleEditForm;
import com.fab.banggabgo.dto.ArticleRegisterForm;
import com.fab.banggabgo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  public ResponseEntity<?> postArticle(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @RequestBody ArticleRegisterForm form
  ) {
    articleService.postArticle(token, ArticleRegisterForm.toDto(form));
    return ResponseEntity.created(null).build();
  }
  
  @PutMapping("/{id}")
  public ResponseEntity<?> putArticle(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @PathVariable long id,
      @RequestBody ArticleEditForm form
  ) {
    articleService.putArticle(token, id, ArticleEditForm.toDto(form));
    return ResponseEntity.created(null).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteArticle(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @PathVariable long id
  ) {
    articleService.deleteArticle(token, id);
    return ResponseEntity.noContent().build();
  }
}
