package com.fab.banggabgo.dto.apply;

import com.fab.banggabgo.entity.Apply;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyListResultDto {

  Integer applyId;
  Integer articleId;
  String articleTitle;
  Integer otherUserId;
  String otherUserName;
  String matchStatus;

  public static List<ApplyListResultDto> toDtoList(List<Apply> applyPage) {
    return applyPage.stream().map(ApplyListResultDto::toDto)
        .collect(Collectors.toList());
  }

  public static ApplyListResultDto toDto(Apply apply) {
    return ApplyListResultDto.builder()
        .applyId(apply.getId())
        .articleId(apply.getArticle().getId())
        .articleTitle(apply.getArticle().getTitle())
        .otherUserId(apply.getApplicantUser().getId())
        .otherUserName(apply.getApplicantUser().getNickname())
        .matchStatus(apply.getApproveStatus().getValue())
        .build();
  }
  public static List<ApplyListResultDto> toToDtoList(List<Apply> applyPage) {
    return applyPage.stream().map(ApplyListResultDto::toToDto)
        .collect(Collectors.toList());
  }

  public static ApplyListResultDto toToDto(Apply apply) {
    return ApplyListResultDto.builder()
        .applyId(apply.getId())
        .articleId(apply.getArticle().getId())
        .articleTitle(apply.getArticle().getTitle())
        .otherUserId(apply.getArticle().getUser().getId())
        .otherUserName(apply.getArticle().getUser().getNickname())
        .matchStatus(apply.getApproveStatus().getValue())
        .build();
  }
}
