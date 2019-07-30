package com.denkarz.jcat.backend.model.user;

import com.denkarz.jcat.backend.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthenticationUser extends BaseEntity {
  @NotNull(message = "E-Mail should be entered")
  @Pattern(regexp = "^(?:[a-zA-Z0-9_'^&/+-])+(?:\\.(?:[a-zA-Z0-9_'^&/+-])+)"
          + "*@(?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.)"
          + "{3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)"
          + "+(?:[a-zA-Z]){2,}\\.?)$", message = "Email isn't correct")
  @Column(unique = true, name = "email")
  protected String email;

  @NotNull(message = "Password should be entered")
  @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
          message = "Password must be 8+ chars, has at least one uppercase char, one lowercase char and one digit")
  @Column(name = "password")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  protected String password;

}
