package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.dto.chat.ChatDto;
import com.fab.banggabgo.dto.chat.RequestChatDto;
import com.fab.banggabgo.entity.chat.Chat;
import com.fab.banggabgo.repository.mongo.ChatRepository;
import com.fab.banggabgo.service.ChatService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
  private final ChatRepository chatRepository;
  @Override
  public ChatDto sendMsg(Chat chat){
    return ChatDto.toDto(chat);
  }
  @Override
  public List<ChatDto> getChatLogs(String roomId){
    return chatRepository.findByRoomId(roomId)
        .stream().map(ChatDto::toDto).collect(Collectors.toList());
  }
  @Override
  public Chat saveMsg(String roomId,RequestChatDto dto){
    var chat =Chat.builder()
        .roomId(roomId)
        .userEmail(dto.getUsername())
        .msg(dto.getMsg())
        .build();
    return chatRepository.save(chat);
  }
}
