package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.config.security.JwtTokenProvider;
import com.fab.banggabgo.dto.ArticleEditDto;
import com.fab.banggabgo.dto.ArticleRegisterDto;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.service.ArticleService;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.Period;
import com.fab.banggabgo.type.Seoul;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;

  @Override
  public void registerArticle(String token, ArticleRegisterDto dto) {
    if (isStringEmpty(dto.getContent()) || isStringEmpty(dto.getTitle())
        || dto.getPrice() < 1000000 || dto.getPrice() > 20000000) {
      throw new RuntimeException("글 등록 양식이 잘못되었습니다.");
    }

    Seoul region = null;
    try {
      region = Seoul.fromValue(dto.getRegion());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("해당 지역이 존재하지 않습니다.");
    }

    Gender gender = null;
    try {
      gender = Gender.fromValue(dto.getGender());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("해당 성별이 존재하지 않습니다.");
    }

    Period period = null;
    try {
      period = Period.fromValue(dto.getPeriod());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("해당 기간이 존재하지 않습니다.");
    }

    User user = getUserFromToken(token);

    int userArticleCnt = articleRepository.countByUserAndIsDeletedFalse(user);

    if (userArticleCnt >= 5) {
      throw new RuntimeException("게시글은 5개까지 작성할 수 있습니다.");
    }

    Article article = Article.builder()
        .user(user)
        .title(dto.getTitle())
        .content(dto.getContent())
        .region(region)
        .period(period)
        .price(dto.getPrice())
        .gender(gender)
        .isRecruit(false)
        .isDeleted(false)
        .build();

    articleRepository.save(article);
  }

  @Override
  public void editArticle(String token, Long id, ArticleEditDto dto) {
    if (isStringEmpty(dto.getContent()) || isStringEmpty(dto.getTitle())
        || dto.getPrice() < 1000000 || dto.getPrice() > 20000000) {
      throw new RuntimeException("글 수정 양식이 잘못되었습니다.");
    }

    Seoul region = null;
    try {
      region = Seoul.fromValue(dto.getRegion());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("해당 지역이 존재하지 않습니다.");
    }

    Gender gender = null;
    try {
      gender = Gender.fromValue(dto.getGender());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("해당 성별이 존재하지 않습니다.");
    }

    Period period = null;
    try {
      period = Period.fromValue(dto.getPeriod());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("해당 기간이 존재하지 않습니다.");
    }

    User user = getUserFromToken(token);

    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));

    if (article.isDeleted()) {
      throw new RuntimeException("삭제된 게시글입니다.");
    }

    if (!Objects.equals(article.getUser().getId(), user.getId())) {
      throw new RuntimeException("해당 게시글의 작성자가 아닙니다.");
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
  public void deleteArticle(String token, Long id) {
    User user = getUserFromToken(token);

    Article article = articleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));

    if (article.isDeleted()) {
      throw new RuntimeException("이미 삭제된 게시글입니다.");
    }

    if (!Objects.equals(article.getUser().getId(), user.getId())) {
      throw new RuntimeException("해당 게시글의 작성자가 아닙니다.");
    }

    article.setDeleted(true);

    articleRepository.save(article);
  }

  private User getUserFromToken(String token) {
    String userEmail = jwtTokenProvider.getUser(token);

    User user = userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new RuntimeException("존재하지않는 유저"));

    return user;
  }

  private static boolean isStringEmpty(String str) {
    return str == null || str.isBlank();
  }
}
