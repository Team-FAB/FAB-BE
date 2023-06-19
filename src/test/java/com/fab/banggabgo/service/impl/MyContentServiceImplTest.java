package com.fab.banggabgo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.dto.mycontent.FavoriteArticleDto;
import com.fab.banggabgo.dto.mycontent.MyArticleDto;
import com.fab.banggabgo.dto.mycontent.PatchMyInfoForm;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameForm;
import com.fab.banggabgo.entity.Apply;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ApplyRepository;
import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.type.ActivityTime;
import com.fab.banggabgo.type.ApproveStatus;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.MatchStatus;
import com.fab.banggabgo.type.Mbti;
import com.fab.banggabgo.type.Period;
import com.fab.banggabgo.type.Seoul;
import com.fab.banggabgo.type.UserRole;
import com.fab.banggabgo.type.UserType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
class MyContentServiceImplTest {

  @Mock
  private ArticleRepository articleRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ApplyRepository applyRepository;
  @InjectMocks
  private MyContentServiceImpl myContentService;

  User stub_user = User.builder()
      .id(1)
      .nickname("원래이름")
      .email("Origin")
      .build();

  @BeforeEach
  void init() {
    Authentication auth = mock(Authentication.class);
    lenient().when(auth.getPrincipal()).thenReturn(stub_user);
    SecurityContext securityContext = mock(SecurityContext.class);
    lenient().when(securityContext.getAuthentication()).thenReturn(auth);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void getMyArticle() { // 내 아티클 가져오기
    //given
    List<MyArticleDto> stub_list = new ArrayList<>();
    for (int i = 1; i <= 5; i++) {
      stub_list.add(MyArticleDto.builder()
          .id(i)
          .title("test" + i)
          .content("content" + i)
          .build());
    }
    //when
    when(articleRepository.getMyArticle(any(User.class))).thenReturn(stub_list);
    //then
    assertEquals(myContentService.getMyArticle(stub_user), stub_list);
  }

  @Test
  void getMyFavoriteArticle() { // 좋아요 한글 가져오기
    List<FavoriteArticleDto> stub_list = new ArrayList<>();
    for (int i = 1; i <= 5; i++) {
      stub_list.add(FavoriteArticleDto.builder()
          .id(i)
          .title("test" + i)
          .content("content" + i)
          .build());
    }
    //when
    when(articleRepository.getFavoriteArticle(any(User.class))).thenReturn(stub_list);
    //then
    assertEquals(myContentService.getMyFavoriteArticle(stub_user), stub_list);

  }

  @Nested
  @DisplayName("내정보 불러오기")
  class MyInfo {

    @Test
    @DisplayName("특정 데이터가 null일경우")
    void null_test() {
      //given
      //when
      var result = myContentService.getMyInfo(stub_user);
      //then
      System.out.println(result.toString());
    }
  }

  @Nested
  @DisplayName("닉네임 변경")
  class PatchNick {

    @Test
    @DisplayName("닉네임 변경 성공케이스")
    void Patch_nick_success() {

      //given
      var form = PatchMyNicknameForm.builder()
          .nickname("당당한무지")
          .build();

      //when
      when(userRepository.save(stub_user)).thenReturn(stub_user);
      var result = myContentService.patchNickname(stub_user, PatchMyNicknameForm.toDto(form));
      //then
      assertEquals(result.getNickname(), "당당한무지");
    }

    @Test
    @DisplayName("닉네임 변경 실패(이미 존재하는 닉네임일경우)")
    void Patch_nick_duplicate() {
      //given
      var form = PatchMyNicknameForm.builder()
          .nickname("당당한무지")
          .build();
      //when
      when(userRepository.save(stub_user)).thenThrow(DataIntegrityViolationException.class);
      //then
      Assertions.assertThrows(CustomException.class,
          () -> myContentService.patchNickname(stub_user, PatchMyNicknameForm.toDto(form)));
    }
  }

  @Nested
  @DisplayName("내정보 변경")
  class patchMyInfo {

    PatchMyInfoForm form = PatchMyInfoForm.builder()
        .gender("남성")
        .region("강남구")
        .activityTime("오후")
        .myAge(15)
        .favoriteTag(new ArrayList<>(List.of(new String[]{"tag1", "tag2"})))
        .myText("자세한정보")
        .mbti("INFP")
        .isSmoke(true)
        .maxAge(25)
        .minAge(21)
        .build();

    @Test
    @DisplayName("- 성공케이스")
    void patch_my_info_success() {
      //given
      //when
      when(userRepository.save(stub_user)).thenReturn(stub_user);
      //then
      var result = myContentService.patchMyInfo(stub_user, PatchMyInfoForm.toDto(form));

      verify(userRepository, times(1)).save(any(User.class));
      assertEquals(result.getMbti(), "INFP");
    }

    @Test
    @DisplayName("- form 값이 잘못된경우")
    void patch_my_info_missedValue_form() {
      //given
      form.setActivityTime(null);
      //when
      //then
      assertThrows(CustomException.class,
          () -> myContentService.patchMyInfo(stub_user, PatchMyInfoForm.toDto(form)));
    }

  }

  @Nested
  @DisplayName("신청자 목록 불러오기")
  class fromApplicant {

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
    @Test
    @DisplayName("신청자 목록 - 성공")
    @WithMockUser
    void getApplicantsSuccess() {

      when(applyRepository.getMyApplicant(any(), anyInt())).thenReturn(List.of(Apply.builder()
              .approveStatus(ApproveStatus.WAIT)
                  .article(article)
                  .applicantUser(appliedUser)
                  .approveStatus(ApproveStatus.WAIT)
              .build()));

      var result = myContentService.getMyFromApplicantList(loginUser, 4, 1);

      verify(applyRepository, times(1)).getMyApplicant(any(Pageable.class), any(Integer.class));
      assertEquals(result.get(0).getMatchStatus(), ApproveStatus.WAIT.getValue());
      assertEquals(result.get(0).getArticleTitle(), article.getTitle());
      assertEquals(result.get(0).getArticleId(), article.getId());
      assertEquals(result.get(0).getOtherUserName(), appliedUser.getNickname());
      assertEquals(result.get(0).getOtherUserId(), appliedUser.getId());
    }
  }

  @Nested
  @DisplayName("신청한 목록 불러오기")
  class toApplicant {
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

    private Article article = Article.builder()
        .id(1)
        .user(appliedUser)
        .title("테스트 게시글")
        .content("test test test")
        .region(Seoul.DOBONG)
        .period(Period.ONETOTHREE)
        .price(10000000)
        .gender(Gender.MALE)
        .isRecruiting(true)
        .isDeleted(false)
        .build();
    @Test
    @DisplayName("신청한 목록 - 성공")
    @WithMockUser
    void getApplicantsSuccess() {
      when(applyRepository.getMyToApplicant(any(), anyInt())).thenReturn(List.of(Apply.builder()
          .approveStatus(ApproveStatus.WAIT)
          .article(article)
          .applicantUser(loginUser)
          .approveStatus(ApproveStatus.WAIT)
          .build()));

      var result = myContentService.getMyToApplicantList(loginUser, 4, 1);

      verify(applyRepository, times(1)).getMyToApplicant(any(Pageable.class), any(Integer.class));
      assertEquals(result.get(0).getMatchStatus(), ApproveStatus.WAIT.getValue());
      assertEquals(result.get(0).getArticleTitle(), article.getTitle());
      assertEquals(result.get(0).getArticleId(), article.getId());
      assertEquals(result.get(0).getOtherUserName(), appliedUser.getNickname());
      assertEquals(result.get(0).getOtherUserId(), appliedUser.getId());
    }
  }

}