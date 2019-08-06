package com.denkarz.jcat.backend.security.jwt;

import com.denkarz.jcat.backend.model.user.Role;
import com.denkarz.jcat.backend.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class JwtValidator {

  @Value("${jwt.secret}")
  private String secret;

  public User validate(String token) {

    User jwtUser = null;
    try {
      Claims body = Jwts.parser()
              .setSigningKey(secret)
              .parseClaimsJws(token)
              .getBody();

      jwtUser = new User();

      jwtUser.setEmail(body.getSubject());
      jwtUser.setId((String) body.get("user_id"));
      String s_role = (String) body.get("role");
      jwtUser.setRoles(Collections.singleton(Role.valueOf(s_role.toUpperCase())));
    } catch (Exception e) {
      System.out.println(e);
    }

    return jwtUser;
  }
}
