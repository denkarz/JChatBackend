package com.denkarz.jcat.backend.security.jwt;

import com.denkarz.jcat.backend.model.JwtAuthenticationToken;
import com.denkarz.jcat.backend.model.JwtUserDetails;
import com.denkarz.jcat.backend.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  @Autowired
  private JwtValidator validator;

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

  }

  @Override
  protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

    JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;
    String token = jwtAuthenticationToken.getToken();
    User jwtUser = validator.validate(token);

    if (jwtUser == null) {
      throw new RuntimeException("JWT Token is incorrect");
    }

    List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) jwtUser.getAuthorities();
    return new JwtUserDetails(jwtUser.getEmail(), jwtUser.getId(),
            token,
            grantedAuthorities);
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
  }
}
