package com.denkarz.jcat.backend.security.jwt;

import com.denkarz.jcat.backend.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerator {
  @Value("${jwt.secret}")
  private String secret;

  public String generate(User jwtUser) {
    Claims claims = Jwts.claims();
    claims.put("user_id", jwtUser.getId());
    claims.put("user_nick", jwtUser.getNickname());
    claims.put("role", jwtUser.getRoles());

    return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
  }
}
