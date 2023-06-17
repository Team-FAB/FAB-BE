package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
  Optional<User> findByEmail(String email);

  List<User> getRecommend(User user, Integer size);
}
