package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.model.user.AuthenticationUser;
import com.denkarz.jcat.backend.model.user.User;
import com.denkarz.jcat.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

  @CrossOrigin
  @PostMapping(value = "/sign_up", produces = "application/json")
  @ResponseBody
  public ResponseEntity signUp(@RequestBody @Valid User user,
                               BindingResult bindingResult) {
    return userService.userRegistration(user, bindingResult);
  }

//  @RequestMapping(value = "/forgot_password", method = RequestMethod.GET, produces = "application/json")
//  @ResponseBody
//  User forget() {
//    return null;
//  }

  @PostMapping(value = "/registration_setup", produces = "application/json")
  @ResponseBody
  public ResponseEntity registrationSetup(@RequestBody @Valid AuthenticationUser authUser,
                                          BindingResult bindingResult) {
    return userService.registrationSetup(authUser, bindingResult);
  }
}
