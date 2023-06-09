package com.fab.banggabgo.service;

import com.fab.banggabgo.dto.apply.ApplyIsApplyResultDto;
import com.fab.banggabgo.dto.apply.ApplyUserResultDto;
import com.fab.banggabgo.dto.article.ArticleEditDto;
import com.fab.banggabgo.dto.article.ArticleInfoDto;
import com.fab.banggabgo.dto.article.ArticlePageDto;
import com.fab.banggabgo.dto.article.ArticlePageResultDto;
import com.fab.banggabgo.dto.article.ArticleRegisterDto;
import com.fab.banggabgo.entity.User;
import java.util.List;

public interface ArticleService {

  /**
   * 게시글 등록
   */
  void postArticle(User user, ArticleRegisterDto dto);

  /**
   * 게시글 가져오기
   */
  ArticlePageDto getArticle(Integer id);

  /**
   * 유저가 작성한 게시글 가져오기
   */
  List<ArticleInfoDto> getUserArticles(Integer userId);

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
  ArticlePageResultDto getArticleByPageable(Integer page, Integer size, boolean isRecruiting);

  /**
   * 게시글 검색하기
   */
  ArticlePageResultDto getArticleByFilter(Integer page, Integer size, boolean isRecruiting,
      String region, String period, String price, String gender);

  /**
   * 게시글 찜 등록/삭제
   */
  String postArticleFavorite(User user, Integer id);

  /**
   * 게시글 찜했는지
   */
  boolean getArticleFavorite(User user, Integer id);

  ApplyUserResultDto applyUser(User user, Integer articleId);

  ApplyIsApplyResultDto isApply(User user, Integer articleId);
}
