package com.fab.banggabgo.repository.impl;

import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.QArticle;
import com.fab.banggabgo.entity.QUser;
import com.fab.banggabgo.repository.ArticleRepositoryCustom;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.Period;
import com.fab.banggabgo.type.Seoul;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Article> getArticle(Pageable pageable, boolean isRecruiting) {
    QArticle qArticle = QArticle.article;
    QUser qUser = QUser.user;

    var articleQuery = queryFactory.selectFrom(qArticle)
        .join(qArticle.user, qUser)
        .fetchJoin()
        .orderBy(qArticle.createDate.desc())
        .where(qArticle.isDeleted.eq(false))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .distinct();

    if (isRecruiting) {
      articleQuery = articleQuery.where(qArticle.isRecruiting.eq(true));
    }

    List<Article> articleList = articleQuery.fetch();

    return new PageImpl<>(articleList);
  }

  @Override
  public Page<Article> getArticleByFilter(Pageable pageable, boolean isRecruiting, String region,
      String period, String price, String gender) {
    QArticle qArticle = QArticle.article;
    QUser qUser = QUser.user;

    var articleQuery = queryFactory.selectFrom(qArticle)
        .join(qArticle.user, qUser)
        .fetchJoin()
        .orderBy(qArticle.createDate.desc())
        .where(qArticle.isDeleted.eq(false))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .distinct();

    if (isRecruiting) {
      articleQuery = articleQuery.where(qArticle.isRecruiting.eq(true));
    }

    if (!"상관 없음".equals(region)) {
      articleQuery = articleQuery.where(qArticle.region.eq(Seoul.fromValue(region)));
    }

    if (!"상관 없음".equals(period)) {
      articleQuery = articleQuery.where(qArticle.period.eq(Period.fromValue(period)));
    }

    if (!"상관 없음".equals(price)) {
      articleQuery = articleQuery.where(qArticle.price.loe(Integer.parseInt(price)));
    }

    if (!"상관 없음".equals(gender)) {
      articleQuery = articleQuery.where(qArticle.gender.eq(Gender.fromValue(gender)));
    }

    List<Article> articleList = articleQuery.fetch();

    return new PageImpl<>(articleList);
  }
}
