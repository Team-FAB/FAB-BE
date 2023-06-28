package com.fab.banggabgo.dto.user;

import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.type.ActivityTime;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.MatchStatus;
import com.fab.banggabgo.type.Mbti;
import com.fab.banggabgo.type.Seoul;
import com.fab.banggabgo.type.UserType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

  private Integer id;
  private String email;
  private String password;
  private String nickname;
  private UserType userType;
  private String image;
  private MatchStatus matchStatus;
  private Boolean isSmoker;
  private ActivityTime activityTime;
  private Gender gender;
  private Seoul region;
  private Mbti mbti;
  private Integer minAge;
  private Integer maxAge;
  private Integer myAge;
  private Set<String> tag;
  private String detail;
  private List<String> roles;

  public static UserDto toDto(User user) {
    return UserDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .password(user.getPassword())
        .nickname(user.getNickname())
        .userType(user.getUserType())
        .image(user.getImage())
        .matchStatus(user.getMatchStatus())
        .isSmoker(user.getIsSmoker())
        .activityTime(user.getActivityTime())
        .gender(user.getGender())
        .region(user.getRegion())
        .mbti(user.getMbti())
        .minAge(user.getMinAge())
        .maxAge(user.getMaxAge())
        .myAge(user.getMyAge())
        .tag(new HashSet<>(user.getTag()))
        .detail(user.getDetail())
        .roles(new ArrayList<>(user.getRoles()))
        .build();
  }
}
