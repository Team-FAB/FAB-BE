package com.fab.banggabgo.dto.chatRoom;

import com.fab.banggabgo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMateListResponseDto {

  private String roomId;
  private String userNickname;

  public static GetMateListResponseDto toDto(MateDto mateDto, User user) {
    return GetMateListResponseDto.builder()
        .roomId(mateDto.getChatRoomId())
        .userNickname(
            mateDto.getUser1().getId().equals(user.getId()) ? mateDto.getUser2().getNickname()
                : mateDto.getUser1().getNickname())
        .build();
  }
}
