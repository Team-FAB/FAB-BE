package com.fab.banggabgo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestForm {
  @ApiModelProperty(value = "이메일",example = "test@test.com")
  private String email;
  @ApiModelProperty(value = "비밀번호",example = "MyPassword@123")
  private String password;

  public static SignInRequestDto toDto(SignInRequestForm form) {
    return SignInRequestDto.builder()
        .email(form.getEmail())
        .password(form.getPassword())
        .build();
  }
}
