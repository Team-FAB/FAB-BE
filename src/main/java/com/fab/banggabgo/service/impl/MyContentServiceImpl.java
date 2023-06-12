package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.dto.mycontent.FavoriteArticleDto;
import com.fab.banggabgo.dto.mycontent.MyArticleDto;
import com.fab.banggabgo.dto.mycontent.MyInfoDto;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameDto;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameResult;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.service.MyContentService;
import java.sql.SQLException;
import java.util.List;
import com.fab.banggabgo.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyContentServiceImpl implements MyContentService {

  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;

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

  @Override
  public PatchMyNicknameResult patchNickname(User user, PatchMyNicknameDto dto)
      throws CustomException {

    user.setNickname(dto.getNickname());
    try {
      var result = userRepository.save(user);

      return PatchMyNicknameResult.builder()
          .nickname(result.getNickname())
          .build();
    }catch (DataIntegrityViolationException e){
      throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
    }
  }
}
