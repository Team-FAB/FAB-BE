package com.fab.banggabgo.dto.apply;

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
public class ApplyIsApplyResultDto {

  boolean isApply;

  public static ApplyIsApplyResultDto toDto(boolean isApply) {
    return ApplyIsApplyResultDto.builder()
        .isApply(isApply)
        .build();
  }
}
