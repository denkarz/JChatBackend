package com.denkarz.jcat.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
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
public class User extends BaseEntity implements UserDetails {
  private static final int HASH = 5;

  @Column(name = "active")
  private boolean active;

  @NotNull(message = "")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Past
  @Column(name = "birth_date")
  private Date birthDate;

  @NotNull(message = "E-Mail Should Be Entered\"")
  @Pattern(regexp = "^(?:[a-zA-Z0-9_'^&/+-])+(?:\\.(?:[a-zA-Z0-9_'^&/+-])+)"
          + "*@(?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.)"
          + "{3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)"
          + "+(?:[a-zA-Z]){2,}\\.?)$", message = "")
  @Column(unique = true, name = "email")
  private String email;

  @NotNull(message = "First Name Should Be Entered")
  @Size(min = MIN_SIZE, message = "Size Of ")
  @Column(name = "first_name")
  private String firstName;

  @Enumerated(EnumType.ORDINAL)
  private Gender gender;

  @NotNull(message = "Last Name Should Be Entered")
  @Size(min = MIN_SIZE, message = "Size Of ")
  @Column(name = "last_name")
  private String lastName;

  @NotNull(message = "Nickname Should Be Entered")
  @Size(min = MIN_SIZE, message = "")
  @Column(unique = true, name = "nickname")
  private String nickname;

  @NotNull(message = "Password Should Be Entered")
  @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "")
  @Column(name = "password")
  @JsonProperty(access = Access.WRITE_ONLY)
  private String password;

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
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
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