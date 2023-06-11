package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.dto.mycontent.FavoriteArticleDto;
import com.fab.banggabgo.dto.mycontent.MyArticleDto;
import com.fab.banggabgo.dto.mycontent.MyInfoDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.service.MyContentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyContentServiceImpl implements MyContentService {

  private final ArticleRepository articleRepository;

  @Override
  public List<MyArticleDto> getMyArticle(User user) {
    return articleRepository.getMyArticle(user);
  }

  @Override
  public List<FavoriteArticleDto> getMyFavoriteArticle(User user) {
    return articleRepository.getFavoriteArticle(user);
  }

  @Override
  public MyInfoDto getMyInfo(User user) {
    return MyInfoDto.toDto(user);
  }
}
