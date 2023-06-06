package com.fab.banggabgo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fab.banggabgo.config.security.JwtTokenProvider;
import com.fab.banggabgo.dto.ArticleEditDto;
import com.fab.banggabgo.dto.ArticleRegisterDto;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

  @Mock
  private JwtTokenProvider provider;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ArticleRepository articleRepository;

  @InjectMocks
  private ArticleServiceImpl articleService;

  @Test
  @DisplayName("글 등록 성공")
  void registerArticleSuccess() {
    //given
    ArticleRegisterDto dto = ArticleRegisterDto.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    given(provider.getUser(anyString()))
        .willReturn("User Email");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(User.builder().id(1L).build()));

    //when
    articleService.registerArticle("JWT", dto);

    //then
    verify(articleRepository, times(1)).save(any(Article.class));
  }

  @Test
  @DisplayName("글 등록 실패 : 글 양식 오류")
  void registerArticleFail_INVALID_REGISTER() {
    //given
    ArticleRegisterDto dto = ArticleRegisterDto.builder()
        .title("")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(0)
        .gender("남성")
        .content("")
        .build();

    //when
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> articleService.registerArticle("JWT", dto));

    //then
    assertEquals(exception.getMessage(), "글 등록 양식이 잘못되었습니다.");
  }

  @Test
  @DisplayName("글 등록 실패 : 지역 오류")
  void registerArticleFail_INVALID_REGION() {
    //given
    ArticleRegisterDto dto = ArticleRegisterDto.builder()
        .title("글 제목")
        .region("경기")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    //when
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> articleService.registerArticle("JWT", dto));

    //then
    assertEquals(exception.getMessage(), "해당 지역이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("글 등록 실패 : 성별 오류")
  void registerArticleFail_INVALID_GENDER() {
    //given
    ArticleRegisterDto dto = ArticleRegisterDto.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("외계인")
        .content("글 내용")
        .build();

    //when
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> articleService.registerArticle("JWT", dto));

    //then
    assertEquals(exception.getMessage(), "해당 성별이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("글 등록 실패 : 유저 오류")
  void registerArticleFail_INVALID_USER() {
    //given
    ArticleRegisterDto dto = ArticleRegisterDto.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    given(provider.getUser(anyString()))
        .willReturn("User Email");

    //when
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> articleService.registerArticle("JWT", dto));

    //then
    assertEquals(exception.getMessage(), "존재하지않는 유저");
  }

  @Test
  @DisplayName("글 수정 성공")
  void editArticleSuccess() {
    //given
    ArticleEditDto dto = ArticleEditDto.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1L)
        .build();

    given(provider.getUser(anyString()))
        .willReturn("User Email");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(user));

    given(articleRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user)
            .isDeleted(false)
            .build()));

    //when
    articleService.editArticle("JWT", 1L, dto);

    //then
    verify(articleRepository, times(1)).save(any(Article.class));
  }

  @Test
  @DisplayName("글 수정 실패 : 글 양식 오류")
  void editArticleFail_INVALID_EDIT() {
    //given
    ArticleEditDto dto = ArticleEditDto.builder()
        .title("")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(0)
        .gender("남성")
        .content("글 내용")
        .build();

    //when
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> articleService.editArticle("JWT",1L , dto));

    //then
    assertEquals(exception.getMessage(), "글 수정 양식이 잘못되었습니다.");
  }

  @Test
  @DisplayName("글 수정 실패 : 게시글 찾을 수 없음")
  void editArticleFail_NOT_FOUND_ARTICLE() {
    //given
    ArticleEditDto dto = ArticleEditDto.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1L)
        .build();

    given(provider.getUser(anyString()))
        .willReturn("User Email");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(user));

    //when
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> articleService.editArticle("JWT",1L , dto));

    //then
    assertEquals(exception.getMessage(), "해당 게시글을 찾을 수 없습니다.");
  }

  @Test
  @DisplayName("글 수정 실패 : 이미 삭제된 게시글")
  void editArticleFail_DELETED_ARTICLE() {
    //given
    ArticleEditDto dto = ArticleEditDto.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1L)
        .build();

    given(provider.getUser(anyString()))
        .willReturn("User Email");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(user));

    given(articleRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user)
            .isDeleted(true)
            .build()));

    //when
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> articleService.editArticle("JWT",1L , dto));

    //then
    assertEquals(exception.getMessage(), "삭제된 게시글입니다.");
  }

  @Test
  @DisplayName("글 수정 실패 : 해당 게시글의 작성자만 수정 가능")
  void editArticleFail_INVALID_USER() {
    //given
    ArticleEditDto dto = ArticleEditDto.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    User user = User.builder()
        .id(1L)
        .build();

    User user2 = User.builder()
        .id(2L)
        .build();

    given(provider.getUser(anyString()))
        .willReturn("User Email");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(user));

    given(articleRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user2)
            .isDeleted(false)
            .build()));

    //when
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> articleService.editArticle("JWT",1L , dto));

    //then
    assertEquals(exception.getMessage(), "해당 게시글의 작성자가 아닙니다.");
  }

  @Test
  @DisplayName("글 삭제 성공")
  void deleteArticleSuccess() {
    //given
    User user = User.builder()
        .id(1L)
        .build();

    given(provider.getUser(anyString()))
        .willReturn("User Email");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(user));

    given(articleRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user)
            .isDeleted(false)
            .build()));

    //when
    articleService.deleteArticle("JWT", 1L);

    //then
    verify(articleRepository, times(1)).save(any(Article.class));
  }

  @Test
  @DisplayName("글 삭제 실패 : 게시글 찾을 수 없음")
  void deleteArticleFail_NOT_FOUND_ARTICLE() {
    //given
    User user = User.builder()
        .id(1L)
        .build();

    given(provider.getUser(anyString()))
        .willReturn("User Email");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(user));

    //when
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> articleService.deleteArticle("JWT",1L));

    //then
    assertEquals(exception.getMessage(), "해당 게시글을 찾을 수 없습니다.");
  }

  @Test
  @DisplayName("글 삭제 실패 : 이미 삭제된 게시글")
  void deleteArticleFail_DELETED_ARTICLE() {
    //given
    User user = User.builder()
        .id(1L)
        .build();

    given(provider.getUser(anyString()))
        .willReturn("User Email");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(user));

    given(articleRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user)
            .isDeleted(true)
            .build()));

    //when
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> articleService.deleteArticle("JWT",1L));

    //then
    assertEquals(exception.getMessage(), "이미 삭제된 게시글입니다.");
  }

  @Test
  @DisplayName("글 삭제 실패 : 해당 게시글 작성자만 삭제 가능")
  void deleteArticleFail_INVALID_USER() {
    //given
    User user = User.builder()
        .id(1L)
        .build();

    User user2 = User.builder()
        .id(2L)
        .build();

    given(provider.getUser(anyString()))
        .willReturn("User Email");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(user));

    given(articleRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Article.builder()
            .user(user2)
            .isDeleted(false)
            .build()));

    //when
    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> articleService.deleteArticle("JWT",1L));

    //then
    assertEquals(exception.getMessage(), "해당 게시글의 작성자가 아닙니다.");
  }
}