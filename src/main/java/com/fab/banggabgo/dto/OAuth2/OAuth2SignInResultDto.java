package com.fab.banggabgo.dto.OAuth2;

import com.fab.banggabgo.dto.sign.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2SignInResultDto {

  String email;
  String nickName;
  TokenDto token;
}
