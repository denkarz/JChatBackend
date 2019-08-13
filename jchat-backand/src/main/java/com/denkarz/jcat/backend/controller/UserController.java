package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.model.user.User;
import com.denkarz.jcat.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user")
@PreAuthorize("hasAuthority('USER')")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping(value = "/get", produces = "application/json")
  @ResponseBody
  public ResponseEntity get(@RequestParam(value = "id") String id, @RequestParam(value = "nickname") String nickname) {
    return userService.getUser(id, nickname);
  }

  @PostMapping(value = "/logout", produces = "application/json")
  @ResponseBody
  public ResponseEntity logout(@RequestBody String id) throws IOException {
    // todo refactor to a jwt token in header
    Map map;
    map = new ObjectMapper().readValue(id, Map.class);
    String userId = map.get("id").toString();
    return userService.logout(userId);
  }

  @PostMapping(value = "/update", produces = "application/json")
  @ResponseBody
  public ResponseEntity signUp(@RequestBody @Valid User user,
                               BindingResult bindingResult) {
    return userService.userUpdate(user, bindingResult);
  }
}
