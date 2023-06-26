package com.fab.banggabgo.dto.chatRoom;

import com.fab.banggabgo.dto.user.UserDto;
import com.fab.banggabgo.entity.Mate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateDto {

  private Integer id;
  private String chatRoomId;
  private UserDto user1;
  private UserDto user2;

  public static MateDto toDto(Mate mate) {
    return MateDto.builder()
        .id(mate.getId())
        .chatRoomId(mate.getChatRoomId())
        .user1(UserDto.toDto(mate.getUser1()))
        .user2(UserDto.toDto(mate.getUser2()))
        .build();
  }
}
