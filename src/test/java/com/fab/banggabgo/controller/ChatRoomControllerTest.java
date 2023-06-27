package com.fab.banggabgo.controller;

import com.fab.banggabgo.dto.chatRoom.MateDto;
import com.fab.banggabgo.dto.user.UserDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.impl.ChatRoomServiceImpl;
import com.fab.banggabgo.type.ActivityTime;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.MatchStatus;
import com.fab.banggabgo.type.Mbti;
import com.fab.banggabgo.type.Seoul;
import com.fab.banggabgo.type.UserRole;
import com.fab.banggabgo.type.UserType;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ChatRoomController.class)
@ExtendWith(MockitoExtension.class)
class ChatRoomControllerTest {

  @MockBean
  private ChatRoomServiceImpl chatRoomService;

  @Autowired
  private MockMvc mockMvc;

  @Nested
  @DisplayName("채팅 매칭")
  class postChatApplyId {

    private final User loginUser = User.builder()
        .id(1)
        .email("test@email.com")
        .password("abcd1234")
        .nickname("테스트")
        .userType(UserType.NORMAL)
        .image("http://image_uri")
        .matchStatus(MatchStatus.ACTIVITY)
        .isSmoker(true)
        .activityTime(ActivityTime.MORNING)
        .gender(Gender.MALE)
        .region(Seoul.DOBONG)
        .mbti(Mbti.ENFP)
        .minAge(20)
        .maxAge(30)
        .myAge(25)
        .tag(new HashSet<>())
        .detail("abcdef")
        .roles(List.of(UserRole.USER_ROLE))
        .build();

    private final User appliedUser = User.builder()
        .id(2)
        .email("test2@email.com")
        .password("abcdf12345")
        .nickname("테스트2")
        .userType(UserType.NORMAL)
        .image("http://image_uri2")
        .matchStatus(MatchStatus.ACTIVITY)
        .isSmoker(true)
        .activityTime(ActivityTime.MORNING)
        .gender(Gender.MALE)
        .region(Seoul.DOBONG)
        .mbti(Mbti.ENFP)
        .minAge(22)
        .maxAge(28)
        .myAge(26)
        .tag(new HashSet<>())
        .detail("abcdeffdaa")
        .roles(List.of(UserRole.USER_ROLE))
        .build();

    MateDto mateDto = MateDto.builder()
        .id(1)
        .chatRoomId("UUID")
        .user1(UserDto.toDto(loginUser))
        .user2(UserDto.toDto(appliedUser))
        .build();

    @Test
    @DisplayName("성공")
    @WithMockUser
    void successPostChatApplyId() throws Exception {
      BDDMockito.given(
              chatRoomService.createChatRoom(ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
          .willReturn(mateDto);

      mockMvc.perform(MockMvcRequestBuilders.post("/api/chat/1")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(MockMvcResultMatchers.status().isOk())
          .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("실패 - 유저없음")
    void failValidUserPostChatApplyId() throws Exception {
      mockMvc.perform(MockMvcRequestBuilders.post("/api/chat/3")
              .with(SecurityMockMvcRequestPostProcessors.csrf()))
          .andExpect(MockMvcResultMatchers.status().isUnauthorized())
          .andDo(MockMvcResultHandlers.print());
    }
  }
}