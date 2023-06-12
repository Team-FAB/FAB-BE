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
public class OAuth2SignInRequestForm {

  @ApiModelProperty(value = "access_token", example = "access_token_value")
  String accessToken;

  public static OAuth2SignInRequestDto toDto(OAuth2SignInRequestForm form) {
    return new OAuth2SignInRequestDto(form.accessToken);
  }
}
