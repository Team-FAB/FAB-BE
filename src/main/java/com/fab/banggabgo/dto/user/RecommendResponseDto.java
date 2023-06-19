package com.fab.banggabgo.dto.user;

import java.util.List;
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
public class RecommendResponseDto {

  private String mbti;
  private List<RecommendDto> recommendDtoList;
}
