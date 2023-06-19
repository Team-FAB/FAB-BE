package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.dto.apply.ApproveUserDto;
import com.fab.banggabgo.dto.apply.ApproveUserResultDto;
import com.fab.banggabgo.entity.Apply;
import com.fab.banggabgo.entity.Article;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ApplyRepository;
import com.fab.banggabgo.type.ApproveStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

  private void validPatchApprove(Article article) {

    if (!article.isRecruiting()) {
      throw new CustomException(ErrorCode.ALREADY_END_RECRUITING);
    }

    if (article.isDeleted()) {
      throw new CustomException(ErrorCode.ARTICLE_DELETED);
    }
  }
}
