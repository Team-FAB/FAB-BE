package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

  Page<Article> getArticle(Pageable pageable, boolean isRecruiting);

  Page<Article> getArticleByFilter(Pageable pageable, boolean isRecruiting, String region,
      String period, String price, String gender);
}
