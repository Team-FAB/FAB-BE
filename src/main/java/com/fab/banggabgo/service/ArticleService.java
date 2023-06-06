package com.fab.banggabgo.service;

import com.fab.banggabgo.dto.ArticleRegisterDto;

public interface ArticleService {

  void registerArticle(String token, ArticleRegisterDto dto);
}
