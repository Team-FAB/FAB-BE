package com.fab.banggabgo.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.dto.article.ArticleEditForm;
import com.fab.banggabgo.dto.article.ArticleRegisterForm;
import com.fab.banggabgo.service.ArticleService;
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

@WebMvcTest(ArticleController.class)
@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

  @MockBean
  private ArticleService articleService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("글 등록 성공")
  @WithMockUser
  void postArticleSuccess() throws Exception {
    //given
    ArticleRegisterForm form = ArticleRegisterForm.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    //when
    //then
    mockMvc.perform(post("/api/articles")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  @DisplayName("글 등록 실패 : 글 양식 오류")
  @WithMockUser
  void postArticleFail_INVALID_REGISTER() throws Exception {
    //given
    ArticleRegisterForm form = ArticleRegisterForm.builder()
        .title("")
        .region("경기")
        .period("1개월 ~ 3개월")
        .price(0)
        .gender("외계인")
        .content("")
        .build();

    doThrow(new CustomException(ErrorCode.INVALID_ARTICLE))
        .when(articleService)
        .postArticle(any(), any());

    //when
    MvcResult result = mockMvc.perform(post("/api/articles")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("INVALID_ARTICLE");
  }

  @Test
  @DisplayName("글 등록 실패 : 지역 오류")
  @WithMockUser
  void postArticleFail_INVALID_REGION() throws Exception {
    //given
    ArticleRegisterForm form = ArticleRegisterForm.builder()
        .title("글 제목")
        .region("경기")
        .period("1개월 ~ 3개월")
        .price(3000)
        .gender("외계인")
        .content("글 내용")
        .build();

    doThrow(new CustomException(ErrorCode.REGION_NOT_EXISTS))
        .when(articleService)
        .postArticle(any(), any());

    //when
    MvcResult result = mockMvc.perform(post("/api/articles")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("REGION_NOT_EXISTS");
  }

  @Test
  @DisplayName("글 등록 실패 : 성별 오류")
  @WithMockUser
  void postArticleFail_INVALID_GENDER() throws Exception {
    //given
    ArticleRegisterForm form = ArticleRegisterForm.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000)
        .gender("외계인")
        .content("글 내용")
        .build();

    doThrow(new CustomException(ErrorCode.GENDER_NOT_EXISTS))
        .when(articleService)
        .postArticle(any(), any());

    //when
    MvcResult result = mockMvc.perform(post("/api/articles")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("GENDER_NOT_EXISTS");
  }

  @Test
  @DisplayName("글 가져오기 성공")
  @WithMockUser
  void getArticleSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get("/api/articles/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("글 가져오기 실패 : 해당 게시글이 존재하지 않음")
  @WithMockUser
  void getArticleFail_ARTICLE_NOT_EXISTS() throws Exception {
    //given
    doThrow(new CustomException(ErrorCode.ARTICLE_NOT_EXISTS))
        .when(articleService)
        .getArticle(anyInt());

    //when
    MvcResult result = mockMvc.perform(get("/api/articles/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("ARTICLE_NOT_EXISTS");
  }

  @Test
  @DisplayName("글 수정 성공")
  @WithMockUser
  void putArticleSuccess() throws Exception {
    //given
    ArticleEditForm form = ArticleEditForm.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    //when
    //then
    mockMvc.perform(put("/api/articles/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .header("Authorization", "JWT")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  @DisplayName("글 수정 실패 : 글 양식 오류")
  @WithMockUser
  void putArticleFail_INVALID_EDIT() throws Exception {
    //given
    ArticleEditForm form = ArticleEditForm.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(0)
        .gender("남성")
        .content("글 내용")
        .build();

    doThrow(new CustomException(ErrorCode.INVALID_ARTICLE))
        .when(articleService)
        .putArticle(any(), anyInt(), any());

    //when
    MvcResult result = mockMvc.perform(put("/api/articles/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("INVALID_ARTICLE");
  }

  @Test
  @DisplayName("글 수정 실패 : 글을 찾을 수 없음")
  @WithMockUser
  void putArticleFail_NOT_FOUND_ARTICLE() throws Exception {
    //given
    ArticleEditForm form = ArticleEditForm.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    doThrow(new CustomException(ErrorCode.ARTICLE_NOT_EXISTS))
        .when(articleService)
        .putArticle(any(), anyInt(), any());

    //when
    MvcResult result = mockMvc.perform(put("/api/articles/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("ARTICLE_NOT_EXISTS");
  }

  @Test
  @DisplayName("글 수정 실패 : 삭제된 게시글은 수정 불가")
  @WithMockUser
  void putArticleFail_DELETED_ARTICLE() throws Exception {
    //given
    ArticleEditForm form = ArticleEditForm.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    doThrow(new CustomException(ErrorCode.ARTICLE_DELETED))
        .when(articleService)
        .putArticle(any(), anyInt(), any());

    //when
    MvcResult result = mockMvc.perform(put("/api/articles/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("ARTICLE_DELETED");
  }

  @Test
  @DisplayName("글 수정 실패 : 해당 게시글의 작성자만 수정 가능")
  @WithMockUser
  void putArticleFail_INVALID_USER() throws Exception {
    //given
    ArticleEditForm form = ArticleEditForm.builder()
        .title("글 제목")
        .region("강남")
        .period("1개월 ~ 3개월")
        .price(3000000)
        .gender("남성")
        .content("글 내용")
        .build();

    doThrow(new CustomException(ErrorCode.USER_NOT_MATCHED))
        .when(articleService)
        .putArticle(any(), anyInt(), any());

    //when
    MvcResult result = mockMvc.perform(put("/api/articles/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("USER_NOT_MATCHED");
  }

  @Test
  @DisplayName("글 삭제 성공")
  @WithMockUser
  void deleteArticleSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(delete("/api/articles/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .header("Authorization", "JWT"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("글 삭제 실패 : 게시글 찾을 수 없음")
  @WithMockUser
  void deleteArticleFail_NOT_FOUND_ARTICLE() throws Exception {
    doThrow(new CustomException(ErrorCode.USER_NOT_MATCHED))
        .when(articleService)
        .deleteArticle(any(), anyInt());

    //when
    MvcResult result = mockMvc.perform(delete("/api/articles/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("USER_NOT_MATCHED");
  }

  @Test
  @DisplayName("글 삭제 실패 : 이미 삭제된 게시글")
  @WithMockUser
  void deleteArticleFail_DELETED_ARTICLE() throws Exception {
    doThrow(new CustomException(ErrorCode.ARTICLE_DELETED))
        .when(articleService)
        .deleteArticle(any(), anyInt());

    //when
    MvcResult result = mockMvc.perform(delete("/api/articles/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("ARTICLE_DELETED");
  }

  @Test
  @DisplayName("글 삭제 실패 : 해당 게시글의 작성자만 삭제 가능")
  @WithMockUser
  void deleteArticleFail_INVALID_USER() throws Exception {
    doThrow(new CustomException(ErrorCode.USER_NOT_MATCHED))
        .when(articleService)
        .deleteArticle(any(), anyInt());

    //when
    MvcResult result = mockMvc.perform(delete("/api/articles/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("USER_NOT_MATCHED");
  }

  @Test
  @DisplayName("글 최신순 페이지 불러오기 성공")
  @WithMockUser
  void getArticleByPageableSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get("/api/articles?page=1&size=10&isRecruiting=true")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("글 검색 결과 최신순 페이지 불러오기 성공")
  @WithMockUser
  void getArticleByFilterSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get("/api/articles/filter?page=1&size=10&isRecruiting=true&region=서초구&period=1개월~3개월&price=1000000&gender=남성")
            .with(SecurityMockMvcRequestPostProcessors.csrf())
        )
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("게시글 총 개수 가져오기")
  @WithMockUser
  void getArticleTotalCntSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get("/api/articles/total")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("글 찜 등록 및 삭제 성공")
  @WithMockUser
  void postArticleFavoriteSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(post("/api/articles/favorites/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @Test
  @DisplayName("글 찜 등록 및 삭제 실패 : 게시글이 존재하지 않음")
  @WithMockUser
  void postArticleFavoriteFail_ARTICLE_NOT_EXISTS() throws Exception {
    //given
    doThrow(new CustomException(ErrorCode.ARTICLE_NOT_EXISTS))
        .when(articleService)
        .postArticleFavorite(any(), anyInt());

    //when
    MvcResult result = mockMvc.perform(post("/api/articles/favorites/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isBadRequest())
        .andReturn();

    //then
    String responseBody = result.getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String errorCode = responseJson.get("code").asText();
    assertThat(errorCode).isEqualTo("ARTICLE_NOT_EXISTS");
  }

  @Test
  @DisplayName("글 찜 했는지 여부 가져오기 성공")
  @WithMockUser
  void getArticleFavoriteSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get("/api/articles/favorites/1")
            .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }
}