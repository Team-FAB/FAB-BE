package com.fab.banggabgo.dto;

import io.swagger.annotations.ApiModelProperty;
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
public class SignUpRequestForm {

  @ApiModelProperty(value = "이메일", example = "test@test.com")

  private String email;
  @ApiModelProperty(value = "비밀번호", example = "MyPassword@123")

  private String password;
  @ApiModelProperty(value = "닉네임", example = "수줍은라이언")

  private String nickname;

  public static SignUpRequestDto toDto(SignUpRequestForm form) {
    return SignUpRequestDto.builder()
        .email(form.email)
        .password(form.password)
        .nickname(form.getNickname())
        .build();
  }
}


