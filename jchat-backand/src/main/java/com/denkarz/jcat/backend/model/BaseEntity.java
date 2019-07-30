package com.denkarz.jcat.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


@MappedSuperclass
@Data
@EqualsAndHashCode(of = {"id"})
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
  protected static final int MIN_SIZE = 2;

  /**
   * ID of entity in database.
   */
  @Column(name = "id")
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  protected String id;

  @CreatedDate
  @Column(name = "created")
  protected Date createdAt;

  @LastModifiedDate
  @Column(name = "updated")
  protected Date updatedAt;
}
