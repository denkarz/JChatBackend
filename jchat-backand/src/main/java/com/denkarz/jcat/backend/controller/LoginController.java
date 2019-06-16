package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.model.Role;
import com.denkarz.jcat.backend.model.User;
import com.denkarz.jcat.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;

@Controller
public class LoginController {
  @Autowired
  private UserRepository userRepository;

  @CrossOrigin
  @RequestMapping(value = "/sign_in", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  User signIn() {
    return null;
  }

  @CrossOrigin
  @RequestMapping(value = "/sign_up", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public ResponseEntity signUp(@RequestBody User user) {
    User userFromDb = userRepository.findByEmail(user.getEmail());
    if (userFromDb != null) {
      // ToDo: add logger for error
      return ResponseEntity.status(HttpStatus.CONFLICT).body("user_exists");
    }
    //ToDo: поле  password исключается из JSON'a,
    // поэтому я передаю поле firstPassword
    // которое присутствует в ответе от сервера
    // Неоюходимо убрать ненужные поля(firstPassword)
    user.setPassword(user.getFirstPassword());
    user.setFirstPassword(null);

    user.setFirstName("test");
    user.setLastName("testov");
    user.setNickname("tesNick");
    user.setBirthDate(new Date());

//    user.setActive(true);
    // ToDo: replace for multiple roles
    user.setRoles(Collections.singleton(Role.USER));
    // ToDo: add logger for success
    try {
      User savedUser = userRepository.save(user);
      return ResponseEntity.status(HttpStatus.OK).body(savedUser);
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
