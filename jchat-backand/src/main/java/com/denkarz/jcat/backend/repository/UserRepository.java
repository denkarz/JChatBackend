package com.denkarz.jcat.backend.repository;

import com.denkarz.jcat.backend.model.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

  @Query(value = "SELECT *"
          + " FROM Users users"
          + " WHERE users.email = ?1", nativeQuery = true)
  Optional<User> findByEmail(String email);

  @Query(value = "SELECT *"
          + " FROM Users users"
          + " WHERE users.id = ?1 or users.nickname = ?2", nativeQuery = true)
  Optional<User> findByIdOrNick(String id, String nick);

  @Query(value = "SELECT *"
          + " FROM Users users"
          + " WHERE users.activation_code = ?1", nativeQuery = true)
  Optional<User> findByActivationCode(String code);

  @Query(value = "SELECT *"
          + " FROM Users users"
          + " WHERE users.reset_password = ?1", nativeQuery = true)
  Optional<User> findByResetPasswordCode(String code);

  @Modifying
  @Transactional
  @Query(value = "begin;\n" +
          "DELETE FROM\n" +
          "  user_role\n" +
          "WHERE\n" +
          "  user_id in (\n" +
          "    SELECT\n" +
          "      id\n" +
          "    FROM\n" +
          "      users\n" +
          "    WHERE\n" +
          "      created < (NOW() - INTERVAL '2 days')\n" +
          "      AND activation_code IS NOT NULL\n" +
          "  );\n" +
          "DELETE FROM\n" +
          "  users\n" +
          "WHERE\n" +
          "  created < (NOW() - INTERVAL '2 days')\n" +
          "  AND activation_code IS NOT NULL;" +
          "commit;\n", nativeQuery = true)
  void deleteAllNonActivated();
}