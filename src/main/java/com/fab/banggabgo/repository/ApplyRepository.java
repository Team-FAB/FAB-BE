package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.Apply;
import com.fab.banggabgo.type.ApproveStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Integer>, ApplyRepositoryCustom {

  boolean existsByApplicantUserIdAndArticleId(Integer applicantUserId, Integer articleId);

  Optional<Apply> findByApplicantUserIdAndArticleId(Integer id, Integer articleId);

  List<Apply> findByArticleIdAndApproveStatus(Integer articleId, ApproveStatus approveStatus);
}
