package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.User;
import java.util.Optional;

public interface UserRepositoryCustom {
  Optional<User> findByEmail(String email);
}
