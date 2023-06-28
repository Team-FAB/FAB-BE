package com.fab.banggabgo.service.impl;

import static org.mockito.ArgumentMatchers.any;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.entity.Apply;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.Mate;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ApplyRepository;
import com.fab.banggabgo.repository.MateRepository;
import com.fab.banggabgo.type.ActivityTime;
import com.fab.banggabgo.type.ApproveStatus;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.MatchStatus;
import com.fab.banggabgo.type.Mbti;
import com.fab.banggabgo.type.Period;
import com.fab.banggabgo.type.Seoul;
import com.fab.banggabgo.type.UserRole;
import com.fab.banggabgo.type.UserType;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {

  @Mock
  MateRepository mateRepository;
  @Mock
  ApplyRepository applyRepository;

  @InjectMocks
  ChatRoomServiceImpl chatRoomService;

  @Nested
  @DisplayName("채팅방 생성")
  class CreateChatRoom {

    private final User loginUser = User.builder()
        .id(1)
        .email("test@email.com")
        .password("abcd1234")
        .nickname("테스트")
        .userType(UserType.NORMAL)
        .image("http://image_uri")
        .matchStatus(MatchStatus.ACTIVITY)
        .isSmoker(true)
        .activityTime(ActivityTime.MORNING)
        .gender(Gender.MALE)
        .region(Seoul.DOBONG)
        .mbti(Mbti.ENFP)
        .minAge(20)
        .maxAge(30)
        .myAge(25)
        .tag(new HashSet<>())
        .detail("abcdef")
        .roles(List.of(UserRole.USER_ROLE))
        .build();

    private final User appliedUser = User.builder()
        .id(2)
        .email("test2@email.com")
        .password("abcdf12345")
        .nickname("테스트2")
        .userType(UserType.NORMAL)
        .image("http://image_uri2")
        .matchStatus(MatchStatus.ACTIVITY)
        .isSmoker(true)
        .activityTime(ActivityTime.MORNING)
        .gender(Gender.MALE)
        .region(Seoul.DOBONG)
        .mbti(Mbti.ENFP)
        .minAge(22)
        .maxAge(28)
        .myAge(26)
        .tag(new HashSet<>())
        .detail("abcdeffdaa")
        .roles(List.of(UserRole.USER_ROLE))
        .build();

    Mate mate = Mate.builder()
        .id(1)
        .chatRoomId("UUID")
        .user1(loginUser)
        .user2(appliedUser)
        .build();

    private final Article article = Article.builder()
        .id(1)
        .user(loginUser)
        .title("테스트 게시글")
        .content("test test test")
        .region(Seoul.DOBONG)
        .period(Period.ONETOTHREE)
        .price(10000000)
        .gender(Gender.MALE)
        .isRecruiting(true)
        .isDeleted(false)
        .build();

    Apply apply = Apply.builder()
        .id(1)
        .approveStatus(ApproveStatus.APPROVAL)
        .article(article)
        .applicantUser(appliedUser)
        .build();

    @Test
    @DisplayName("성공")
    void successCreateChatRoom() {
      BDDMockito.given(applyRepository.findById(ArgumentMatchers.anyInt()))
          .willReturn(Optional.of(apply));
      BDDMockito.given(mateRepository.findMate(any(), any()))
          .willReturn(Optional.of(mate));

      var result = chatRoomService.createChatRoom(loginUser, 1);

      Assertions.assertEquals(result.getId(), mate.getId());
      Assertions.assertEquals(result.getChatRoomId(), mate.getChatRoomId());
      Assertions.assertEquals(result.getUser1().getId(), mate.getUser1().getId());
      Assertions.assertEquals(result.getUser2().getId(), mate.getUser2().getId());
    }

    @Test
    @DisplayName("실패 - 승인상태가 아닐때")
    void failNotApprovalCreateChatRoom() {
      apply.setApproveStatus(ApproveStatus.WAIT);
      BDDMockito.given(applyRepository.findById(ArgumentMatchers.anyInt()))
          .willReturn(Optional.of(apply));

      CustomException customException = Assertions.assertThrows(
          CustomException.class, () -> chatRoomService.createChatRoom(loginUser, 1));

      Assertions.assertEquals(customException.getErrorCode(), ErrorCode.ONLY_MATCH_APPROVE);
    }
    @Test
    @DisplayName("실패 - 본인의 apply가 아닐때")
    void failNotUserApplyCreateChatRoom() {
      article.setUser(appliedUser);
      BDDMockito.given(applyRepository.findById(ArgumentMatchers.anyInt()))
          .willReturn(Optional.of(apply));

      CustomException customException = Assertions.assertThrows(
          CustomException.class, () -> chatRoomService.createChatRoom(loginUser, 1));

      Assertions.assertEquals(customException.getErrorCode(), ErrorCode.INVALID_APPLY_ID);
    }
    @Test
    @DisplayName("실패 - apply존재하지 않을때")
    void failNotFoundApplyCreateChatRoom() {
      BDDMockito.given(applyRepository.findById(ArgumentMatchers.anyInt()))
          .willReturn(Optional.empty());

      CustomException customException = Assertions.assertThrows(
          CustomException.class, () -> chatRoomService.createChatRoom(loginUser, 1));

      Assertions.assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_APPLY_ID);
    }
  }

}