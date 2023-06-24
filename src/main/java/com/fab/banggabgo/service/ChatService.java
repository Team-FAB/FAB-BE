package com.fab.banggabgo.service;

import com.fab.banggabgo.dto.chat.ChatDto;
import com.fab.banggabgo.dto.chat.RequestChatDto;
import com.fab.banggabgo.entity.chat.Chat;
import java.util.List;

public interface ChatService {

  List<ChatDto> getChatLogs(String roomId);

  ChatDto sendMsg(Chat chat);
  Chat saveMsg(String roomId,RequestChatDto dto);
}
