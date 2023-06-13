package com.fab.banggabgo.controller;

import static com.querydsl.core.types.ExpressionUtils.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fab.banggabgo.dto.mycontent.PatchMyInfoForm;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameForm;
import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.service.MyContentService;
import com.fab.banggabgo.service.impl.MyContentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MyContentController.class)
@ExtendWith(MockitoExtension.class)
class MyContentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MyContentService myContentService;
  @Autowired
  ObjectMapper objectMapper;

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
  @Test
  @WithMockUser
  @DisplayName("닉네임 변경")
  void patchMyNickname() throws Exception {
    PatchMyNicknameForm form=PatchMyNicknameForm.builder()
        .nickname("test")
        .build();
      //given
    mockMvc.perform(patch("/api/my/nickname")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isOk());
      //when
      //then
  }

  @Test
  @DisplayName("닉네임 변경- 로그인 안되어있을경우")
  void patchMyNickname_notAuthenticated() throws Exception {
    PatchMyNicknameForm form=PatchMyNicknameForm.builder()
        .nickname("test")
        .build();
    //given
    mockMvc.perform(patch("/api/my/nickname")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().is4xxClientError());
    //when
    //then
  }
  @Test
  @DisplayName("닉네임 변경- 입력폼이없을경우")
  @WithMockUser
  void patchMyNickname_form() throws Exception {
    //given
    mockMvc.perform(patch("/api/my/nickname")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
    //when
    //then
  }
  @Test
  @DisplayName("내정보 변경- 폼이없는경우")
  @WithMockUser
  void patchMyInfo_without_form() throws Exception {
      //given
    mockMvc.perform(patch("/api/my")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
      //when
      //then
  }
  @Test
  @DisplayName("내정보 변경 - 정상")
  @WithMockUser
  void patchMyInfo() throws Exception {
    PatchMyInfoForm form=PatchMyInfoForm.builder()
        .build();
    //given
    mockMvc.perform(patch("/api/my")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isOk());
    //when
    //then
  }
}