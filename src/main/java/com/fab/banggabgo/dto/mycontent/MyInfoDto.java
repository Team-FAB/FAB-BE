package com.fab.banggabgo.dto.mycontent;

import com.fab.banggabgo.entity.User;
import java.util.Set;
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
public class MyInfoDto {

  private String email;
  private String nickname;
  private String image;
  private Boolean isSmoker;
  private String activityTime;
  private String gender;
  private String region;
  private String mbti;
  private Set<String> tags;
  private int minAge;
  private int maxAge;
  private String preferredAge;
  private int myAge;
  private String detail;

  public static MyInfoDto toDto(User user) {
    return MyInfoDto.builder()
        .email(user.getEmail())
        .nickname(user.getNickname())
        .image(user.getImage())
        .isSmoker(user.getIsSmoker())
        .activityTime(user.getActivityTime().name())
        .gender(user.getGender().getValue())
        .region(user.getRegion().getValue())
        .mbti(user.getMbti().name())
        .tags(user.getTag())
        .minAge(user.getMinAge())
        .maxAge(user.getMaxAge())
        .myAge(user.getMyAge())
        .detail(user.getDetail())
        .build();
  }
}
