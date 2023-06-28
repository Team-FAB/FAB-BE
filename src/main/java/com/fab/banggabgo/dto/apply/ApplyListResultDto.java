package com.fab.banggabgo.dto.apply;

import com.fab.banggabgo.entity.Apply;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyListResultDto {

  private Long totalCount;

  private List<ApplyPageDto> applyPageList;

  public static ApplyListResultDto toFromApplicantDtoList(Page<Apply> applyPage) {
    return ApplyListResultDto.builder()
        .totalCount(applyPage.getTotalElements())
        .applyPageList(applyPage.stream().map(ApplyListResultDto::toFromApplicantDto)
            .collect(Collectors.toList()))
        .build();
  }

  public static ApplyPageDto toFromApplicantDto(Apply apply) {
    return ApplyPageDto.builder()
        .applyId(apply.getId())
        .isToMe(false)
        .articleId(apply.getArticle().getId())
        .articleTitle(apply.getArticle().getTitle())
        .otherUserId(apply.getApplicantUser().getId())
        .otherUserName(apply.getApplicantUser().getNickname())
        .matchStatus(apply.getApproveStatus().getValue())
        .build();
  }

  public static ApplyListResultDto toToApplicantDtoList(Page<Apply> applyPage) {
    return ApplyListResultDto.builder()
        .totalCount(applyPage.getTotalElements())
        .applyPageList(applyPage.stream().map(ApplyListResultDto::toToApplicantDto)
            .collect(Collectors.toList()))
        .build();
  }

  public static ApplyPageDto toToApplicantDto(Apply apply) {
    return ApplyPageDto.builder()
        .applyId(apply.getId())
        .isToMe(true)
        .articleId(apply.getArticle().getId())
        .articleTitle(apply.getArticle().getTitle())
        .otherUserId(apply.getArticle().getUser().getId())
        .otherUserName(apply.getArticle().getUser().getNickname())
        .matchStatus(apply.getApproveStatus().getValue())
        .build();
  }

  public static ApplyListResultDto toMixApplicantDtoList(Page<Apply> applyPage, Integer userId) {
    return ApplyListResultDto.builder()
        .totalCount(applyPage.getTotalElements())
        .applyPageList(applyPage.stream().map((apply -> toMixApplicantDto(apply, userId)))
            .collect(Collectors.toList()))
        .build();
  }

  public static ApplyPageDto toMixApplicantDto(Apply apply, Integer userId) {
    if (apply.getApplicantUser().getId().equals(userId)) {
      return toToApplicantDto(apply);
    } else {
      return toFromApplicantDto(apply);
    }
  }
}
