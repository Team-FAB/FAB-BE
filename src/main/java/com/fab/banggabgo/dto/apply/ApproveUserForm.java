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
public class ApproveUserForm {

  @ApiModelProperty(value = "상대 유저 id", example = "1")
  private Integer userId;
  @ApiModelProperty(value = "게시글 id", example = "1")
  private Integer articleId;

  public static ApproveUserDto toDto(ApproveUserForm form) {
    return ApproveUserDto.builder()
        .userId(form.userId)
        .articleId(form.articleId)
        .build();
  }
}
