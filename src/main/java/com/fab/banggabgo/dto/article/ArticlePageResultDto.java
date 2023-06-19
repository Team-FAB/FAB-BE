package com.fab.banggabgo.dto.article;

import com.fab.banggabgo.entity.Article;
import java.util.List;
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
public class ArticlePageResultDto {

  private long totalCnt;
  private List<ArticlePageDto> articleList;

  public static ArticlePageResultDto toDto(Page<Article> articlePage) {
    return ArticlePageResultDto.builder()
        .totalCnt(articlePage.getTotalElements())
        .articleList(ArticlePageDto.toDtoList(articlePage))
        .build();
  }
}
