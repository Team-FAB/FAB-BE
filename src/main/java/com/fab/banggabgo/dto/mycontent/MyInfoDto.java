package com.fab.banggabgo.dto.mycontent;

import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.type.ActivityTime;
import com.fab.banggabgo.type.Gender;
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
  private String preferredAge;
  private int myAge;
  private String detail;


  public static MyInfoDto toDto(User user){
    return MyInfoDto.builder()
        .email(user.getEmail())
        .nickname(user.getNickname())
        .image(user.getImage())
        .isSmoker(user.getIsSmoker())
        .activityTime(user.getActivityTime().name())
        .gender(user.getGender().getValue())
        .region(user.getRegion().getValue())
        .mbti(user.getMbti().name())
        .preferredAge(user.getPreferredAge().name())
        .myAge(user.getMyAge())
        .detail(user.getDetail())
        .build();
  }
}
