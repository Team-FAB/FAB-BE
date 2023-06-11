package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.dto.ArticleEditDto;
import com.fab.banggabgo.dto.ArticlePageDto;
import com.fab.banggabgo.dto.ArticleRegisterDto;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.service.ArticleService;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.Period;
import com.fab.banggabgo.type.Price;
import com.fab.banggabgo.type.Seoul;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;

  @Override
  public void postArticle(User user, ArticleRegisterDto dto) {
    if (!StringUtils.hasText(dto.getContent()) || !StringUtils.hasText(dto.getTitle())
        || dto.getPrice() < Price.MINPRICE.getValue()
        || dto.getPrice() > Price.MAXPRICE.getValue()) {
      throw new CustomException(ErrorCode.INVALID_ARTICLE);
    }

    Seoul region = null;
    try {
      region = Seoul.fromValue(dto.getRegion());
    } catch (IllegalArgumentException e) {
      throw new CustomException(ErrorCode.REGION_NOT_EXISTS);
    }

    Gender gender = null;
    try {
      gender = Gender.fromValue(dto.getGender());
    } catch (IllegalArgumentException e) {
      throw new CustomException(ErrorCode.GENDER_NOT_EXISTS);
    }

    Period period = null;
    try {
      period = Period.fromValue(dto.getPeriod());
    } catch (IllegalArgumentException e) {
      throw new CustomException(ErrorCode.PERIOD_NOT_EXISTS);
    }

    int userArticleCnt = articleRepository.countByUserAndIsDeletedFalseAndIsRecruitingTrue(user);

    if (userArticleCnt >= 5) {
      throw new CustomException(ErrorCode.MAX_ARTICLE);
    }

    Article article = Article.builder()
        .user(user)
        .title(dto.getTitle())
        .content(dto.getContent())
        .region(region)
        .period(period)
        .price(dto.getPrice())
        .gender(gender)
        .isRecruiting(true)
        .isDeleted(false)
        .build();

    articleRepository.save(article);
  }

  @Override
  public void putArticle(User user, Integer id, ArticleEditDto dto) {
    if (!StringUtils.hasText(dto.getContent()) || !StringUtils.hasText(dto.getTitle())
        || dto.getPrice() < Price.MINPRICE.getValue()
        || dto.getPrice() > Price.MAXPRICE.getValue()) {
      throw new CustomException(ErrorCode.INVALID_ARTICLE);
    }

    Seoul region = null;
    try {
      region = Seoul.fromValue(dto.getRegion());
    } catch (IllegalArgumentException e) {
      throw new CustomException(ErrorCode.REGION_NOT_EXISTS);
    }

    Gender gender = null;
    try {
      gender = Gender.fromValue(dto.getGender());
    } catch (IllegalArgumentException e) {
      throw new CustomException(ErrorCode.GENDER_NOT_EXISTS);
    }

    Period period = null;
    try {
      period = Period.fromValue(dto.getPeriod());
    } catch (IllegalArgumentException e) {
      throw new CustomException(ErrorCode.PERIOD_NOT_EXISTS);
    }

    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_EXISTS));

    if (article.isDeleted()) {
      throw new CustomException(ErrorCode.ARTICLE_DELETED);
    }

    if (!Objects.equals(article.getUser().getId(), user.getId())) {
      throw new CustomException(ErrorCode.USER_NOT_MATCHED);
    }

    article.setTitle(dto.getTitle());
    article.setContent(dto.getContent());
    article.setRegion(region);
    article.setPeriod(period);
    article.setPrice(dto.getPrice());
    article.setGender(gender);

    articleRepository.save(article);
  }

  @Override
  public void deleteArticle(User user, Integer id) {
    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_EXISTS));

    if (article.isDeleted()) {
      throw new CustomException(ErrorCode.ARTICLE_DELETED);
    }

    if (!Objects.equals(article.getUser().getId(), user.getId())) {
      throw new CustomException(ErrorCode.USER_NOT_MATCHED);
    }

    article.setDeleted(true);

    articleRepository.save(article);
  }

  @Override
  public List<ArticlePageDto> getArticleByPageable(Integer page, Integer size,
      boolean isRecruiting) {

    page = page < 1 ? 1 : page;

    Pageable pageable = PageRequest.of(page - 1, size);

    Page<Article> articleList = articleRepository.getArticle(pageable, isRecruiting);

    return ArticlePageDto.toDtoList(articleList);
  }

  @Override
  public List<ArticlePageDto> getArticleByFilter(Integer page, Integer size, boolean isRecruiting,
      String region, String period, String price, String gender) {

    page = page < 1 ? 1 : page;

    Pageable pageable = PageRequest.of(page - 1, size);

    Page<Article> articleList = articleRepository.getArticleByFilter(pageable, isRecruiting, region,
        period, price, gender);

    return ArticlePageDto.toDtoList(articleList);
  }

  @Override
  public Integer getArticleTotalCnt() {

    return articleRepository.getArticleTotalCnt();
  }
}
