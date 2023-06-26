package com.fab.banggabgo.service.impl;

import com.fab.banggabgo.common.exception.CustomException;
import com.fab.banggabgo.common.exception.ErrorCode;
import com.fab.banggabgo.dto.chatRoom.MateDto;
import com.fab.banggabgo.entity.Apply;
import com.fab.banggabgo.entity.Mate;
import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.repository.ApplyRepository;
import com.fab.banggabgo.repository.MateRepository;
import com.fab.banggabgo.type.ApproveStatus;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl {

  private final MateRepository mateRepository;
  private final ApplyRepository applyRepository;

  public MateDto createChatRoom(User user, Integer applyId) {
    Apply apply = applyRepository.findById(applyId).orElseThrow(() -> new CustomException(
        ErrorCode.NOT_FOUND_APPLY_ID));

    validExistsChatRoom(user, apply);

    User otherUser = apply.getApplicantUser().getId().equals(user.getId()) ?
        apply.getArticle().getUser() : apply.getApplicantUser();

    Mate mate = mateRepository.findMate(user, otherUser).orElseGet(() -> mateRepository.save(
        Mate.builder()
            .user1(user)
            .user2(otherUser)
            .chatRoomId(UUID.randomUUID().toString())
            .build()));

    return MateDto.toDto(mate);
  }

  private void validExistsChatRoom(User user, Apply apply) {
    validApply(user, apply);

    if (!apply.getApproveStatus().equals(ApproveStatus.APPROVAL)) {
      throw new CustomException(ErrorCode.ONLY_MATCH_APPROVE);
    }
  }

  private void validApply(User user, Apply apply) {
    if (!apply.getApplicantUser().getId().equals(user.getId()) && !apply.getArticle().getUser()
        .getId().equals(user.getId())) {
      throw new CustomException(ErrorCode.INVALID_APPLY_ID);
    }
  }
}
