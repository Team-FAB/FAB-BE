package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.Article;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

  Page<Article> getArticle(Pageable pageable, boolean isRecruiting);
}
