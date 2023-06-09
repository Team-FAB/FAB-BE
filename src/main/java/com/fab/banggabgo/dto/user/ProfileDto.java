package com.fab.banggabgo.dto.user;

import com.fab.banggabgo.entity.User;
import com.fab.banggabgo.type.ActivityTime;
import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.Mbti;
import com.fab.banggabgo.type.Seoul;
import java.util.Optional;
import java.util.Set;
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
public class ProfileDto {

  private String email;
  private String nickname;
  private String image;
  private Boolean isSmoker;
  private String activityTime;
  private String gender;
  private String region;
  private String mbti;
  private Set<String> tags;
  private Integer minAge;
  private Integer maxAge;

  private Integer myAge;
  private String detail;

  public static ProfileDto toDto(User user) {
    return ProfileDto.builder()
        .email(user.getEmail())
        .nickname(user.getNickname())
        .image(user.getImage())
        .isSmoker(user.getIsSmoker())
        .activityTime(Optional.ofNullable(user.getActivityTime()).map(ActivityTime::getValue).orElse("null"))
        .gender(Optional.ofNullable(user.getGender()).map(Gender::getValue).orElse("null"))
        .region(Optional.ofNullable(user.getRegion()).map(Seoul::getValue).orElse("null"))
        .mbti(Optional.ofNullable(user.getMbti()).map(Mbti::name).orElse("null"))
        .tags(user.getTag())
        .minAge(user.getMinAge())
        .maxAge(user.getMaxAge())
        .myAge(user.getMyAge())
        .detail(user.getDetail())
        .build();
  }

}
