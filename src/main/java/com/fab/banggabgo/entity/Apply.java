package com.fab.banggabgo.entity;

import com.fab.banggabgo.type.ApproveStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Apply extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "approve_status")
  @Enumerated(value = EnumType.STRING)
  private ApproveStatus approveStatus;

  private boolean isApplicantDelete;

  private boolean isArticleUserDelete;

  private boolean isApplicantRead;

  private boolean isArticleUserRead;

  @ManyToOne
  @JoinColumn(name = "applicant_user_id")
  @ToString.Exclude
  private User applicantUser;

  @ManyToOne
  @JoinColumn(name = "article_id")
  @ToString.Exclude
  private Article article;

}
