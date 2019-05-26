package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.model.User;
import com.denkarz.jcat.backend.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
  final UserRepository userRepository;
  final User user = new User();

  public HelloController(UserRepository userRepository) {
    this.userRepository = userRepository;
    this.user.setNickname("anon");
    this.user.setPassword("123ee");
    this.user.setFirstName("john");
    this.user.setLastName("snow");
  }

  @CrossOrigin
  @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  User index() {
    return userRepository.save(this.user);
  }

  @CrossOrigin
  @RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  Iterable<User> hello() {
    return userRepository.findAll();
  }

}