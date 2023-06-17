package com.fab.banggabgo.service;

import com.fab.banggabgo.dto.user.RecommendResponseDto;
import com.fab.banggabgo.entity.User;

public interface UserService {

  /**
   * 프로필 추천 불러오기
   */
  RecommendResponseDto getRecommendUsers(User user, Integer size);

  /**
   *
   */
}
