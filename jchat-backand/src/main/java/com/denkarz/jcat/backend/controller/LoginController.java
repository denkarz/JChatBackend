package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.model.user.AuthenticationUser;
import com.denkarz.jcat.backend.model.user.User;
import com.denkarz.jcat.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("api/v1/auth")
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

//  @RequestMapping(value = "/forget", method = RequestMethod.GET, produces = "application/json")
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

  /**
   * Проверяем наличие пользоваеля. и разрешаем дальнейшую регистрацию
   *
   * @param email user's mail
   * @return status
   */
  @GetMapping(value = "/has_mail", produces = "application/json")
  @ResponseBody
  public ResponseEntity hasMail(@RequestParam(value = "email") String email) {
    if (userService.hasMail(email)) {
      return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
    return ResponseEntity.status(HttpStatus.CONFLICT).body("user_exists");
  }

  // Метод для отладки
  @RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  Iterable<User> hello() {
    return userService.testRequest();
  }

}
