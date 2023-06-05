package com.fab.banggabgo.entity;


import com.fab.banggabgo.type.Gender;
import com.fab.banggabgo.type.Period;
import com.fab.banggabgo.type.Seoul;
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
public class Article extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private User user;

  private String title;
  private String content;

  @Enumerated(EnumType.STRING)
  private Seoul region;

  @Enumerated(EnumType.STRING)
  private Period period;

  private Integer price;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  private boolean isRecruit;
  private boolean isDeleted;
}
