package com.fab.banggabgo.dto.apply;

import com.fab.banggabgo.entity.Apply;
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

  String approveStatus;
  String articleName;
  Integer articleId;

  public static ApplyUserResultDto toDto(Apply apply) {
    return ApplyUserResultDto.builder()
        .approveStatus(apply.getApproveStatus().getValue())
        .articleName(apply.getArticle().getTitle())
        .articleId(apply.getArticle().getId())
        .build();
  }
}
