package com.denkarz.jcat.backend.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
  private String email;
  private String password;
}
