package com.fab.banggabgo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.hibernate.type.descriptor.sql.TinyIntTypeDescriptor;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Apply  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "is_approved")
    private boolean isApproved;

    @ManyToOne
    @JoinColumn(name = "apply_user_id")
    @ToString.Exclude
    private User applyUser;

    @ManyToOne
    @JoinColumn(name = "applied_user_id")
    @ToString.Exclude
    private User appliedUser;

    @ManyToOne
    @JoinColumn(name = "article_id")
    @ToString.Exclude
    private Article article;
}
