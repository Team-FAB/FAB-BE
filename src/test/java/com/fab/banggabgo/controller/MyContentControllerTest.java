package com.fab.banggabgo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fab.banggabgo.dto.mycontent.PatchMyInfoForm;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameForm;
import com.fab.banggabgo.service.MyContentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    PatchMyNicknameForm form = PatchMyNicknameForm.builder()
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
    PatchMyNicknameForm form = PatchMyNicknameForm.builder()
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
    PatchMyInfoForm form = PatchMyInfoForm.builder()
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

  @Nested
  @DisplayName("내가받은 신청자리스트")
  class applicantList {

    @Test
    @DisplayName("신청자리스트 - 성공")
    @WithMockUser
    void successGetApplicantList() throws Exception {
      mockMvc.perform(get("/api/my/from-applicants?page=1&size=4")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("신청자리스트 - 성공 페이지 누락")
    @WithMockUser
    void successLostPageGetApplicantList() throws Exception {
      mockMvc.perform(get("/api/my/from-applicants?size=4")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("신청자리스트 - 성공 사이즈 누락")
    @WithMockUser
    void successLostSizeGetApplicantList() throws Exception {
      mockMvc.perform(get("/api/my/from-applicants?page=1")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("신청자리스트 - 실패 계정 누락")
    void FailNonAuthGetApplicantList() throws Exception {
      mockMvc.perform(get("/api/my/from-applicants?page=1&size=4")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(status().isUnauthorized());
    }
  }

  @Nested
  @DisplayName("내가신청한 신청자리스트")
  class toApplicantList {

    @Test
    @DisplayName("신청한리스트 - 성공")
    @WithMockUser
    void successGetApplicantList() throws Exception {
      mockMvc.perform(get("/api/my/to-applicants?page=1&size=4")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("신청한리스트 - 성공 페이지 누락")
    @WithMockUser
    void successLostPageGetApplicantList() throws Exception {
      mockMvc.perform(get("/api/my/to-applicants?size=4")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("신청한리스트 - 성공 사이즈 누락")
    @WithMockUser
    void successLostSizeGetApplicantList() throws Exception {
      mockMvc.perform(get("/api/my/to-applicants?page=1")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("신청한리스트 - 실패 계정 누락")
    void FailNonAuthGetApplicantList() throws Exception {
      mockMvc.perform(get("/api/my/to-applicants?page=1&size=4")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("이미지 변경 테스트")
    @WithMockUser
    void postMyImg() throws Exception {
      Path imagePath = Paths.get("src/test/resources/wierd.png");
      byte[] imageBytes = Files.readAllBytes(imagePath);
      var image = new MockMultipartFile("image", "wierd.png", MediaType.IMAGE_PNG_VALUE,
          imageBytes);
      mockMvc.perform(MockMvcRequestBuilders.multipart("/api/my/image")
              .file(image)
              .with(SecurityMockMvcRequestPostProcessors.csrf())
          )
          .andExpect(status().isOk());
    }
  }
}