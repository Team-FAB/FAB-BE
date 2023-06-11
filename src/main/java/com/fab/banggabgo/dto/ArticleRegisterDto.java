package com.fab.banggabgo.dto;

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
public class ArticleRegisterDto {

  private String title;
  private String region;
  private String period;
  private Integer price;
  private String gender;
  private String content;
}
