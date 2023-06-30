package com.fab.banggabgo.dto.mycontent;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatchMyInfoForm {
  @ApiModelProperty(value = "성별", example = "MALE")

  private String gender;
  @ApiModelProperty(value = "본인 나이", example = "23")

  private Integer myAge;
  @ApiModelProperty(value = "선호나이 (최소)", example = "21")

  private Integer minAge;
  @ApiModelProperty(value = "선호나이 (최대)", example = "28")

  private Integer maxAge;
  @ApiModelProperty(value = "흡연여부", example = "false")

  private boolean isSmoke;
  @ApiModelProperty(value = "mbti", example = "ENTJ")

  private String mbti;
  @ApiModelProperty(value = "지역", example = "서초구")

  private String region;
  @ApiModelProperty(value = "활동시간", example = "오후")

  private String activityTime;
  @ApiModelProperty(value = "태그", example = "[\"밥같이먹는\",\"늙고병든\"]")

  private List<String> favoriteTag;
  @ApiModelProperty(value = "상세정보", example = "안녕하세요. xxx입니다. 잘부탁드립니다.:)")

  private String myText;

  public static PatchMyInfoRequestDto toDto(PatchMyInfoForm form){
      return PatchMyInfoRequestDto.builder()
          .gender(form.getGender())
          .myAge(form.getMyAge())
          .minAge(form.getMinAge())
          .maxAge(form.getMaxAge())
          .isSmoke(form.isSmoke())
          .activityTime(form.getActivityTime())
          .region(form.getRegion())
          .mbti(form.getMbti())
          .tags(form.getFavoriteTag())
          .detail(form.getMyText())
          .build();
  }

}
