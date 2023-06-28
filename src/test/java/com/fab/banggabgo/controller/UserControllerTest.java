package com.fab.banggabgo.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @MockBean
  private UserService userService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("프로필 추천 불러오기 성공")
  @WithMockUser
  void getRecommendUsersSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get("/api/users/recommendation?size=9")
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

  @Test
  @DisplayName("프로필 정보 불러오기 성공")
  @WithMockUser
  void getUserProfileSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get("/api/users/profile/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("프로필 정보 불러오기 실패 : 해당 유저가 존재하지 않음")
  @WithMockUser
  void getUserProfileFail_USER_IS_NULL() throws Exception {
    //given
    doThrow(new CustomException(ErrorCode.USER_IS_NULL))
        .when(userService)
        .getUserProfile(anyInt());

    //when
    MvcResult result = mockMvc.perform(get("/api/users/profile/1"))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("USER_IS_NULL");
  }
}