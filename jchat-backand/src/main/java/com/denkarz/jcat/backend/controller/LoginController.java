package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.model.Role;
import com.denkarz.jcat.backend.model.User;
import com.denkarz.jcat.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;

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
  public User signUp(User user) {
    User userFromDb = userRepository.findByEmail(user.getEmail());
    if (userFromDb != null) {
      // ToDo: add logger for error
      return null;
    }

    user.setActive(true);
    // ToDo: replace for multiple roles
    user.setRoles(Collections.singleton(Role.USER));
    // ToDo: add logger for success
    return userRepository.save(user);
  }

  @CrossOrigin
  @RequestMapping(value = "/forget", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  User forget() {
    return null;
  }

}
