package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{

  @Query("select u from User u left join fetch u.roles where u.email=:email")
  Optional<User> findByEmail(String email);

  Integer countByEmail(String email);
  Integer countByNickname(String nickname);

  boolean existsByEmailOrNickname(String email, String nickname);
}
