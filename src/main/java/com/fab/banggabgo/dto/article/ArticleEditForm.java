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
public class ArticleEditForm {

  @ApiModelProperty(value = "제목", example = "강남구 룸메이트 구해요!")
  private String title;
  @ApiModelProperty(value = "지역", example = "강남구")
  private String region;
  @ApiModelProperty(value = "기간", example = "3개월 ~ 6개월")
  private String period;
  @ApiModelProperty(value = "보증금", example = "20000000")
  private Integer price;
  @ApiModelProperty(value = "구하는 룸메이트 성별", example = "FEMALE")
  private String gender;
  @ApiModelProperty(value = "내용", example = "룸메이트 구해요 강남구 에오 !:)")
  private String content;

  public static ArticleEditDto toDto(ArticleEditForm form) {
    return ArticleEditDto.builder()
        .title(form.getTitle())
        .region(form.getRegion())
        .period(form.getPeriod())
        .price(form.getPrice())
        .gender(form.getGender())
        .content(form.getContent())
        .build();
  }
}


