package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.model.Role;
import com.denkarz.jcat.backend.model.User;
import com.denkarz.jcat.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@Controller
public class LoginController {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @CrossOrigin
  @RequestMapping(value = "/sign_in", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  ResponseEntity signIn(@RequestBody User user) {
    Optional<User> userFromDb = userRepository.findByEmail(user.getEmail());
    if (userFromDb.isPresent() && passwordEncoder.matches(user.getPassword(), userFromDb.get().getPassword())) {
      // ToDo: add logger for error
      user.setActive(true);
      return ResponseEntity.status(HttpStatus.OK).body(userFromDb);
    }
    return ResponseEntity.status(HttpStatus.CONFLICT).body("user_not_exists");
  }

  /**
   * Проверяем наличие пользоваеля. и разрешаем дальнейшую регистрацию
   *
   * @param email user's mail
   * @return status
   */
  @CrossOrigin
  @RequestMapping(value = "/has_mail", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public ResponseEntity hasMail(@RequestParam(value = "email") String email) {
    email = email.toLowerCase();
    Optional<User> userFromDb = userRepository.findByEmail(email);
    if (userFromDb.isPresent()) {
      // ToDo: add logger for error
      return ResponseEntity.status(HttpStatus.CONFLICT).body("user_exists");
    }
    return ResponseEntity.status(HttpStatus.OK).body("ok");
  }


  @CrossOrigin
  @RequestMapping(value = "/sign_up", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public ResponseEntity signUp(@RequestBody User user) {
    Optional<User> userFromDb = userRepository.findByEmail(user.getEmail());
    if (userFromDb.isPresent()) {
      // ToDo: add logger for error
      return ResponseEntity.status(HttpStatus.CONFLICT).body("user_exists");
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setActive(true);
    user.setNickname(user.getId());
    // ToDo: replace for multiple roles
    user.setRoles(Collections.singleton(Role.USER));
    // ToDo: add logger for success
    try {
      User savedUser = userRepository.save(user);
      return ResponseEntity.status(HttpStatus.OK).body(savedUser.getId());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("validation_error");
    }
  }

//  @CrossOrigin
//  @RequestMapping(value = "/forget", method = RequestMethod.GET, produces = "application/json")
//  @ResponseBody
//  User forget() {
//    return null;
//  }

  // Метод для отладки
  @CrossOrigin
  @RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  Iterable<User> hello() {
    return userRepository.findAll();
  }

}
