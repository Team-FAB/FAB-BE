package com.fab.banggabgo.dto.mycontent;

import com.fab.banggabgo.entity.User;
import java.util.ArrayList;
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
public class PatchMyInfoResultDto {
  private String gender;
  private Integer myAge;
  private Integer minAge;
  private Integer maxAge;
  private boolean isSmoke;
  private String mbti;
  private String region;
  private String activityTime;
  private List<String> tags;
  private String detail;

  public static PatchMyInfoResultDto from(User user) {
    return PatchMyInfoResultDto.builder()
        .gender(user.getGender().getValue())
        .myAge(user.getMyAge())
        .minAge(user.getMinAge())
        .maxAge(user.getMaxAge())
        .isSmoke(user.getIsSmoker())
        .mbti(user.getMbti().name())
        .region(user.getRegion().getValue())
        .activityTime(user.getActivityTime().getValue())
        .tags(new ArrayList<>(user.getTag()))
        .detail(user.getDetail())
        .build();
  }
}
