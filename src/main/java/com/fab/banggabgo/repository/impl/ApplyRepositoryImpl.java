package com.fab.banggabgo.repository.impl;

import com.fab.banggabgo.entity.Apply;
import com.fab.banggabgo.entity.QApply;
import com.fab.banggabgo.repository.ApplyRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  QApply qApply = QApply.apply;

  @Override
  public List<Apply> getMyApplicant(Pageable pageable, Integer userId) {
    var pageQuery = jpaQueryFactory.selectFrom(qApply)
        .leftJoin(qApply.applicantUser)
        .fetchJoin()
        .leftJoin(qApply.article)
        .fetchJoin()
        .where(qApply.article.user.id.eq(userId))
        .orderBy(qApply.lastModifiedDate.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    return pageQuery.fetch();
  }
}
