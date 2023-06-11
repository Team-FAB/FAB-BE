package com.fab.banggabgo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.service.MyContentService;
import com.fab.banggabgo.service.impl.MyContentServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MyContentController.class)
@ExtendWith(MockitoExtension.class)
class MyContentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MyContentService myContentService;

  @Test
  @WithMockUser
  @DisplayName("내 글 불러오기")
  void getMyArticle() throws Exception {
    mockMvc.perform(get("/api/my/articles"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  @DisplayName("좋아요한 글 불러오기")
  void getMyFavoriteArticle() throws Exception {
    mockMvc.perform(get("/api/my/favorites"))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  @DisplayName("내 정보 불러오기")
  void getMyInfo() throws Exception {
    mockMvc.perform(get("/api/my"))
        .andExpect(status().isOk());
  }
}