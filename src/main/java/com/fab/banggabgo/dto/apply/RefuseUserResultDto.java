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
public class RefuseUserResultDto {

  private Integer approveUserId;
  private String approveUserName;
  private Integer articleId;
  private String articleTitle;
  private String approveStatus;
}
