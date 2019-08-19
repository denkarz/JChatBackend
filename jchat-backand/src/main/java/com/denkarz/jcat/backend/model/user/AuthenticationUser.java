package com.denkarz.jcat.backend.model.user;

import com.denkarz.jcat.backend.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthenticationUser extends BaseEntity {
  @NotNull(message = "Email should be entered")
  @Pattern(regexp = "^(?:[a-zA-Z0-9_'^&/+-])+(?:\\.(?:[a-zA-Z0-9_'^&/+-])+)"
          + "*@(?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.)"
          + "{3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)"
          + "+(?:[a-zA-Z]){2,}\\.?)$", message = "Email isnt correct")
  @Column(unique = true, name = "email")
  protected String email;

  //todo add all alphabethic chars via unicode (not [a-zа-я])
  @NotNull(message = "Password should be entered")
  @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-ZА-Я])(?=.*[a-zа-я]).*$",
          message = "Password must be 8+ chars, has at least one uppercase char, one lowercase char and one digit")
  @Column(name = "password")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  protected String password;

}
