package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.model.user.User;
import com.denkarz.jcat.backend.repository.UserRepository;
import com.denkarz.jcat.backend.service.UserService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;

  @GetMapping(value = "/get_all_users", produces = "application/json")
  @ResponseBody
  Iterable<User> get_all() {
    return userService.getAllUsers();
  }


  @PostMapping(value = "/update_roles", produces = "application/json")
  public ResponseEntity updateRoles(@RequestBody JSONObject json) {
    return userService.updateRoles(json);
  }
}
