package com.fab.banggabgo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.config.security.JwtTokenProvider;
import com.fab.banggabgo.dto.ArticleEditDto;
import com.fab.banggabgo.dto.ArticleRegisterDto;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.repository.impl.ArticleRepositoryImpl;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.Period;
import com.fab.banggabgo.type.Seoul;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
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
  private ArticleRepositoryImpl articleRepositoryImpl;

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
        .gender("남성")
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
        .gender("남성")
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
        .gender("남성")
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
  @DisplayName("글 등록 실패 : 성별 오류")
  void postArticleFail_INVALID_GENDER() {
    //given
    ArticleRegisterDto dto = ArticleRegisterDto.builder()
        .title("글 제목")
        .region("강남구")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("외계인")
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1)
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> articleService.postArticle(user, dto));

    //then
    assertEquals(exception.getMessage(), "존재하지 않는 성별 입니다.");
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
        .gender("남성")
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1)
        .build();

    given(articleRepository.findById(anyInt()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user)
            .isDeleted(false)
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
        .gender("남성")
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
        .gender("남성")
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
        .gender("남성")
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
        .gender("남성")
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
          .build());
      article.setContent("글 내용" + i);
      article.setGender(Gender.MALE);
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
    assertEquals(5, result.size());
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
          .build());
      article.setContent("글 내용" + i);
      article.setGender(Gender.MALE);
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
    assertEquals(5, result.size());
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
          .build());
      article.setContent("글 내용" + i);
      article.setGender(Gender.MALE);
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
    assertEquals(5, result.size());
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
          .build());
      article.setContent("글 내용" + i);
      article.setGender(Gender.MALE);
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
    var result = articleService.getArticleByFilter(1, 5, false, "서초구", "1개월 ~ 3개월", "1000000", "남성");

    //then
    assertEquals(5, result.size());
  }

  @Test
  @DisplayName("글 삭제 성공")
  void getArticleTotalCntSuccess() {
    //given
    given(articleRepository.getArticleTotalCnt())
        .willReturn(5);

    //when
    Integer result = articleService.getArticleTotalCnt();

    //then
    assertEquals(5, result);
  }
}