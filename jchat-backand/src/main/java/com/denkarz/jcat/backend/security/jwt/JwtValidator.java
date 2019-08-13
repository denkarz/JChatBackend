package com.denkarz.jcat.backend.security.jwt;

import com.denkarz.jcat.backend.model.user.Role;
import com.denkarz.jcat.backend.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JwtValidator {

  @Value("${jwt.secret}")
  private String secret;

  public User validate(String token) {
//todo error here
    User jwtUser = new User();
    try {
      Claims body = Jwts.parser()
              .setSigningKey(secret)
              .parseClaimsJws(token)
              .getBody();

      jwtUser.setId((String) body.get("user_id"));
      jwtUser.setNickname((String) body.get("user_nick"));
      List rolesList = body.get("role", List.class);
      Set<Role> roles = new HashSet<>();
      for (Object role : rolesList) {
        roles.add(Role.valueOf(role.toString()));
      }
      jwtUser.setRoles(roles);
    } catch (Exception e) {
      System.out.println(e.getClass().getCanonicalName());
      System.out.println("jwt_validator_error " + Arrays.toString(e.getStackTrace()));
    }

    return jwtUser;
  }
}
