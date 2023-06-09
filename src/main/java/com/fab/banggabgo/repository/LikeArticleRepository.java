package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.LikeArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeArticleRepository extends JpaRepository<LikeArticle, Integer> {

  boolean existsByUserIdAndArticleId(Integer userId, Integer articleId);
  LikeArticle findByUserIdAndArticleId(Integer userId, Integer articleId);
}
