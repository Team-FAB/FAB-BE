package com.fab.banggabgo.repository.impl;

import com.fab.banggabgo.dto.mycontent.FavoriteArticleDto;
import com.fab.banggabgo.dto.mycontent.MyArticleDto;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.QArticle;
import com.fab.banggabgo.entity.QLikeArticle;
import com.fab.banggabgo.entity.QUser;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ArticleRepositoryCustom;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.Period;
import com.fab.banggabgo.type.Seoul;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  QArticle qArticle = QArticle.article;
  QUser qUser = QUser.user;
  QLikeArticle qLikeArticle= QLikeArticle.likeArticle;
  @Override
  public Page<Article> getArticle(Pageable pageable, boolean isRecruiting) {

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

  @Override
  public Integer getArticleTotalCnt() {
    return Math.toIntExact(queryFactory.select(qArticle.count())
        .from(qArticle)
        .where(qArticle.isDeleted.eq(false))
        .fetchFirst());
  }

  public List<MyArticleDto> getMyArticle(User user) {

    var getMyArticleQuery=queryFactory.selectFrom(qArticle)
        .join(qArticle.user ,qUser)
        .fetchJoin()
        .where(qUser.eq(user))
        .orderBy(qArticle.isDeleted.asc(),qArticle.createDate.desc());

    return getMyArticleQuery.fetch()
        .stream().map(MyArticleDto::toDto)
        .collect(Collectors.toList());
  }
  
  public List<FavoriteArticleDto> getFavoriteArticle(User user){

    var getMyFavoriteArticleQuery=queryFactory
        .select(qLikeArticle.article)
        .from(qLikeArticle)
        .join(qLikeArticle.article,qArticle)
        .join(qLikeArticle.user,qUser)
        .where(qUser.eq(user));

    return getMyFavoriteArticleQuery.fetch()
        .stream().map(FavoriteArticleDto::toDto)
        .collect(Collectors.toList());
  }
}
