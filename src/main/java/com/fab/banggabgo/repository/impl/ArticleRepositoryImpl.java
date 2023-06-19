package com.fab.banggabgo.repository.impl;

import com.fab.banggabgo.dto.article.ArticleInfoDto;
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
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Value("${query.default.static}")
  private String default_option;
  QArticle qArticle = QArticle.article;
  QUser qUser = QUser.user;
  QLikeArticle qLikeArticle= QLikeArticle.likeArticle;
  @Override
  public Page<Article> getArticle(Pageable pageable, boolean isRecruiting) {

    var articleQuery = queryFactory.selectFrom(qArticle)
        .join(qArticle.user, qUser)
        .fetchJoin()
        .orderBy(qArticle.createDate.desc())
        .where(eqDelete(false),eqRecruiting(true))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .distinct();

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
        .where(eqDelete(false)
            ,eqRecruiting(true)
            ,eqGender(gender)
            ,eqPeriod(period)
            ,eqRegion(region)
          ,loePrice(price))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .distinct();



    List<Article> articleList = articleQuery.fetch();

    return new PageImpl<>(articleList);
  }

  @Override
  public Integer getArticleTotalCnt() {
    return Math.toIntExact(queryFactory.select(qArticle.count())
        .from(qArticle)
        .where(eqDelete(false))
        .fetchFirst());
  }

  public List<MyArticleDto> getMyArticle(User user) {

    var getMyArticleQuery=queryFactory.selectFrom(qArticle)
        .join(qArticle.user ,qUser)
        .fetchJoin()
        .where(qUser.eq(user).and(eqDelete(false)))
        .orderBy(qArticle.isRecruiting.desc() ,qArticle.createDate.desc());
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
        .where(qUser.eq(user),eqDelete(false));

    return getMyFavoriteArticleQuery.fetch()
        .stream().map(FavoriteArticleDto::toDto)
        .collect(Collectors.toList());
  }

  public List<ArticleInfoDto> getUserArticle(User user) {

    var getUserArticleQuery = queryFactory.selectFrom(qArticle)
        .join(qArticle.user, qUser)
        .fetchJoin()
        .where(qUser.eq(user),eqDelete(false),eqRecruiting(true))
        .orderBy(qArticle.createDate.desc());

    return getUserArticleQuery.fetch()
        .stream().map(ArticleInfoDto::toDto)
        .collect(Collectors.toList());
  }

  private BooleanExpression eqDelete(boolean deleted){
    return qArticle.isDeleted.eq(deleted);
  }
  private BooleanExpression eqRecruiting(boolean isRecruiting){
    return qArticle.isRecruiting.eq(isRecruiting);
  }
  private BooleanExpression eqPeriod(String period){
    return StringUtils.hasText(period)? qArticle.period.eq(Period.fromValue(period)):null;
  }
  private BooleanExpression eqGender(String gender){
    return StringUtils.hasText(gender)? qArticle.gender.eq(Gender.fromValue(gender)):null;
  }
  private BooleanExpression eqRegion(String region){
    return StringUtils.hasText(region)? qArticle.region.eq(Seoul.fromValue(region)):null;
  }
  private BooleanExpression loePrice(String price){
    return StringUtils.hasText(price)? qArticle.price.loe(Integer.parseInt(price)):null;
  }
}
