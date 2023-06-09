package com.fab.banggabgo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.dto.apply.ApproveUserDto;
import com.fab.banggabgo.entity.Apply;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ApplyRepository;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApplyServiceImplTest {

  @Mock
  ApplyRepository applyRepository;

  @InjectMocks
  ApplyServiceImpl applyService;

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
  private final User appliedUser2 = User.builder()
      .id(3)
      .email("test3@email.com")
      .password("abcdf12345")
      .nickname("테스트3")
      .userType(UserType.NORMAL)
      .image("http://image_uri3")
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
      .detail("abcdeffdaaa")
      .roles(List.of(UserRole.USER_ROLE))
      .build();
  private Article article = Article.builder()
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

  @Nested
  @DisplayName("룸메이트 승인")
  class Approve {

    List<Apply> applyList = List.of(Apply.builder()
            .approveStatus(ApproveStatus.WAIT)
            .article(article)
            .applicantUser(appliedUser)
            .build(),
        Apply.builder()
            .approveStatus(ApproveStatus.WAIT)
            .article(article)
            .applicantUser(appliedUser2)
            .build());


    ApproveUserDto approveUserDto = ApproveUserDto.builder()
        .userId(appliedUser.getId())
        .articleId(article.getId())
        .build();

    @Test
    @DisplayName("룸메이트 승인 - 성공")
    void patchApproveSuccess() {
      given(applyRepository.getAllMyApplicantByArticleId(any(), any())).willReturn(applyList);

      var result = applyService.patchApprove(loginUser, approveUserDto);

      assertEquals(result.getApproveStatus(), ApproveStatus.APPROVAL.getValue());
      assertEquals(result.getApproveUserId(), appliedUser.getId());
      assertEquals(result.getApproveUserName(), appliedUser.getNickname());
      assertEquals(result.getArticleId(), article.getId());
      assertEquals(result.getArticleTitle(), article.getTitle());
    }
    @Test
    @DisplayName("룸메이트 승인 - 유저 id 입력값이 올바르지 않은경우")
    void patchApproveFailBadRequestUserId() {
      approveUserDto.setUserId(null);

      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.patchApprove(loginUser, approveUserDto));

      assertEquals(customException.getErrorCode(), ErrorCode.INVALID_ARTICLE);
    }
    @Test
    @DisplayName("룸메이트 승인 - article id 입력값이 올바르지 않은경우")
    void patchApproveFailBadRequestArticleId() {
      approveUserDto.setArticleId(null);

      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.patchApprove(loginUser, approveUserDto));

      assertEquals(customException.getErrorCode(), ErrorCode.INVALID_ARTICLE);
    }

    @Test
    @DisplayName("룸메이트 승인 - 입력받은 게시글이 본인의 게시글이 아니거나 없는경우")
    void patchApproveFailNonArticles() {

      given(applyRepository.getAllMyApplicantByArticleId(any(), any())).willReturn(List.of());

      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.patchApprove(loginUser, approveUserDto));

      assertEquals(customException.getErrorCode(), ErrorCode.ARTICLE_NOT_EXISTS);
    }

    @Test
    @DisplayName("룸메이트 승인 - 입력받은 게시글이 모집 완료된 경우")
    void patchApproveFailRecruiting() {

      article.setRecruiting(false);

      given(applyRepository.getAllMyApplicantByArticleId(any(), any())).willReturn(applyList);

      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.patchApprove(loginUser, approveUserDto));

      assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_END_RECRUITING);
    }
    @Test
    @DisplayName("룸메이트 승인 - 입력받은 게시글이 삭제된 경우")
    void patchApproveFailDeleted() {

      article.setDeleted(true);

      given(applyRepository.getAllMyApplicantByArticleId(any(), any())).willReturn(applyList);

      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.patchApprove(loginUser, approveUserDto));

      assertEquals(customException.getErrorCode(), ErrorCode.ARTICLE_DELETED);
    }
    @Test
    @DisplayName("룸메이트 승인 - 신청목록에 입력받은 유저가 없는경우")
    void patchApproveFailNotMatchUserId() {
      approveUserDto.setUserId(4);

      given(applyRepository.getAllMyApplicantByArticleId(any(), any())).willReturn(applyList);

      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.patchApprove(loginUser, approveUserDto));

      assertEquals(customException.getErrorCode(), ErrorCode.INVALID_APPLY_USER_ID);
    }
  }
  @Nested
  @DisplayName("룸메이트 거절")
  class Refuse {

    Apply apply = Apply.builder()
            .approveStatus(ApproveStatus.WAIT)
            .article(article)
            .applicantUser(appliedUser)
            .build();
    @Test
    @DisplayName("룸메이트 거절 - 성공")
    void patchRefuseSuccess() {
      given(applyRepository.findById(any())).willReturn(Optional.of(apply));

      var result = applyService.patchRefuse(loginUser, 1);

      assertEquals(result.getApproveStatus(), ApproveStatus.REFUSE.getValue());
      assertEquals(result.getApproveUserId(), appliedUser.getId());
      assertEquals(result.getApproveUserName(), appliedUser.getNickname());
      assertEquals(result.getArticleId(), article.getId());
      assertEquals(result.getArticleTitle(), article.getTitle());
    }
    @Test
    @DisplayName("룸메이트 거절 - 유저 id 입력값이 올바르지 않은경우")
    void patchRefuseFailBadRequestUserId() {
      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.patchRefuse(loginUser, null));

      assertEquals(customException.getErrorCode(), ErrorCode.INVALID_ARTICLE);
    }
    @Test
    @DisplayName("룸메이트 거절 - apply가 없는경우")
    void patchRefuseFailNonArticles() {


      given(applyRepository.findById(any())).willReturn(Optional.empty());
      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.patchRefuse(loginUser, 1));

      assertEquals(customException.getErrorCode(), ErrorCode.NOT_FOUND_APPLY_ID);
    }

    @Test
    @DisplayName("룸메이트 거절 - 본인이 작성한 게시글의 apply가 아닐때")
    void patchRefuseFailRecruiting() {
      article.setUser(appliedUser);

      given(applyRepository.findById(any())).willReturn(Optional.of(apply));
      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.patchRefuse(loginUser, 1));

      assertEquals(customException.getErrorCode(), ErrorCode.USER_NOT_MATCHED);
    }
    @Test
    @DisplayName("룸메이트 거절 - 입력받은 게시글이 삭제된 경우")
    void patchRefuseFailDeleted() {
      article.setDeleted(true);

      given(applyRepository.findById(any())).willReturn(Optional.of(apply));
      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.patchRefuse(loginUser, 1));

      assertEquals(customException.getErrorCode(), ErrorCode.ARTICLE_DELETED);
    }
    @Test
    @DisplayName("룸메이트 거절 - 입력받은 게시글이 모집종료된 경우")
    void patchRefuseFailNotRecruiting() {
      article.setRecruiting(false);

      given(applyRepository.findById(any())).willReturn(Optional.of(apply));
      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.patchRefuse(loginUser, 1));

      assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_END_RECRUITING);
    }
    @Test
    @DisplayName("룸메이트 거절 - 이미 거절한 상태인 경우")
    void patchRefuseFailNotMatchUserId() {
      apply.setApproveStatus(ApproveStatus.REFUSE);

      given(applyRepository.findById(any())).willReturn(Optional.of(apply));
      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.patchRefuse(loginUser, 1));

      assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_REFUSE);
    }
  }
  @Nested
  @DisplayName("룸메이트 목록 제거")
  class DeleteApply {

    Apply apply = Apply.builder()
            .approveStatus(ApproveStatus.WAIT)
            .article(article)
            .applicantUser(appliedUser)
            .build();
    @Test
    @DisplayName("룸메이트 목록 제거 - 성공")
    void deleteApplySuccess() {
      given(applyRepository.findById(any())).willReturn(Optional.of(apply));
      given(applyRepository.setApplyDelete(anyInt(), anyInt(), anyBoolean())).willReturn(1L);
      var result = applyService.deleteApply(loginUser, 1);

      assertEquals(result.getApplyId(), 1);
    }
    @Test
    @DisplayName("룸메이트 목록 제거 - apply가 없는경우")
    void deleteApplyFailNonArticles() {
      given(applyRepository.findById(any())).willReturn(Optional.empty());
      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.deleteApply(loginUser, 1));

      assertEquals(customException.getErrorCode(), ErrorCode.INVALID_APPLY_ID);
    }

    @Test
    @DisplayName("룸메이트 목록 제거 - 본인이 작성한 게시글의 apply가 아닐때")
    void deleteApplyFailRecruiting() {
      article.setUser(appliedUser);
      apply.setApplicantUser(appliedUser2);

      given(applyRepository.findById(any())).willReturn(Optional.of(apply));
      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.deleteApply(loginUser, 1));

      assertEquals(customException.getErrorCode(), ErrorCode.USER_NOT_MATCHED);
    }
  }

  @Nested
  @DisplayName("알림 제거")
  class DeleteNotice {

    Apply apply = Apply.builder()
            .approveStatus(ApproveStatus.WAIT)
            .article(article)
            .applicantUser(appliedUser)
            .build();
    @Test
    @DisplayName("알림 제거 - 성공")
    void deleteNoticeSuccess() {
      given(applyRepository.findById(any())).willReturn(Optional.of(apply));
      given(applyRepository.setRead(anyInt(), anyBoolean())).willReturn(1L);
      var result = applyService.deleteNotice(loginUser, 1);

      assertEquals(result.getApplyId(), 1);
    }
    @Test
    @DisplayName("알림 제거 - apply가 없는경우")
    void deleteNoticeFailNonArticles() {
      given(applyRepository.findById(any())).willReturn(Optional.empty());
      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.deleteNotice(loginUser, 1));

      assertEquals(customException.getErrorCode(), ErrorCode.INVALID_APPLY_ID);
    }

    @Test
    @DisplayName("알림 제거 - 본인이 작성한 게시글의 apply가 아닐때")
    void deleteNoticeFailRecruiting() {
      article.setUser(appliedUser);
      apply.setApplicantUser(appliedUser2);

      given(applyRepository.findById(any())).willReturn(Optional.of(apply));
      CustomException customException = assertThrows(CustomException.class,
          () -> applyService.deleteNotice(loginUser, 1));

      assertEquals(customException.getErrorCode(), ErrorCode.USER_NOT_MATCHED);
    }
  }

}