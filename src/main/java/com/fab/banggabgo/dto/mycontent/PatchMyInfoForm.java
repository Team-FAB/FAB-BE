package com.fab.banggabgo.dto.mycontent;

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

  private String gender;
  private int myAge;
  private int minAge;
  private int maxAge;
  private boolean isSmoke;
  private String mbti;
  private String region;
  private String activityTime;
  private List<String> favoriteTag;
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
