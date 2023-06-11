package com.fab.banggabgo.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fab.banggabgo.dto.mycontent.FavoriteArticleDto;
import com.fab.banggabgo.dto.mycontent.MyArticleDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ArticleRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class MyContentServiceImplTest {

  @Mock
  private ArticleRepository articleRepository;
  @InjectMocks
  private MyContentServiceImpl myContentService;

  User stub_user = User.builder()
      .id(1)
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
    Assertions.assertEquals(myContentService.getMyArticle(stub_user),stub_list);
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
    Assertions.assertEquals(myContentService.getMyFavoriteArticle(stub_user),stub_list);

  }
}