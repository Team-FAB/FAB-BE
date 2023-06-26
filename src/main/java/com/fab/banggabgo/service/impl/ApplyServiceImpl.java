package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.dto.apply.ApplyDeleteNoticeResultDto;
import com.fab.banggabgo.dto.apply.ApplyDeleteResultDto;
import com.fab.banggabgo.dto.apply.ApplyListResultDto;
import com.fab.banggabgo.dto.apply.ApproveUserDto;
import com.fab.banggabgo.dto.apply.ApproveUserResultDto;
import com.fab.banggabgo.dto.apply.RefuseUserResultDto;
import com.fab.banggabgo.entity.Apply;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ApplyRepository;
import com.fab.banggabgo.type.ApproveStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplyServiceImpl {

  private final ApplyRepository applyRepository;

  public ApproveUserResultDto patchApprove(User user, ApproveUserDto approveUserDto) {
    if (approveUserDto.getUserId() == null || approveUserDto.getArticleId() == null) {
      throw new CustomException(ErrorCode.INVALID_ARTICLE);
    }

    List<Apply> applyList = applyRepository.getAllMyApplicantByArticleId(user.getId(),
        approveUserDto.getArticleId());

    if (applyList.isEmpty()) {
      throw new CustomException(ErrorCode.ARTICLE_NOT_EXISTS);
    }

    Article article = applyList.get(0).getArticle();

    validPatchApprove(article);

    Apply approveApply = null;
    for (Apply apply : applyList) {
      if (apply.getApplicantUser().getId().equals(approveUserDto.getUserId())) {
        apply.setApproveStatus(ApproveStatus.APPROVAL);
        approveApply = apply;
      } else {
        apply.setApproveStatus(ApproveStatus.REFUSE);
      }
    }

    if (approveApply == null) {
      throw new CustomException(ErrorCode.INVALID_APPLY_USER_ID);
    }

    article.setRecruiting(false);

    applyRepository.saveAll(applyList);

    return ApproveUserResultDto.builder()
        .approveStatus(approveApply.getApproveStatus().getValue())
        .approveUserId(approveApply.getApplicantUser().getId())
        .approveUserName(approveApply.getApplicantUser().getNickname())
        .articleId(article.getId())
        .articleTitle(article.getTitle())
        .build();
  }

  public RefuseUserResultDto patchRefuse(User user, Integer applyId) {
    if (applyId == null) {
      throw new CustomException(ErrorCode.INVALID_ARTICLE);
    }

    Apply apply = applyRepository.findById(applyId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPLY_ID));

    Article article = apply.getArticle();
    if (!article.getUser().getId().equals(user.getId())) {
      throw new CustomException(ErrorCode.USER_NOT_MATCHED);
    }

    validPatchRefuse(apply, article);

    apply.setApproveStatus(ApproveStatus.REFUSE);

    applyRepository.save(apply);
    return RefuseUserResultDto.builder()
        .approveStatus(apply.getApproveStatus().getValue())
        .approveUserId(apply.getApplicantUser().getId())
        .approveUserName(apply.getApplicantUser().getNickname())
        .articleId(article.getId())
        .articleTitle(article.getTitle())
        .build();
  }

  public ApplyDeleteResultDto deleteApply(User user, Integer applyId) {
    Apply apply = applyRepository.findById(applyId)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_APPLY_ID));
    Article article = apply.getArticle();

    if (!apply.getApplicantUser().getId().equals(user.getId())
        && !article.getUser().getId().equals(user.getId())) {
      throw new CustomException(ErrorCode.USER_NOT_MATCHED);
    }

    Integer applySaveId = Math.toIntExact(applyRepository.setApplyDelete(user.getId(), applyId,
        apply.getApplicantUser().getId().equals(user.getId())));

    return ApplyDeleteResultDto.builder()
        .applyId(applySaveId)
        .build();
  }

  public ApplyListResultDto getNotices(User user, Integer page, Integer size) {
    page = page >= 1 ? page - 1 : 1;
    Pageable pageable = PageRequest.of(page, size);
    return ApplyListResultDto.toMixApplicantDtoList(
        applyRepository.getMyNoticeApplicant(pageable, user.getId()), user.getId());
  }

  public ApplyDeleteNoticeResultDto deleteNotice(User user, Integer applyId) {

    Apply apply = applyRepository.findById(applyId)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_APPLY_ID));

    if(apply.getArticle().getUser().getId().equals(user.getId())){
      apply.setArticleUserRead(true);
    } else if(apply.getApplicantUser().getId().equals(user.getId())){
      apply.setApplicantRead(true);
    } else {
      throw new CustomException(ErrorCode.USER_NOT_MATCHED);
    }

    Long dBApplyId = applyRepository.setRead(applyId, apply.getApplicantUser().getId().equals(user.getId()));

    return ApplyDeleteNoticeResultDto.builder()
        .applyId(dBApplyId)
        .build();
  }

  private void validPatchRefuse(Apply apply, Article article) {
    validRecruitingArticle(article);

    if (apply.getApproveStatus().equals(ApproveStatus.REFUSE)) {
      throw new CustomException(ErrorCode.ALREADY_REFUSE);
    }
  }

  private void validPatchApprove(Article article) {
    validRecruitingArticle(article);
  }

  private void validRecruitingArticle(Article article) {
    if (!article.isRecruiting()) {
      throw new CustomException(ErrorCode.ALREADY_END_RECRUITING);
    }

    if (article.isDeleted()) {
      throw new CustomException(ErrorCode.ARTICLE_DELETED);
    }
  }
}
