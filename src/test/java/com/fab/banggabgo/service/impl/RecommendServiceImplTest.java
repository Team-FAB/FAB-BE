package com.fab.banggabgo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import com.fab.banggabgo.dto.recommend.RecommendResponseDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.type.ActivityTime;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.Mbti;
import com.fab.banggabgo.type.Seoul;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecommendServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private RecommendServiceImpl recommendService;

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
    RecommendResponseDto result = recommendService.getRecommendUsers(user1, 9);

    //then
    assertEquals(9, result.getRecommendDtoList().size());
    assertEquals("ENFJ", result.getMbti());
  }
}