package com.fab.banggabgo.controller;

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
  }
}