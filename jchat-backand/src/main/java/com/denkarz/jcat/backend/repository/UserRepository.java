package com.denkarz.jcat.backend.repository;

import com.denkarz.jcat.backend.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
  /**
   * Find User by nick.
   *
   * @param name nickname.
   * @return User.
   */
  @Query(value = "SELECT *"
          + " FROM Users users"
          + " WHERE users.nickname = ?1", nativeQuery = true)
  User findByNick(String name);

}