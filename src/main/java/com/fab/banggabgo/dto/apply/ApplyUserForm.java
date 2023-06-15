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
public class ApplyUserForm {

  @ApiModelProperty(value = "상대 유저 id", example = "1")
  private Integer appliedUserId;

  @ApiModelProperty(value = "게시판 id", example = "1")
  private Integer articleId;

  public static ApplyUserDto toDto(ApplyUserForm form){
    return ApplyUserDto.builder()
        .appliedUserId(form.getAppliedUserId())
        .articleId(form.getArticleId())
        .build();
  }
}
