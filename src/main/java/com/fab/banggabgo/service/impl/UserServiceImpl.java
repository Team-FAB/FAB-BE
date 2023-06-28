package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.dto.user.ProfileDto;
import com.fab.banggabgo.dto.user.RecommendDto;
import com.fab.banggabgo.dto.user.RecommendResponseDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public RecommendResponseDto getRecommendUsers(User user, Integer size) {
    if (checkUserProfile(user)) {
      throw new CustomException(ErrorCode.INVALID_PROFILE);
    }

    List<User> userList = userRepository.getRecommend(user, size);

    return RecommendResponseDto.builder()
        .mbti(user.getMbti().toString())
        .nickname(user.getNickname())
        .recommendDtoList(RecommendDto.toDtoList(userList))
        .build();
  }

  @Override
  public ProfileDto getUserProfile(int id) {
    User user = getUserById(id);

    return ProfileDto.toDto(user);
  }

  private boolean checkUserProfile(User user) {
    return user.getIsSmoker() == null || user.getGender() == null || user.getActivityTime() == null
        || user.getRegion() == null || user.getMinAge() == null || user.getMaxAge() == null;
  }

  private User getUserById(int id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_IS_NULL));

    return user;
  }
}
