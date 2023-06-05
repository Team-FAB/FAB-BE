package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.config.security.JwtTokenProvider;
import com.fab.banggabgo.dto.ArticleRegisterDto;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.service.ArticleService;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.Seoul;
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
    if (isStringEmpty(dto.getContent()) || isStringEmpty(dto.getTitle()) || dto.getPrice() <= 0) {
      throw new RuntimeException("글 등록 양식이 잘못되었습니다.");
    }

    User user = getUserFromToken(token);

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

    Article article = Article.builder()
        .user(user)
        .title(dto.getTitle())
        .content(dto.getContent())
        .region(region)
        .period(dto.getPeriod())
        .price(dto.getPrice())
        .gender(gender)
        .isRecruit(false)
        .isDeleted(false)
        .build();

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
