package com.fab.banggabgo.repository;

import com.fab.banggabgo.dto.mycontent.FavoriteArticleDto;
import com.fab.banggabgo.dto.mycontent.MyArticleDto;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

  Page<Article> getArticle(Pageable pageable, boolean isRecruiting);

  Page<Article> getArticleByFilter(Pageable pageable, boolean isRecruiting, String region,
      String period, String price, String gender);

  Integer getArticleTotalCnt();

  List<MyArticleDto> getMyArticle(User user);

  List<FavoriteArticleDto> getFavoriteArticle(User user);
}
