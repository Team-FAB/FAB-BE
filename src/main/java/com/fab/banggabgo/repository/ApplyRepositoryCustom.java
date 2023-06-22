package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.Apply;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplyRepositoryCustom {

  Page<Apply> getMyApplicant(Pageable pageable, Integer userId);

  Page<Apply> getMyToApplicant(Pageable pageable, Integer userId);

  List<Apply> getAllMyApplicantByArticleId(Integer userId, Integer articleId);

  Long setApplyDelete(Integer userId, Integer applyId, boolean isApplicant);

  Page<Apply> getMyNoticeApplicant(Pageable pageable, Integer userId);
}
