package com.fab.banggabgo.dto.article;

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
public class ArticleRegisterForm {
  @ApiModelProperty(value = "제목", example = "마포구 룸메이트 구해요!")
  private String title;
  @ApiModelProperty(value = "지역", example = "마포구")
  private String region;
  @ApiModelProperty(value = "기간", example = "3개월 ~ 6개월")
  private String period;
  @ApiModelProperty(value = "보증금", example = "10000000")
  private Integer price;
  @ApiModelProperty(value = "내용", example = "룸메이트 구해요 :)")
  private String content;

  public static ArticleRegisterDto toDto(ArticleRegisterForm form) {
    return ArticleRegisterDto.builder()
        .title(form.getTitle())
        .region(form.getRegion())
        .period(form.getPeriod())
        .price(form.getPrice())
        .content(form.getContent())
        .build();
  }
}


