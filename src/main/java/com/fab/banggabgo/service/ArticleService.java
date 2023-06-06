package com.fab.banggabgo.service;

import com.fab.banggabgo.dto.ArticleEditDto;
import com.fab.banggabgo.dto.ArticleRegisterDto;

public interface ArticleService {

  /**
   * 게시글 등록
   */
  void registerArticle(String token, ArticleRegisterDto dto);

  /**
   * 게시글 수정
   */
  void editArticle(String token, Long id, ArticleEditDto dto);

  /**
   * 게시글 삭제
   */
  void deleteArticle(String token, Long id);
}
