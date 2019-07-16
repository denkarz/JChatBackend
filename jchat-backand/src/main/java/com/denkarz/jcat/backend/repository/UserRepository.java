package com.denkarz.jcat.backend.repository;

import com.denkarz.jcat.backend.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

  @Query(value = "SELECT *"
          + " FROM Users users"
          + " WHERE users.email = ?1", nativeQuery = true)
  Optional<User> findByEmail(String email);

}