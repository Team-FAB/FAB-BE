package com.fab.banggabgo.dto.article;

import com.fab.banggabgo.entity.Article;
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
public class ArticleInfoDto {

  private Integer id;
  private String title;

  public static ArticleInfoDto toDto(Article article) {
    return ArticleInfoDto.builder()
        .id(article.getId())
        .title(article.getTitle())
        .build();
  }
}
