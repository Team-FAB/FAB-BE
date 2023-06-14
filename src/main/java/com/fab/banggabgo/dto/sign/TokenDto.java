package com.fab.banggabgo.dto.sign;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenDto {

  private String atk; //access token
  private String rtk; //refresh token
}
