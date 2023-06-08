package com.fab.banggabgo.service;

import com.fab.banggabgo.dto.ArticleEditDto;
import com.fab.banggabgo.dto.ArticlePageDto;
import com.fab.banggabgo.dto.ArticleRegisterDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

  /**
   * 게시글 등록
   */
  void postArticle(String token, ArticleRegisterDto dto);

  /**
   * 게시글 수정
   */
  void putArticle(String token, Long id, ArticleEditDto dto);

  /**
   * 게시글 삭제
   */
  void deleteArticle(String token, Long id);

  /**
   * 게시글 페이징 불러오기
   */
  List<ArticlePageDto> getArticleByPageable(Integer page, Integer size, boolean isRecruiting);
}
