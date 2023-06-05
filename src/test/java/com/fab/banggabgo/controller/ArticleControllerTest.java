package com.fab.banggabgo.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fab.banggabgo.dto.ArticleRegisterForm;
import com.fab.banggabgo.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

  @MockBean
  private ArticleService articleService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("글 등록 성공")
  void registerArticleSuccess() throws Exception{
    //given
    ArticleRegisterForm form = ArticleRegisterForm.builder()
        .title("글 제목")
        .region("강남")
        .period(LocalDate.now())
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    //when
    //then
    mockMvc.perform(post("/api/article")
            .header("Authorization", "JWT")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  @DisplayName("글 등록 실패 : 글 양식 오류")
  void registerArticleFail_INVALID_REGISTER() throws Exception{
    //given
    ArticleRegisterForm form = ArticleRegisterForm.builder()
        .title("")
        .region("경기")
        .period(LocalDate.now())
        .price(0)
        .gender("외계인")
        .content("")
        .build();

    doThrow(new RuntimeException("글 등록 양식이 잘못되었습니다."))
        .when(articleService)
        .registerArticle(anyString(), any());

    //when
    MvcResult result = mockMvc.perform(post("/api/article")
        .header("Authorization", "JWT")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isInternalServerError())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    assertThat(responseBody).isEqualTo("글 등록 양식이 잘못되었습니다.");
  }

  @Test
  @DisplayName("글 등록 실패 : 지역 오류")
  void registerArticleFail_INVALID_REGION() throws Exception{
    //given
    ArticleRegisterForm form = ArticleRegisterForm.builder()
        .title("글 제목")
        .region("경기")
        .period(LocalDate.now())
        .price(3000)
        .gender("외계인")
        .content("글 내용")
        .build();

    doThrow(new RuntimeException("해당 지역이 존재하지 않습니다."))
        .when(articleService)
        .registerArticle(anyString(), any());

    //when
    MvcResult result = mockMvc.perform(post("/api/article")
            .header("Authorization", "JWT")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isInternalServerError())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    assertThat(responseBody).isEqualTo("해당 지역이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("글 등록 실패 : 성별 오류")
  void registerArticleFail_INVALID_GENDER() throws Exception{
    //given
    ArticleRegisterForm form = ArticleRegisterForm.builder()
        .title("글 제목")
        .region("강남")
        .period(LocalDate.now())
        .price(3000)
        .gender("외계인")
        .content("글 내용")
        .build();

    doThrow(new RuntimeException("해당 성별이 존재하지 않습니다."))
        .when(articleService)
        .registerArticle(anyString(), any());

    //when
    MvcResult result = mockMvc.perform(post("/api/article")
            .header("Authorization", "JWT")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isInternalServerError())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    assertThat(responseBody).isEqualTo("해당 성별이 존재하지 않습니다.");
  }
}