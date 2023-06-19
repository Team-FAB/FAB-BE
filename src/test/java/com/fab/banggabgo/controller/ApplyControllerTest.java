package com.fab.banggabgo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fab.banggabgo.dto.apply.ApproveUserForm;
import com.fab.banggabgo.service.impl.ApplyServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

@WebMvcTest(ApplyController.class)
@ExtendWith(MockitoExtension.class)
class ApplyControllerTest {

  @MockBean
  ApplyServiceImpl applyService;

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Nested
  @DisplayName("룸메이트 신청 허가")
  class patchApprove {

    ApproveUserForm form = ApproveUserForm.builder()
        .articleId(1)
        .userId(1)
        .build();

    @Test
    @DisplayName("룸메이트 신청 허가 - 성공")
    @WithMockUser
    void patchApproveSuccess() throws Exception {

      mockMvc.perform(patch("/api/applicant/approve")
              .with(SecurityMockMvcRequestPostProcessors.csrf())
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(form)))
          .andExpect(status().isOk())
          .andDo(print());
    }
    @Test
    @DisplayName("룸메이트 신청 허가 - 비 로그인유저")
    void patchApproveFailNonUser() throws Exception {

      mockMvc.perform(patch("/api/applicant/approve")
              .with(SecurityMockMvcRequestPostProcessors.csrf())
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(form)))
          .andExpect(status().isUnauthorized())
          .andDo(print());
    }
  }
  @Nested
  @DisplayName("룸메이트 신청 거절")
  class patchRefuse {
    @Test
    @DisplayName("룸메이트 신청 거절 - 성공")
    @WithMockUser
    void patchRefuseSuccess() throws Exception {

      mockMvc.perform(patch("/api/applicant/refuse?applyId=1")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(status().isOk())
          .andDo(print());
    }
    @Test
    @DisplayName("룸메이트 신청 거절 - 비 로그인 유저")
    void patchRefuseFailNonUser() throws Exception {

      mockMvc.perform(patch("/api/applicant/refuse?applyId=1")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(status().isUnauthorized())
          .andDo(print());
    }
  }
  @Nested
  @DisplayName("룸메이트 신청 목록 제거")
  class deleteApply {
    @Test
    @DisplayName("룸메이트 신청 목록 제거 - 성공")
    @WithMockUser
    void deleteApplySuccess() throws Exception {

      mockMvc.perform(delete("/api/applicant/1")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(status().isOk())
          .andDo(print());
    }
    @Test
    @DisplayName("룸메이트 신청 목록 제거 - 비 로그인 유저")
    void deleteApplyFailNonUser() throws Exception {

      mockMvc.perform(delete("/api/applicant/1")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(status().isUnauthorized())
          .andDo(print());
    }
  }
}