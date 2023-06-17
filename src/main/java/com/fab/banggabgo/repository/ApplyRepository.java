package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Integer> {

  boolean existsByApplicantUserIdAndArticleId(Integer applicantUserId, Integer articleId);
}
