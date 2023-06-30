package com.fab.banggabgo.dto.article;

import com.fab.banggabgo.entity.Article;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePageDto {

  private Integer id;
  private String title;
  private String email;
  private String image;
  private String nickname;
  private String content;
  private String gender;
  private LocalDateTime createdDate;
  private String region;
  private String period;
  private Integer price;
  private boolean isRecruiting;

  public static List<ArticlePageDto> toDtoList(Page<Article> articleList) {
    return articleList.stream()
        .map(article -> ArticlePageDto.builder()
            .id(article.getId())
            .title(article.getTitle())
            .email(article.getUser().getEmail())
            .image(article.getUser().getImage())
            .nickname(article.getUser().getNickname())
            .content(article.getContent())
            .gender(article.getUser().getGender().getValue())
            .createdDate(article.getCreateDate())
            .region(article.getRegion().getValue())
            .period(article.getPeriod().getValue())
            .price(article.getPrice())
            .isRecruiting(article.isRecruiting())
            .build())
        .collect(Collectors.toList());
  }

  public static ArticlePageDto toDto(Article article) {
    return ArticlePageDto.builder()
        .id(article.getId())
        .title(article.getTitle())
        .email(article.getUser().getEmail())
        .image(article.getUser().getImage())
        .nickname(article.getUser().getNickname())
        .content(article.getContent())
        .gender(article.getUser().getGender().getValue())
        .createdDate(article.getCreateDate())
        .region(article.getRegion().getValue())
        .period(article.getPeriod().getValue())
        .price(article.getPrice())
        .isRecruiting(article.isRecruiting())
        .build();
  }
}
