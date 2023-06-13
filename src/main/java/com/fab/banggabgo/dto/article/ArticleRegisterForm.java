package com.fab.banggabgo.dto.article;

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

  private String title;
  private String region;
  private String period;
  private Integer price;
  private String gender;
  private String content;

  public static ArticleRegisterDto toDto(ArticleRegisterForm form) {
    return ArticleRegisterDto.builder()
        .title(form.getTitle())
        .region(form.getRegion())
        .period(form.getPeriod())
        .price(form.getPrice())
        .gender(form.getGender())
        .content(form.getContent())
        .build();
  }
}


