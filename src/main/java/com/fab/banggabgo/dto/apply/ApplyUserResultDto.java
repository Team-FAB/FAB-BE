package com.fab.banggabgo.dto.apply;

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
public class ApplyUserResultDto {
  @ApiModelProperty(value = "이메일", example = "test@test.com")
  private String email;
  @ApiModelProperty(value = "비밀번호", example = "MyPassword@123")
  private String password;
}
