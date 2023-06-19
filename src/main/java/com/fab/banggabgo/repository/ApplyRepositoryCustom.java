package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.Apply;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ApplyRepositoryCustom {

  List<Apply> getMyApplicant(Pageable pageable, Integer userId);

  List<Apply> getMyToApplicant(Pageable pageable, Integer userId);

  List<Apply> getAllMyApplicantByArticleId(Integer userId, Integer articleId);
}
