package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.dto.recommend.RecommendDto;
import com.fab.banggabgo.dto.recommend.RecommendResponseDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.service.RecommendService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

  private final UserRepository userRepository;

  @Override
  public RecommendResponseDto getRecommendUsers(User user, Integer size) {
    if (user.getIsSmoker() == null || user.getGender() == null || user.getActivityTime() == null
        || user.getRegion() == null || user.getMinAge() == null || user.getMaxAge() == null) {

      throw new CustomException(ErrorCode.INVALID_PROFILE);
    }

    List<User> userList = userRepository.getRecommend(user, size);

    return RecommendResponseDto.builder()
        .mbti(user.getMbti().toString())
        .recommendDtoList(RecommendDto.toDtoList(userList))
        .build();
  }
}
