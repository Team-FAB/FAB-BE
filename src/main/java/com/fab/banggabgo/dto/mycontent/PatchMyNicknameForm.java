package com.fab.banggabgo.dto.mycontent;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
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
  @ApiModelProperty(value = "닉네임", example = "수줍은 라이언")
  private String nickname;

  public static PatchMyNicknameRequestDto toDto(PatchMyNicknameForm form){
    return PatchMyNicknameRequestDto.builder()
        .nickname(form.getNickname())
        .build();
  }
}
