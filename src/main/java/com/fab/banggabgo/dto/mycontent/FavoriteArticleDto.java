package com.fab.banggabgo.dto.mycontent;

import com.fab.banggabgo.entity.Article;
import java.time.LocalDateTime;
import java.util.Set;
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
  private String email;
  private String image;
  private String nickname;
  private String content;
  private String gender;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;
  private String region;
  private String period;
  private Integer price;
  private boolean isRecruiting;

  public static FavoriteArticleDto toDto(Article article) {
    return FavoriteArticleDto.builder()
        .id(article.getId())
        .title(article.getTitle())
        .email(article.getUser().getEmail())
        .image(article.getUser().getImage())
        .nickname(article.getUser().getNickname())
        .gender(article.getGender().getValue())
        .content(article.getContent())
        .createdDate(article.getCreateDate())
        .modifiedDate(article.getLastModifiedDate())
        .region(article.getRegion().getValue())
        .period(article.getPeriod().getValue())
        .price(article.getPrice())
        .isRecruiting(article.isRecruiting())
        .build();
  }
}
