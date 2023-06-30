package com.fab.banggabgo.dto.chat;

import com.fab.banggabgo.entity.chat.Chat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

  private String roomId;
  private String userEmail;
  private String msg;
  private LocalDateTime createDate;

  public static ChatDto toDto(Chat chat) {
    return ChatDto.builder()
        .roomId(chat.getRoomId())
        .userEmail(chat.getUserEmail())
        .msg(chat.getMsg())
        .createDate(chat.getCreateDate())
        .build();
  }
}
