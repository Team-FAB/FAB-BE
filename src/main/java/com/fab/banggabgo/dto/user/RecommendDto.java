package com.fab.banggabgo.dto.user;

import com.fab.banggabgo.entity.User;
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
public class RecommendDto {

  private Integer id;
  private String nickname;
  private String mbti;

  public static List<RecommendDto> toDtoList(List<User> userList) {
    return userList.stream()
        .map(user -> RecommendDto.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .mbti(user.getMbti().toString())
            .build())
        .collect(Collectors.toList());
  }
}
