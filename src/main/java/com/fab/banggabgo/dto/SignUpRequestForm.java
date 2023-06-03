package com.fab.banggabgo.dto;

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

  private String email;
  private String password;
  private String name;
  public static SignUpRequestDto toDto(SignUpRequestForm form){
    return SignUpRequestDto.builder()
        .email(form.email)
        .password(form.password)
        .name(form.getName())
        .build();
  }
}


