package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping(value = "/get", produces = "application/json")
  @ResponseBody
  public ResponseEntity get(@RequestParam(value = "id") String id) {
    return userService.getUser(id);
  }

  @PostMapping(value = "/logout", produces = "application/json")
  @ResponseBody
  public ResponseEntity logout(@RequestBody String id) throws IOException {
    // todo refactor jason object to a single parameter
    Map map;
    map = new ObjectMapper().readValue(id, Map.class);
    String userId = map.get("id").toString();
    return userService.logout(userId);
  }
}
