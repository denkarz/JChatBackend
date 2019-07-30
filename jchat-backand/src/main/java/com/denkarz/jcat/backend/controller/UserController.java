package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.model.user.User;
import com.denkarz.jcat.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @GetMapping(value = "/profile", produces = "application/json")
  @ResponseBody
  public ResponseEntity profile(@RequestParam(value = "id") String id) {
    Optional<User> userFromDb = userRepository.findById(id);
    if (userFromDb.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(userFromDb);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user_not_found");
  }
}
