package com.denkarz.jcat.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
public class User {
  static final int MIN_SIZE = 3;
  private static final int HASH = 5;
  /**
   * ID of entity in database.
   */
  @Column(name = "id")
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
          name = "UUID",
          strategy = "org.hibernate.id.UUIDGenerator"
  )
  private String id;

  @NotNull(message = "")
  @Size(min = MIN_SIZE, message = "")
  @Column(unique = true, name = "nickname")
  private String nickname;

  @NotNull(message = "First Name Should Be Entered")
  @Size(min = MIN_SIZE, message = "Size Of ")
  @Column(name = "first_name")
  private String firstName;

  @NotNull(message = "First Name Should Be Entered")
  @Size(min = MIN_SIZE, message = "Size Of ")
  @Column(name = "last_name")
  private String lastName;

  @NotNull(message = "")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Past
  @Column(name = "birth_date")
  private Date birthDate;


  @NotNull(message = "")
  @Pattern(regexp = "^(?:[a-zA-Z0-9_'^&/+-])+(?:\\.(?:[a-zA-Z0-9_'^&/+-])+)"
          + "*@(?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.)"
          + "{3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)"
          + "+(?:[a-zA-Z]){2,}\\.?)$", message = "")
  @Column(unique = true, name = "e_mail")
  private String email;

  @NotNull(message = "")
  @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$", message = "")
  @Column(name = "password")
  @JsonIgnore
  private String password;

  @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  private Set<Role> roles;

  private boolean active;

  public User() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public byte getAge(final Date birthDate) {
    Calendar dob = Calendar.getInstance();
    dob.setTime(birthDate);
    Calendar currentDate = Calendar.getInstance();
    currentDate.setTime(new Date());
    return (byte) (currentDate.get(Calendar.YEAR) - dob.get(Calendar.YEAR));
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public final int hashCode() {
    return HASH;
  }

  @Override
  public final boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final User other = (User) obj;
    if (!Objects.equals(this.nickname, other.nickname)) {
      return false;
    }
    if (!Objects.equals(this.firstName, other.firstName)) {
      return false;
    }
    if (!Objects.equals(this.lastName, other.lastName)) {
      return false;
    }
    if (!Objects.equals(this.email, other.email)) {
      return false;
    }
    if (!Objects.equals(this.password, other.password)) {
      return false;
    }
    return Objects.equals(this.birthDate, other.birthDate);
  }

  @Override
  public final String toString() {
    return "User's ID: " + this.getId()
            + '\n'
            + "NickName: "
            + this.getNickname() + '\n'
            + "First Name: "
            + this.getFirstName() + '\n'
            + "Last Name: "
            + this.getLastName() + '\n'
            + "Age: "
            + this.getAge(this.getBirthDate()) + '\n'
            + "E-Mail: "
            + this.getEmail() + '\n'
            + "B-Day: "
            + this.getBirthDate().toString() + '\n';
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