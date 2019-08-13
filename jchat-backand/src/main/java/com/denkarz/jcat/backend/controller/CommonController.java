package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.model.user.User;
import com.denkarz.jcat.backend.repository.UserRepository;
import com.denkarz.jcat.backend.service.UserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/v1/test")
public class CommonController {
  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;

  // Метод для отладки
  @RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  Iterable<User> hello() {
    return userService.getAllUsers();
  }

  @PostMapping(value = "/update_roles", produces = "application/json")
  public ResponseEntity updateRoles(@RequestBody JSONObject json) {
    return userService.updateRoles(json);
  }
}
