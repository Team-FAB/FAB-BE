package com.fab.banggabgo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.dto.article.ArticleEditDto;
import com.fab.banggabgo.dto.article.ArticleInfoDto;
import com.fab.banggabgo.dto.article.ArticlePageDto;
import com.fab.banggabgo.dto.article.ArticleRegisterDto;
import com.fab.banggabgo.entity.Apply;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.LikeArticle;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ApplyRepository;
import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.repository.LikeArticleRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.data.domain.PageImpl;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

  @Mock
  private ArticleRepository articleRepository;

  @Mock
  private LikeArticleRepository likeArticleRepository;

  @Mock
  private ApplyRepository applyRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private ArticleServiceImpl articleService;

  @Test
  @DisplayName("글 등록 성공")
  void postArticleSuccess() {
    //given
    ArticleRegisterDto dto = ArticleRegisterDto.builder()
        .title("글 제목")
        .region("강남구")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1)
        .build();

    //when
    articleService.postArticle(user, dto);

    //then
    verify(articleRepository, times(1)).save(any(Article.class));
  }

  @Test
  @DisplayName("글 등록 실패 : 글 양식 오류")
  void postArticleFail_INVALID_REGISTER() {
    //given
    ArticleRegisterDto dto = ArticleRegisterDto.builder()
        .title("")
        .region("강남구")
        .period("1개월 ~ 3개월")
        .price(0)
        .content("")
        .build();

    User user = User.builder()
        .id(1)
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> articleService.postArticle(user, dto));

    //then
    assertEquals(exception.getMessage(), "잘못된 글 양식입니다.");
  }

  @Test
  @DisplayName("글 등록 실패 : 지역 오류")
  void postArticleFail_INVALID_REGION() {
    //given
    ArticleRegisterDto dto = ArticleRegisterDto.builder()
        .title("글 제목")
        .region("경기")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1)
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> articleService.postArticle(user, dto));

    //then
    assertEquals(exception.getMessage(), "존재하지 않는 지역 입니다.");
  }

  @Test
  @DisplayName("글 가져오기 성공")
  void getArticleSuccess() {
    //given
    Article article = Article.builder().build();
    article.setId(1);
    article.setTitle("글" + 1);
    article.setUser(User.builder()
        .nickname("유저" + 1)
        .gender(Gender.MALE)
        .build());
    article.setContent("글 내용" + 1);
    article.setGender(Gender.MALE);
    article.setCreateDate(LocalDateTime.now());
    article.setRegion(Seoul.DONGJAK);
    article.setPeriod(Period.ONETOTHREE);
    article.setPrice(5000000 + 1);
    article.setRecruiting(false);
    article.setDeleted(false);

    given(articleRepository.findByIdAndIsDeletedFalse(anyInt()))
        .willReturn(Optional.of(article));

    //when
    ArticlePageDto result = articleService.getArticle(1);

    //then
    assertEquals("글1", result.getTitle());
  }

  @Test
  @DisplayName("글 가져오기 실패 : 해당 게시글이 존재하지 않음")
  void getArticleFail_ARTICLE_NOT_EXISTS() {
    //given
    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> articleService.getArticle(1));

    //then
    assertEquals(exception.getMessage(), "존재하지 않는 게시글 입니다.");
  }

  @Test
  @DisplayName("유저가 작성한 글 목록 가져오기 성공")
  void getUserArticlesSuccess() {
    //given
    List<ArticleInfoDto> articleList = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      ArticleInfoDto articleInfoDto = ArticleInfoDto.builder()
          .id(i + 1)
          .title("글 제목" + i)
          .build();

      articleList.add(articleInfoDto);
    }

    given(articleRepository.getUserArticle(any()))
        .willReturn(articleList);

    given(userRepository.findById(anyInt()))
        .willReturn(Optional.ofNullable(User.builder().build()));

    //when
    List<ArticleInfoDto> result = articleService.getUserArticles(1);

    //then
    assertEquals(5, result.size());
  }

  @Test
  @DisplayName("유저가 작성한 글 목록 가져오기 실패 : 유저가 존재하지 않음")
  void getUserArticlesFail_USER_IS_NULL() {
    //given
    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> articleService.getUserArticles(1));

    //then
    assertEquals(exception.getMessage(), "유저 정보를 불러오는데 실패했습니다.");
  }

  @Test
  @DisplayName("글 수정 성공")
  void putArticleSuccess() {
    //given
    ArticleEditDto dto = ArticleEditDto.builder()
        .title("글 제목")
        .region("강남구")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1)
        .build();

    given(articleRepository.findById(anyInt()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user)
            .isDeleted(false)
            .isRecruiting(true)
            .build()));

    //when
    articleService.putArticle(user, 1, dto);

    //then
    verify(articleRepository, times(1)).save(any(Article.class));
  }

  @Test
  @DisplayName("글 수정 실패 : 글 양식 오류")
  void putArticleFail_INVALID_EDIT() {
    //given
    ArticleEditDto dto = ArticleEditDto.builder()
        .title("")
        .region("강남구")
        .period("1개월 ~ 3개월")
        .price(0)
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1)
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> articleService.putArticle(user, 1, dto));

    //then
    assertEquals(exception.getMessage(), "잘못된 글 양식입니다.");
  }

  @Test
  @DisplayName("글 수정 실패 : 게시글 찾을 수 없음")
  void putArticleFail_NOT_FOUND_ARTICLE() {
    //given
    ArticleEditDto dto = ArticleEditDto.builder()
        .title("글 제목")
        .region("강남구")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1)
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> articleService.putArticle(user, 1, dto));

    //then
    assertEquals(exception.getMessage(), "존재하지 않는 게시글 입니다.");
  }

  @Test
  @DisplayName("글 수정 실패 : 이미 삭제된 게시글")
  void putArticleFail_DELETED_ARTICLE() {
    //given
    ArticleEditDto dto = ArticleEditDto.builder()
        .title("글 제목")
        .region("강남구")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1)
        .build();

    given(articleRepository.findById(anyInt()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user)
            .isDeleted(true)
            .build()));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> articleService.putArticle(user, 1, dto));

    //then
    assertEquals(exception.getMessage(), "삭제된 게시글 입니다.");
  }

  @Test
  @DisplayName("글 수정 실패 : 해당 게시글의 작성자만 수정 가능")
  void putArticleFail_INVALID_USER() {
    //given
    ArticleEditDto dto = ArticleEditDto.builder()
        .title("글 제목")
        .region("강남구")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1)
        .build();

    User user2 = User.builder()
        .id(2)
        .build();

    given(articleRepository.findById(anyInt()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user2)
            .isDeleted(false)
            .isRecruiting(true)
            .build()));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> articleService.putArticle(user, 1, dto));

    //then
    assertEquals(exception.getMessage(), "해당 게시글의 작성자가 아닙니다.");
  }

  @Test
  @DisplayName("글 삭제 성공")
  void deleteArticleSuccess() {
    //given
    User user = User.builder()
        .id(1)
        .build();

    given(articleRepository.findById(anyInt()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user)
            .isDeleted(false)
            .build()));

    //when
    articleService.deleteArticle(user, 1);

    //then
    verify(articleRepository, times(1)).save(any(Article.class));
  }

  @Test
  @DisplayName("글 삭제 실패 : 게시글 찾을 수 없음")
  void deleteArticleFail_NOT_FOUND_ARTICLE() {
    //given
    User user = User.builder()
        .id(1)
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> articleService.deleteArticle(user, 1));

    //then
    assertEquals(exception.getMessage(), "존재하지 않는 게시글 입니다.");
  }

  @Test
  @DisplayName("글 삭제 실패 : 이미 삭제된 게시글")
  void deleteArticleFail_DELETED_ARTICLE() {
    //given
    User user = User.builder()
        .id(1)
        .build();

    given(articleRepository.findById(anyInt()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user)
            .isDeleted(true)
            .build()));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> articleService.deleteArticle(user, 1));

    //then
    assertEquals(exception.getMessage(), "삭제된 게시글 입니다.");
  }

  @Test
  @DisplayName("글 삭제 실패 : 해당 게시글 작성자만 삭제 가능")
  void deleteArticleFail_INVALID_USER() {
    //given
    User user = User.builder()
        .id(1)
        .build();

    User user2 = User.builder()
        .id(2)
        .build();

    given(articleRepository.findById(anyInt()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user2)
            .isDeleted(false)
            .build()));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> articleService.deleteArticle(user, 1));

    //then
    assertEquals(exception.getMessage(), "해당 게시글의 작성자가 아닙니다.");
  }

  @Test
  @DisplayName("글 최신순 페이지 불러오기 성공 : 모집중인 글만")
  void getArticleByPageableSuccess_ISRECRUITING_TRUE() {
    //given
    List<Article> articleList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Article article = Article.builder().build();
      article.setId(i);
      article.setTitle("글" + i);
      article.setUser(User.builder()
          .nickname("유저" + i)
          .gender(Gender.MALE)
          .build());
      article.setContent("글 내용" + i);
      article.setCreateDate(LocalDateTime.now());
      article.setRegion(Seoul.DONGJAK);
      article.setPeriod(Period.ONETOTHREE);
      article.setPrice(5000000 + i);
      article.setRecruiting(true);
      article.setDeleted(false);

      articleList.add(article);
    }

    given(articleRepository.getArticle(any(), anyBoolean()))
        .willReturn(new PageImpl<>(articleList));

    //when
    var result = articleService.getArticleByPageable(1, 5, true);

    //then
    assertEquals(5, result.getArticleList().size());
  }

  @Test
  @DisplayName("글 최신순 페이지 불러오기 성공 : 전체 글")
  void getArticleByPageableSuccess_ALL_ARTICLES() {
    //given
    List<Article> articleList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Article article = Article.builder().build();
      article.setId(i);
      article.setTitle("글" + i);
      article.setUser(User.builder()
          .nickname("유저" + i)
          .gender(Gender.MALE)
          .build());
      article.setContent("글 내용" + i);
      article.setCreateDate(LocalDateTime.now());
      article.setRegion(Seoul.DONGJAK);
      article.setPeriod(Period.ONETOTHREE);
      article.setPrice(5000000 + i);
      article.setRecruiting(false);
      article.setDeleted(false);

      articleList.add(article);
    }

    given(articleRepository.getArticle(any(), anyBoolean()))
        .willReturn(new PageImpl<>(articleList));

    //when
    var result = articleService.getArticleByPageable(1, 5, false);

    //then
    assertEquals(5, result.getArticleList().size());
  }

  @Test
  @DisplayName("글 최신순 페이지 불러오기 성공 : 모집중인 글만")
  void getArticleByFilterSuccess_ISRECRUITING_TRUE() {
    //given
    List<Article> articleList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Article article = Article.builder().build();
      article.setId(i);
      article.setTitle("글" + i);
      article.setUser(User.builder()
          .nickname("유저" + i)
          .gender(Gender.MALE)
          .build());
      article.setContent("글 내용" + i);
      article.setCreateDate(LocalDateTime.now());
      article.setRegion(Seoul.DONGJAK);
      article.setPeriod(Period.ONETOTHREE);
      article.setPrice(5000000 + i);
      article.setRecruiting(true);
      article.setDeleted(false);

      articleList.add(article);
    }

    given(articleRepository.getArticleByFilter(any(), anyBoolean(), anyString(), anyString(),
        anyString(), anyString()))
        .willReturn(new PageImpl<>(articleList));

    //when
    var result = articleService.getArticleByFilter(1, 5, true, "서초구", "1개월 ~ 3개월", "1000000", "남성");

    //then
    assertEquals(5, result.getArticleList().size());
  }

  @Test
  @DisplayName("글 최신순 페이지 불러오기 성공 : 전체 글")
  void getArticleByFilterSuccess_ALL_ARTICLES() {
    //given
    List<Article> articleList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Article article = Article.builder().build();
      article.setId(i);
      article.setTitle("글" + i);
      article.setUser(User.builder()
          .nickname("유저" + i)
          .gender(Gender.MALE)
          .build());
      article.setContent("글 내용" + i);
      article.setCreateDate(LocalDateTime.now());
      article.setRegion(Seoul.DONGJAK);
      article.setPeriod(Period.ONETOTHREE);
      article.setPrice(5000000 + i);
      article.setRecruiting(false);
      article.setDeleted(false);

      articleList.add(article);
    }

    given(articleRepository.getArticleByFilter(any(), anyBoolean(), anyString(), anyString(),
        anyString(), anyString()))
        .willReturn(new PageImpl<>(articleList));

    //when
    var result = articleService.getArticleByFilter(1, 5, false, "서초구", "1개월 ~ 3개월", "1000000",
        "남성");

    //then
    assertEquals(5, result.getArticleList().size());
  }

  @Test
  @DisplayName("글 찜 등록 성공")
  void postArticleFavoriteSuccess_POST() {
    //given
    User user = User.builder()
        .id(1)
        .build();

    given(articleRepository.findById(anyInt()))
        .willReturn(Optional.ofNullable(Article.builder().id(1).build()));

    given(likeArticleRepository.existsByUserIdAndArticleId(anyInt(), anyInt()))
        .willReturn(false);

    //when
    articleService.postArticleFavorite(user, 1);

    //then
    verify(likeArticleRepository, times(1)).save(any(LikeArticle.class));
  }

  @Test
  @DisplayName("글 찜 삭제 성공")
  void postArticleFavoriteSuccess_DELETE() {
    //given
    User user = User.builder()
        .id(1)
        .build();

    given(articleRepository.findById(anyInt()))
        .willReturn(Optional.ofNullable(Article.builder().id(1).build()));

    given(likeArticleRepository.existsByUserIdAndArticleId(anyInt(), anyInt()))
        .willReturn(true);

    given(likeArticleRepository.findByUserIdAndArticleId(anyInt(), anyInt()))
        .willReturn(LikeArticle.builder().id(1).build());

    //when
    articleService.postArticleFavorite(user, 1);

    //then
    verify(likeArticleRepository, times(1)).delete(any(LikeArticle.class));
  }

  @Test
  @DisplayName("글 찜 여부 가져오기 성공")
  void getArticleFavoriteSuccess() {
    //given
    User user = User.builder()
        .id(1)
        .build();

    given(likeArticleRepository.existsByUserIdAndArticleId(anyInt(), anyInt()))
        .willReturn(false);

    //when
    boolean result = articleService.getArticleFavorite(user, 1);

    //then
    assertFalse(result);
  }

  @Nested
  @DisplayName("apply - 유저 매칭")
  class ApplyTest {

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

    private Apply apply = Apply.builder()
        .article(article)
        .applicantUser(loginUser)
        .approveStatus(ApproveStatus.WAIT)
        .isApplicantDelete(false)
        .isArticleUserDelete(false)
        .build();

    @Test
    @DisplayName("apply - 처음 신청 성공")
    void applySuccess() {
      given(applyRepository.findByApplicantUserIdAndArticleId(anyInt(),
          anyInt())).willReturn(Optional.empty());
      given(articleRepository.findById(anyInt())).willReturn(Optional.of(article));
      given(applyRepository.save(any())).willReturn(apply);

      var result = articleService.applyUser(loginUser, article.getId());

      verify(articleRepository, times(1)).findById(article.getId());
      verify(applyRepository, times(1)).findByApplicantUserIdAndArticleId(
          loginUser.getId(), article.getId());

      assertEquals(result.getArticleId(), article.getId());
      assertEquals(result.getArticleName(), article.getTitle());
      assertEquals(result.getApproveStatus(), ApproveStatus.WAIT.getValue());
    }

    @Test
    @DisplayName("apply - 이미 신청이 되어있는경우 성공")
    void applyAlreadyApply() {

      given(applyRepository.findByApplicantUserIdAndArticleId(anyInt(),
          anyInt())).willReturn(Optional.of(apply));
      given(applyRepository.save(any())).willReturn(apply);
      var result = articleService.applyUser(loginUser, article.getId());

      verify(applyRepository, times(1)).findByApplicantUserIdAndArticleId(
          loginUser.getId(), article.getId());
      verify(applyRepository, times(1)).save(
          apply);

      assertEquals(result.getArticleId(), article.getId());
      assertEquals(result.getArticleName(), article.getTitle());
      assertEquals(result.getApproveStatus(), ApproveStatus.WAIT.getValue());
    }

    @Test
    @DisplayName("apply - 모집 완료된 글에 신청한 경우")
    void applyIsNotRecruiting() {
      article.setRecruiting(false);

      given(applyRepository.findByApplicantUserIdAndArticleId(anyInt(),
          anyInt())).willReturn(Optional.of(apply));

      CustomException customException = assertThrows(CustomException.class,
          () -> articleService.applyUser(loginUser, article.getId()));

      assertEquals(customException.getErrorCode(), ErrorCode.ALREADY_END_RECRUITING);
    }

    @Test
    @DisplayName("apply - 삭제된 글에 신청한 경우")
    void applyIsDeleted() {
      article.setDeleted(true);

      given(applyRepository.findByApplicantUserIdAndArticleId(anyInt(),
          anyInt())).willReturn(Optional.of(apply));

      CustomException customException = assertThrows(CustomException.class,
          () -> articleService.applyUser(loginUser, article.getId()));

      assertEquals(customException.getErrorCode(), ErrorCode.ARTICLE_DELETED);
    }
  }
}