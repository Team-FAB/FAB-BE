package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.dto.mycontent.FavoriteArticleDto;
import com.fab.banggabgo.dto.mycontent.MyArticleDto;
import com.fab.banggabgo.dto.mycontent.MyInfoDto;
import com.fab.banggabgo.dto.mycontent.PatchMyInfoDto;
import com.fab.banggabgo.dto.mycontent.PatchMyInfoResultDto;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameDto;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameResult;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.service.MyContentService;
import com.fab.banggabgo.type.ActivityTime;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.Mbti;
import com.fab.banggabgo.type.Seoul;
import java.util.HashSet;
import java.util.List;
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
    } catch (DataIntegrityViolationException e) {
      throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
    }
  }

  @Override
  public PatchMyInfoResultDto patchMyInfo(User user, PatchMyInfoDto dto) {
    var converted_user=convertUserData(user,dto);
    return PatchMyInfoResultDto.from(userRepository.save(converted_user));
  }

  public User convertUserData(User user, PatchMyInfoDto dto) {
    var changed_user = user;
    try {
      changed_user.setGender(Gender.fromValue(dto.getGender()));
      changed_user.setMyAge(dto.getMyAge());
      changed_user.setIsSmoker(dto.isSmoke());
      changed_user.setMbti(Mbti.valueOf(dto.getMbti()));
      changed_user.setRegion(Seoul.fromValue(dto.getRegion()));
      changed_user.setActivityTime(ActivityTime.valueOf(dto.getActivityTime()));
      changed_user.setTag(new HashSet<>(dto.getTags()));
      changed_user.setDetail(dto.getDetail());
    }catch (Exception e){
      throw new CustomException(ErrorCode.PATCH_MY_INFO_CONVERT_FAIL);
    }
    return changed_user;
  }
}
