package com.fab.banggabgo.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
public class BaseEntity {

  @CreatedDate
  @Column(name = "create_date_time", updatable = false)
  private LocalDateTime createDate;

  @LastModifiedDate
  @Column(name = "modified_date_time")
  private LocalDateTime lastModifiedDate;

}

