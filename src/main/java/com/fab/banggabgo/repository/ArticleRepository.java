package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer>,
    ArticleRepositoryCustom {

  int countByUserAndIsDeletedFalseAndIsRecruitingTrue(User user);

  Optional<Article> findByIdAndIsDeletedFalse(int Id);
}
