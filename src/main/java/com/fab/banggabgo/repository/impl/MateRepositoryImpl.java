package com.fab.banggabgo.repository.impl;

import com.fab.banggabgo.entity.Mate;
import com.fab.banggabgo.entity.QMate;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.MateRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MateRepositoryImpl implements MateRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  QMate qMate = QMate.mate;

  @Override
  public Optional<Mate> findMate(User user1, User user2) {
    var pageQuery = queryFactory.selectFrom(qMate)
        .where(qMate.user1.id.eq(user1.getId())
            .and(qMate.user2.id.eq(user2.getId()))
            .or(qMate.user2.id.eq(user1.getId())
                .and(qMate.user1.id.eq(user2.getId()))));
    return Optional.ofNullable(pageQuery.fetchOne());
  }

  @Override
  public List<Mate> getChatList(Integer userId) {
    var pageQuery = queryFactory.selectFrom(qMate)
        .leftJoin(qMate.user1)
        .fetchJoin()
        .leftJoin(qMate.user2)
        .fetchJoin()
        .where(qMate.user1.id.eq(userId)
            .or(qMate.user2.id.eq(userId)));

    return pageQuery.fetch();
  }
}
