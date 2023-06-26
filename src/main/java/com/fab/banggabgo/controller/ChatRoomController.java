package com.fab.banggabgo.controller;

import com.fab.banggabgo.dto.chatRoom.ChatRoomPostResponseDto;
import com.fab.banggabgo.dto.chatRoom.MateDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.impl.ChatRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

  private final ChatRoomServiceImpl chatRoomService;

  @PostMapping("/{applyId}")
  public ResponseEntity<ChatRoomPostResponseDto> postChat(@AuthenticationPrincipal User user,
      @PathVariable Integer applyId) {
    MateDto mateDto = chatRoomService.createChatRoom(user, applyId);
    return ResponseEntity.ok(ChatRoomPostResponseDto.builder()
        .roomId(mateDto.getChatRoomId())
        .build());
  }
}
