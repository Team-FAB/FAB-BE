package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.dto.apply.ApplyListResultDto;
import com.fab.banggabgo.dto.mycontent.FavoriteArticleDto;
import com.fab.banggabgo.dto.mycontent.MyArticleDto;
import com.fab.banggabgo.dto.mycontent.MyInfoDto;
import com.fab.banggabgo.dto.mycontent.PatchMyInfoRequestDto;
import com.fab.banggabgo.dto.mycontent.PatchMyInfoResultDto;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameRequestDto;
import com.fab.banggabgo.dto.mycontent.PatchMyNicknameResult;
import com.fab.banggabgo.dto.mycontent.PostMyInfoImageRequestDto;
import com.fab.banggabgo.dto.mycontent.PostMyInfoImageResultDto;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ApplyRepository;
import com.fab.banggabgo.repository.ArticleRepository;
import com.fab.banggabgo.repository.UserRepository;
import com.fab.banggabgo.service.MyContentService;
import com.fab.banggabgo.service.S3Service;
import com.fab.banggabgo.type.ActivityTime;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.Mbti;
import com.fab.banggabgo.type.Seoul;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyContentServiceImpl implements MyContentService {

  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;
  private final ApplyRepository applyRepository;
  private final S3Service s3Service;

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
  public PatchMyNicknameResult patchNickname(User user, PatchMyNicknameRequestDto dto)
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
  public PatchMyInfoResultDto patchMyInfo(User user, PatchMyInfoRequestDto dto) {
    var converted_user = convertUserData(user, dto);
    return PatchMyInfoResultDto.from(userRepository.save(converted_user));
  }


  @Override
  public PostMyInfoImageResultDto postMyInfoImage(User user, PostMyInfoImageRequestDto dto)
      throws IOException {

    var img_url = s3Service.fileUpload(dto.getImage());

    user.setImage(img_url);
    userRepository.save(user);

    return PostMyInfoImageResultDto.builder()
        .image(img_url)
        .build();
  }

  public User convertUserData(User user, PatchMyInfoRequestDto dto) {
    var changed_user = user;
    try {
      changed_user.setGender(Gender.fromValue(dto.getGender()));
      changed_user.setMyAge(dto.getMyAge());
      changed_user.setMinAge(dto.getMinAge());
      changed_user.setMaxAge(dto.getMaxAge());
      changed_user.setIsSmoker(dto.isSmoke());
      changed_user.setMbti(Mbti.valueOf(dto.getMbti()));
      changed_user.setRegion(Seoul.fromValue(dto.getRegion()));
      changed_user.setActivityTime(ActivityTime.fromValue(dto.getActivityTime()));
      changed_user.setTag(new HashSet<>(dto.getTags()));
      changed_user.setDetail(dto.getDetail());
    } catch (Exception e) {
      throw new CustomException(ErrorCode.PATCH_MY_INFO_CONVERT_FAIL);
    }
    return changed_user;
  }

  public ApplyListResultDto getMyFromApplicantList(User user, Integer page, Integer size) {
    page = page > 0 ? page - 1 : 0;

    Pageable pageable = PageRequest.of(page, size);
    return ApplyListResultDto.toFromApplicantDtoList(
        applyRepository.getMyApplicant(pageable, user.getId()));
  }

  public ApplyListResultDto getMyToApplicantList(User user, Integer page, Integer size) {
    page = page > 0 ? page - 1 : 0;

    Pageable pageable = PageRequest.of(page, size);
    return ApplyListResultDto.toToApplicantDtoList(
        applyRepository.getMyToApplicant(pageable, user.getId()));
  }
}
