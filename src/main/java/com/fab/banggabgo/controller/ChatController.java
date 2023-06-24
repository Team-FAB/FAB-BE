package com.fab.banggabgo.controller;

import com.fab.banggabgo.dto.chat.ChatDto;
import com.fab.banggabgo.dto.chat.RequestChatDto;
import com.fab.banggabgo.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

  private final ChatService chatService;

  @MessageMapping("/chat.history.{roomId}")
  public List<ChatDto> getChatLog(@DestinationVariable String roomId) {
    return chatService.getChatLogs(roomId);
  }

  @MessageMapping(value = "/chat")
  public String sendChat(String msg) {
    return "RECEIVED : " + msg;
  }

  @MessageMapping("/chat.{roomId}")
  public ChatDto sendRoomChat(@DestinationVariable String roomId, @Payload RequestChatDto chat) {
    log.warn("use chatroom method");
    var saved_chat = chatService.saveMsg(roomId, chat);
    return chatService.sendMsg(saved_chat);
  }
}