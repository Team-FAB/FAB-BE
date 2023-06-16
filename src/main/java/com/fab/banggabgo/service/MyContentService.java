package com.fab.banggabgo.service;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.dto.mycontent.FavoriteArticleDto;
import com.fab.banggabgo.dto.mycontent.MyArticleDto;
import com.fab.banggabgo.dto.mycontent.MyInfoDto;
import com.fab.banggabgo.dto.mycontent.PatchMyInfoRequestDto;
import com.fab.banggabgo.dto.mycontent.PatchMyInfoResultDto;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameRequestDto;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameResult;
import com.fab.banggabgo.entity.User;
import java.util.List;

public interface MyContentService {


  List<MyArticleDto> getMyArticle(User user);

  List<FavoriteArticleDto> getMyFavoriteArticle(User user);

  MyInfoDto getMyInfo(User user);

  PatchMyNicknameResult patchNickname(User user, PatchMyNicknameRequestDto toDto) throws CustomException;

  PatchMyInfoResultDto patchMyInfo(User user, PatchMyInfoRequestDto form);
}
