package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.model.user.AuthenticationUser;
import com.denkarz.jcat.backend.model.user.User;
import com.denkarz.jcat.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api/auth")
public class LoginController {
  @Autowired
  private UserService userService;

  @PostMapping(value = "/sign_in", produces = "application/json")
  @ResponseBody
  ResponseEntity signIn(@RequestBody @Valid AuthenticationUser authUser,
                        BindingResult bindingResult) {
    return userService.userLogin(authUser, bindingResult);
  }

  @PostMapping(value = "/sign_up", produces = "application/json")
  @ResponseBody
  public ResponseEntity signUp(@RequestBody @Valid User user,
                               BindingResult bindingResult) {
    return userService.userRegistration(user, bindingResult);
  }

  @PostMapping(value = "/forgot_password", produces = "application/json")
  @ResponseBody
  ResponseEntity forgot(@RequestBody String email) throws IOException {
    Map map;
    map = new ObjectMapper().readValue(email, Map.class);
    String userEmail = map.get("email").toString();
    return userService.forgot(userEmail);
  }

  @PostMapping(value = "/registration_setup", produces = "application/json")
  @ResponseBody
  public ResponseEntity registrationSetup(@RequestBody @Valid AuthenticationUser authUser,
                                          BindingResult bindingResult) {
    return userService.registrationSetup(authUser, bindingResult);
  }

  @PostMapping(value = "/reset/{code}", produces = "application/json")
  @ResponseBody
  public ResponseEntity resetPasswordCode(@PathVariable String code, @RequestBody String password) throws IOException {
    Map map;
    map = new ObjectMapper().readValue(password, Map.class);
    String userPassword = map.get("password").toString();
    return userService.resetPasswordByCode(code, userPassword);
  }


  @GetMapping(value = "/activate/{code}", produces = "application/json")
  @ResponseBody
  public ResponseEntity activationByCode(@PathVariable String code) throws IOException {
    return userService.activationByCode(code);
  }
}
