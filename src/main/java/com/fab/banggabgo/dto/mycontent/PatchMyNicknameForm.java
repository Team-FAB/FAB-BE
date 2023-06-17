package com.fab.banggabgo.dto.mycontent;

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
public class PatchMyNicknameForm {
  private String nickname;

  public static PatchMyNicknameRequestDto toDto(PatchMyNicknameForm form){
    return PatchMyNicknameRequestDto.builder()
        .nickname(form.getNickname())
        .build();
  }
}
