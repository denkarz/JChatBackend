package com.denkarz.jcat.backend.repository;

import com.denkarz.jcat.backend.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  @Query(value = "SELECT *"
          + " FROM Users users"
          + " WHERE users.email = ?1", nativeQuery = true)
  User findByEmail(String email);

}