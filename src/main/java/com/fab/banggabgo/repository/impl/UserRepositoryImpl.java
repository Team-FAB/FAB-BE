package com.fab.banggabgo.repository.impl;

import com.fab.banggabgo.entity.QArticle;
import com.fab.banggabgo.entity.QUser;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepositoryCustom;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  QUser qUser = QUser.user;
  QArticle qArticle = QArticle.article;

  public Optional<User> findByEmail(String email) {
    var query = queryFactory.selectFrom(qUser)
        .leftJoin(qUser.roles).fetchJoin()
        .leftJoin(qUser.tag).fetchJoin()
        .where(qUser.email.eq(email));
    return Optional.ofNullable(query.fetchOne());
  }

  @Override
  public List<User> getRecommend(User user, Integer size) {

    var subQuery = JPAExpressions.select(qArticle.user)
        .from(qArticle)
        .where(qArticle.isDeleted.eq(false)
            .and(qArticle.isRecruiting.eq(true)));

    var userQuery = queryFactory.selectFrom(qUser)
        .where(qUser.gender.eq(user.getGender())
            .and(qUser.isSmoker.eq(user.getIsSmoker()))
            .and(qUser.region.eq(user.getRegion()))
            .and(qUser.activityTime.eq(user.getActivityTime()))
            .and(qUser.minAge.eq(user.getMinAge()))
            .and(qUser.maxAge.eq(user.getMaxAge()))
            .and(qUser.id.ne(user.getId()))
            .and(qUser.in(subQuery)))
        .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
        .limit(size);

    return userQuery.fetch();
  }

}
