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

  public static PatchMyNicknameDto toDto(PatchMyNicknameForm form){
    return PatchMyNicknameDto.builder()
        .nickname(form.getNickname())
        .build();
  }
}
