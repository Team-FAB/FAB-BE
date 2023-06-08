package com.fab.banggabgo.dto;

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

  private Long id;
  private String title;
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
            .nickname(article.getUser().getNickname())
            .content(article.getContent())
            .gender(article.getGender().getValue())
            .createdDate(article.getCreateDate())
            .region(article.getRegion().getValue())
            .period(article.getPeriod().getValue())
            .price(article.getPrice())
            .isRecruiting(article.isRecruiting())
            .build())
        .collect(Collectors.toList());
  }
}
