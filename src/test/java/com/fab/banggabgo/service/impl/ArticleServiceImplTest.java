package com.fab.banggabgo.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fab.banggabgo.config.security.JwtTokenProvider;
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
        .period(LocalDate.now())
        .price(10000)
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
        .period(LocalDate.now())
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
        .period(LocalDate.now())
        .price(10000)
        .gender("남성")
        .content("글 내용")
        .build();

    given(provider.getUser(anyString()))
        .willReturn("User Email");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(User.builder().id(1L).build()));

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
        .period(LocalDate.now())
        .price(10000)
        .gender("외계인")
        .content("글 내용")
        .build();

    given(provider.getUser(anyString()))
        .willReturn("User Email");

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(User.builder().id(1L).build()));

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
        .period(LocalDate.now())
        .price(10000)
        .gender("외계인")
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
}