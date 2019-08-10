package com.denkarz.jcat.backend.security.filter;

import com.denkarz.jcat.backend.model.JwtAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Order(2)
public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

  public JwtAuthenticationTokenFilter() {
    super("/api/v1/**");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
          throws AuthenticationException, IOException, ServletException {
    try {
      String header = request.getHeader("Authorisation");
      String authenticationToken = header.substring(6);
      JwtAuthenticationToken token = new JwtAuthenticationToken(authenticationToken);
      return getAuthenticationManager().authenticate(token);

    } catch (RuntimeException e) {
      log.warn("Token is missing");
    }
    return null;
  }


  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                          Authentication authResult) throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
    chain.doFilter(request, response);
  }


}