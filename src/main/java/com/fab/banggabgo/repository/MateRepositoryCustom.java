package com.fab.banggabgo.repository;

import com.fab.banggabgo.entity.Mate;
import com.fab.banggabgo.entity.User;
import java.util.Optional;

public interface MateRepositoryCustom {
  Optional<Mate> findMate(User user1, User user2);
}
