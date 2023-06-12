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
public class MyArticleDto {

  private Integer id;
  private String title;

  private String email;
  private String nickname;
  private String content;
  private String gender;
  private LocalDateTime createdDate;
  private LocalDateTime modifiedDate;
  private String region;

  private String period;
  private Integer price;

  private boolean isRecruiting;

  public static MyArticleDto toDto(Article article) {
    return MyArticleDto.builder()
        .id(article.getId())
        .title(article.getTitle())
        .email(article.getUser().getEmail())
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
