package com.fab.banggabgo.dto;

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
  private String email;
  private String password;

  public static SignInRequestDto toDto(SignInRequestForm form) {
    return SignInRequestDto.builder()
        .email(form.getEmail())
        .password(form.getPassword())
        .build();
  }
}
