package com.denkarz.jcat.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;


@MappedSuperclass
@Data
@EqualsAndHashCode(of = {"id"})
public class BaseEntity {
  static final int MIN_SIZE = 2;

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
  private String id;

  @CreatedDate
  @Column(name = "created")
  private Date createdAt;

  @LastModifiedDate
  @Column(name = "updated")
  private Date updatedAt;
}
