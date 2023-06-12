package com.fab.banggabgo.repository.impl;

import com.fab.banggabgo.entity.QUser;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
  private final JPAQueryFactory queryFactory;
  QUser qUser = QUser.user;
  public Optional<User> findByEmail(String email){
    var query=queryFactory.selectFrom(qUser)
            .leftJoin(qUser.roles).fetchJoin()
            .leftJoin(qUser.tag).fetchJoin()
            .where(qUser.email.eq(email));
    return Optional.ofNullable(query.fetchOne());
  }

}
