package com.fab.banggabgo.dto.apply;


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
public class ApplyPageDto {

  private Integer applyId;
  private Integer articleId;
  private String articleTitle;
  private Integer otherUserId;
  private String otherUserName;
  private String matchStatus;
}
