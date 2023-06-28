package com.fab.banggabgo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.dto.user.ProfileDto;
import com.fab.banggabgo.dto.user.RecommendResponseDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.type.ActivityTime;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.Mbti;
import com.fab.banggabgo.type.Seoul;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  @DisplayName("프로필 추천 불러오기 성공")
  void getRecommendUsersSuccess() {
    //given
    List<User> userList = new ArrayList<>();

    for (int i = 0; i < 9; i++) {
      User user = User.builder()
          .id(i + 1)
          .isSmoker(true)
          .gender(Gender.MALE)
          .region(Seoul.DONGJAK)
          .minAge(20)
          .maxAge(25)
          .activityTime(ActivityTime.MIDNIGHT)
          .nickname("유저" + i)
          .mbti(Mbti.ENTJ)
          .build();

      userList.add(user);
    }

    given(userRepository.getRecommend(any(), anyInt()))
        .willReturn(userList);

    User user1 = User.builder()
        .isSmoker(true)
        .gender(Gender.MALE)
        .region(Seoul.DONGJAK)
        .minAge(20)
        .maxAge(25)
        .activityTime(ActivityTime.MIDNIGHT)
        .mbti(Mbti.ENFJ)
        .build();
    //when
    RecommendResponseDto result = userService.getRecommendUsers(user1, 9);

    //then
    assertEquals(9, result.getRecommendDtoList().size());
    assertEquals("ENFJ", result.getMbti());
  }

  @Test
  @DisplayName("프로필 정보 불러오기 성공")
  void getUserProfileSuccess() {
    //given
    User user = User.builder()
        .id(1)
        .isSmoker(true)
        .gender(Gender.MALE)
        .region(Seoul.DONGJAK)
        .minAge(20)
        .maxAge(25)
        .activityTime(ActivityTime.MIDNIGHT)
        .nickname("유저")
        .mbti(Mbti.ENTJ)
        .build();

    given(userRepository.findById(anyInt()))
        .willReturn(Optional.ofNullable(user));

    //when
    ProfileDto result = userService.getUserProfile(1);

    //then
    assertEquals("남성", result.getGender());
    assertEquals("ENTJ", result.getMbti());
  }

  @Test
  @DisplayName("프로필 정보 불러오기 실패 : 해당 유저가 존재하지 않음")
  void getUserProfileFail_USER_IS_NULL() {
    //given
    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> userService.getUserProfile(1));

    //then
    assertEquals(exception.getMessage(), "유저 정보를 불러오는데 실패했습니다.");
  }
}