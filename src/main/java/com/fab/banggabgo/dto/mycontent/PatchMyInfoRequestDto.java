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
public class PatchMyInfoRequestDto {
  private String gender;
  private int myAge;
  private int minAge;
  private int maxAge;
  private boolean isSmoke;
  private String mbti;
  private String region;
  private String activityTime;
  private List<String> tags;
  private String detail;
}
