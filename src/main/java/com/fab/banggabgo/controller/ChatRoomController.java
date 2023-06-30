package com.fab.banggabgo.controller;

import com.fab.banggabgo.dto.chatRoom.ChatRoomPostResponseDto;
import com.fab.banggabgo.dto.chatRoom.GetMateListResponseDto;
import com.fab.banggabgo.dto.chatRoom.MateDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.service.impl.ChatRoomServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = {"Chatroom Controller 채팅방 API"})
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

  private final ChatRoomServiceImpl chatRoomService;
  @ApiOperation(
      value = "채팅방 생성",
      notes = "생성된 채팅방 uuid 를 반환합니다."
  )
  @PostMapping("/{applyId}")
  public ResponseEntity<ChatRoomPostResponseDto> postChat(@AuthenticationPrincipal User user,
      @PathVariable Integer applyId) {
    MateDto mateDto = chatRoomService.createChatRoom(user, applyId);
    return ResponseEntity.ok(ChatRoomPostResponseDto.builder()
        .roomId(mateDto.getChatRoomId())
        .build());
  }
  @ApiOperation(
      value = "유저 채팅방 목록 불러오기",
      notes = "로그인 정보를 바탕으로 해당 유저가 사용할수있는 채팅방 리스트를 가져옵니다. "
  )
  @GetMapping("/list")
  public ResponseEntity<List<GetMateListResponseDto>> getChatList(@AuthenticationPrincipal User user){
    List<MateDto> mateDto = chatRoomService.getChatList(user);
    return ResponseEntity.ok(mateDto.stream().map((mate) -> GetMateListResponseDto.toDto(mate, user)).collect(
        Collectors.toList()));
  }
}
