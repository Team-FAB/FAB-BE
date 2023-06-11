package com.fab.banggabgo.dto.mycontent;

import com.fab.banggabgo.entity.Article;
import java.time.LocalDateTime;
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
public class FavoriteArticleDto {
  private Integer id;
  private String title;
  private String content;
  private LocalDateTime createDate;
  private LocalDateTime modifiedDate;
  private String region;

  private String period;
  private Integer price;

  private boolean isRecruiting;

  public static FavoriteArticleDto toDto(Article article) {
    return FavoriteArticleDto.builder()
        .id(article.getId())
        .title(article.getTitle())
        .content(article.getContent())
        .createDate(article.getCreateDate())
        .modifiedDate(article.getLastModifiedDate())
        .region(article.getRegion().getValue())
        .period(article.getPeriod().getValue())
        .isRecruiting(article.isRecruiting())
        .build();
  }
}
