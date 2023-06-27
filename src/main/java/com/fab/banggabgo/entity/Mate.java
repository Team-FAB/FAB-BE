package com.fab.banggabgo.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class Mate extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String chatRoomId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user1_id")
  @ToString.Exclude
  private User user1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user2_id")
  @ToString.Exclude
  private User user2;

}
