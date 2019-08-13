package com.denkarz.jcat.backend.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public class JwtUserDetails implements UserDetails {

  private String userName;
  private String token;
  private String id;
  private Set<GrantedAuthority> authorities;


  public JwtUserDetails(String userName, String id, String token, Set<GrantedAuthority> grantedAuthorities) {

    this.userName = userName;
    this.id = id;
    this.token = token;
    this.authorities = grantedAuthorities;
  }

  @Override
  public Set<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }


  public String getUserName() {
    return userName;
  }

  public String getToken() {
    return token;
  }


  public String getId() {
    return id;
  }

}
