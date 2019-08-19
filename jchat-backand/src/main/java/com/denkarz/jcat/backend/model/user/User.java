package com.denkarz.jcat.backend.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends AuthenticationUser implements UserDetails {
  private static final int HASH = 5;

  @Column(name = "active")
  private boolean active;

  @NotNull(message = "Birth date should be entered")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Past(message = "Must be in past")
  @Column(name = "birth_date")
  private Date birthDate;

  @NotNull(message = "First name should be entered")
  @Size(min = MIN_SIZE, message = "First name size is less then " + MIN_SIZE)
  @Column(name = "first_name")
  private String firstName;

  @Enumerated(EnumType.ORDINAL)
  private Gender gender;

  @NotNull(message = "Last name should be entered")
  @Size(min = MIN_SIZE, message = "Last name size is less then " + MIN_SIZE)
  @Column(name = "last_name")
  private String lastName;

  @NotNull(message = "Nick name should be entered")
  @Size(min = (MIN_SIZE + 1), message = "Nickname size is less then" + (MIN_SIZE + 1))
  @Column(unique = true, name = "nickname")
  private String nickname;

  @Pattern(regexp = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$", message = "Phone isnt correct")
  @Column(unique = true, name = "phone")
  private String phone;

  @Column(unique = true, name = "activation_code")
  private String activationCode;

  @Column(unique = true, name = "reset_password")
  private String resetPassword;

  // String avatar_url
  // boolean blocked
  // User[] friends
  // Posts[] posts
  // Date lastVisited

  @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  private Set<Role> roles;

  @JsonProperty("age")
  public byte getAge() {
    Calendar dob = Calendar.getInstance();
    dob.setTime(this.birthDate);
    Calendar currentDate = Calendar.getInstance();
    currentDate.setTime(new Date());
    return (byte) (currentDate.get(Calendar.YEAR) - dob.get(Calendar.YEAR));
  }

  public void setEmail(String email) {
    this.email = email.toLowerCase();
  }

  public void setNickname(String nickname) {
    this.nickname = nickname.toLowerCase();
  }

  @Override
  @JsonProperty(access = Access.WRITE_ONLY)
  public Set<GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
    System.out.println(this.roles);
    for (Role temp : this.roles) {
      authorities.add(new SimpleGrantedAuthority(temp.name()));
    }
    return authorities;
  }

  @Override
  @JsonProperty(access = Access.WRITE_ONLY)
  public String getPassword() {
    return this.password;
  }

  @Override
  @JsonProperty(access = Access.WRITE_ONLY)
  public String getUsername() {
    return this.getNickname();
  }

  @Override
  @JsonProperty(access = Access.WRITE_ONLY)
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @JsonProperty(access = Access.WRITE_ONLY)
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @JsonProperty(access = Access.WRITE_ONLY)
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @JsonProperty(access = Access.WRITE_ONLY)
  public boolean isEnabled() {
    return isActive();
  }

  public static class NicknameComparator implements Comparator<User> {
    /**
     * Compare two users by nickname fields.
     *
     * @param o1 user 1
     * @param o2 user 2
     * @return the value {@code 0} if the argument string is equal to
     * this string; a value less than {@code 0} if this string
     * is lexicographically less than the string argument; and a
     * value greater than {@code 0} if this string is
     * lexicographically greater than the string argument.
     */
    public int compare(final User o1, final User o2) {
      return o1.getNickname().compareTo(o2.getNickname());
    }
  }
}