package com.fab.banggabgo.dto.OAuth2;

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

  @ApiModelProperty(value = "code", example = "google은 access_token, kakao는 code")
  String code;

  public static OAuth2SignInRequestDto toDto(OAuth2SignInRequestForm form) {
    return new OAuth2SignInRequestDto(form.code);
  }
}
