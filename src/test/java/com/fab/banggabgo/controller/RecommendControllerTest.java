package com.fab.banggabgo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fab.banggabgo.service.RecommendService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RecommendController.class)
@ExtendWith(MockitoExtension.class)
class RecommendControllerTest {

  @MockBean
  private RecommendService recommendService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("프로필 추천 불러오기 성공")
  @WithMockUser
  void getRecommendUsersSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get("/api/users/recommend?size=9")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("프로필 추천 불러오기 실패 : 존재하지 않는 유저")
  void getRecommendUsersFail_USER_IS_NULL() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get("/api/users/recommend?size=9"))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }
}