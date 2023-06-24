package com.fab.banggabgo.repository.impl;

import com.fab.banggabgo.entity.Apply;
import com.fab.banggabgo.entity.QApply;
import com.fab.banggabgo.repository.ApplyRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ApplyRepositoryImpl implements ApplyRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  QApply qApply = QApply.apply;

  @Override
  public Page<Apply> getMyApplicant(Pageable pageable, Integer userId) {
    var pageQuery = jpaQueryFactory.selectFrom(qApply)
        .leftJoin(qApply.applicantUser)
        .fetchJoin()
        .leftJoin(qApply.article)
        .fetchJoin()
        .where(qApply.article.user.id.eq(userId)
            .and(qApply.isArticleUserDelete.eq(false)))
        .orderBy(qApply.lastModifiedDate.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    var countQuery = jpaQueryFactory.select(qApply.count())
        .from(qApply)
        .where(qApply.article.user.id.eq(userId)
            .and(qApply.isArticleUserDelete.eq(false)));

    return new PageImpl<>(pageQuery.fetch(), pageable, countQuery.fetchOne());
  }

  @Override
  public Page<Apply> getMyToApplicant(Pageable pageable, Integer userId) {
    var pageQuery = jpaQueryFactory.selectFrom(qApply)
        .leftJoin(qApply.article)
        .fetchJoin()
        .leftJoin(qApply.article.user)
        .where(qApply.applicantUser.id.eq(userId)
            .and(qApply.isApplicantDelete.eq(false)))
        .orderBy(qApply.lastModifiedDate.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize());

    var countQuery = jpaQueryFactory.select(qApply.count())
        .from(qApply)
        .where(qApply.applicantUser.id.eq(userId)
            .and(qApply.isApplicantDelete.eq(false)));

    return new PageImpl<>(pageQuery.fetch(), pageable, countQuery.fetchOne());
  }

  @Override
  public List<Apply> getAllMyApplicantByArticleId(Integer userId, Integer articleId) {
    var pageQuery = jpaQueryFactory.selectFrom(qApply)
        .leftJoin(qApply.applicantUser)
        .fetchJoin()
        .leftJoin(qApply.article)
        .fetchJoin()
        .where(qApply.article.user.id.eq(userId).and(qApply.article.id.eq(articleId)))
        .orderBy(qApply.lastModifiedDate.desc());

    return pageQuery.fetch();
  }

  @Override
  @Transactional
  public Long setApplyDelete(Integer userId, Integer applyId, boolean isApplicant) {
    var pageQuery = jpaQueryFactory.update(qApply)
        .where(qApply.id.eq(applyId));
    if (isApplicant) {
      pageQuery.set(qApply.isApplicantDelete, true);
    } else {
      pageQuery.set(qApply.isArticleUserDelete, true);
    }

    return pageQuery.execute();
  }
}
