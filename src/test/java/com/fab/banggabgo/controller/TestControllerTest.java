package com.fab.banggabgo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = TestController.class)
@ExtendWith(MockitoExtension.class)
class TestControllerTest {

  @Autowired
  private MockMvc mvc;

  @DisplayName("로그인 되어있는경우 (권한 있음) : 200")
  @Test
  @WithMockUser
  void authenticated() throws Exception {
    mvc.perform(get("/api/v2/test"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @DisplayName("로그인 되어있지 않은 경우 권한없음 : 403")
  @Test
  void not_authenticated() throws Exception {
    mvc.perform(get("/api/v2/test"))
        .andExpect(status().is4xxClientError())
        .andDo(print());
  }

}
