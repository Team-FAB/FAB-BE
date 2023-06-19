package com.fab.banggabgo.repository.impl;

import com.fab.banggabgo.entity.QArticle;
import com.fab.banggabgo.entity.QUser;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  QUser qUser = QUser.user;
  QArticle qArticle = QArticle.article;

  @Override
  public Optional<User> findByEmail(String email) {
    var query = queryFactory.selectFrom(qUser)
        .leftJoin(qUser.roles).fetchJoin()
        .leftJoin(qUser.tag).fetchJoin()
        .where(eqEmail(email));
    return Optional.ofNullable(query.fetchOne());
  }

  @Override
  public List<User> getRecommend(User user, Integer size) {

    var subQuery = JPAExpressions.select(qArticle.user)
        .from(qArticle)
        .where(qArticle.isDeleted.eq(false)
            .and(qArticle.isRecruiting.eq(true)));

    var userQuery = queryFactory.selectFrom(qUser)
        .where(eqGender(user),
            eqSmoker(user),
            preferAge(user),
            eqRegion(user),
            neSelf(user),
            eqActivityTime(user),
            userInSubQuery(subQuery)
        )
        .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
        .limit(size);

    return userQuery.fetch();
  }

  BooleanExpression eqEmail(String email){
    return qUser.email.eq(email);
  }

  BooleanExpression eqGender(User user) {
    return qUser.gender.eq(user.getGender());
  }

  BooleanExpression eqSmoker(User user) {
    return qUser.isSmoker.eq(user.getIsSmoker());
  }

  BooleanExpression preferAge(User user) {
    return qUser.myAge.between(user.getMinAge(), user.getMaxAge());
  }

  BooleanExpression eqRegion(User user) {
    return qUser.region.eq(user.getRegion());
  }

  BooleanExpression userInSubQuery(JPQLQuery<User> sub_query) {
    return qUser.in(sub_query);
  }

  BooleanExpression neSelf(User user) {
    return qUser.id.ne(user.getId());
  }

  BooleanExpression eqActivityTime(User user) {
    return qUser.activityTime.eq(user.getActivityTime());
  }
}
