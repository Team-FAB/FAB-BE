package com.fab.banggabgo.service;

import com.fab.banggabgo.dto.ArticleEditDto;
import com.fab.banggabgo.dto.ArticlePageDto;
import com.fab.banggabgo.dto.ArticleRegisterDto;
import com.fab.banggabgo.entity.User;
import java.util.List;

public interface ArticleService {

  /**
   * 게시글 등록
   */
  void postArticle(User user, ArticleRegisterDto dto);

  /**
   * 게시글 수정
   */
  void putArticle(User user, Integer id, ArticleEditDto dto);

  /**
   * 게시글 삭제
   */
  void deleteArticle(User user, Integer id);

  /**
   * 게시글 페이징 불러오기
   */
  List<ArticlePageDto> getArticleByPageable(Integer page, Integer size, boolean isRecruiting);

  /**
   * 게시글 검색하기
   */
  List<ArticlePageDto> getArticleByFilter(Integer page, Integer size, boolean isRecruiting,
      String region, String period, String price, String gender);

  /**
   * 게시글 전체 개수
   */
  Integer getArticleTotalCnt();
}
