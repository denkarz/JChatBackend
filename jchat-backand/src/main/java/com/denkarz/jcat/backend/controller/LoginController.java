package com.denkarz.jcat.backend.controller;

import com.denkarz.jcat.backend.dto.AuthenticationRequestDto;
import com.denkarz.jcat.backend.model.User;
import com.denkarz.jcat.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
public class LoginController {
  @Autowired
  private UserService userService;

  @RequestMapping(value = "/sign_in", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  ResponseEntity signIn(@RequestBody AuthenticationRequestDto authUser) {
    return userService.userLogin(authUser);
  }

  /**
   * Проверяем наличие пользоваеля. и разрешаем дальнейшую регистрацию
   *
   * @param email user's mail
   * @return status
   */
  @RequestMapping(value = "/has_mail", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public ResponseEntity hasMail(@RequestParam(value = "email") String email) {
    if (userService.hasMail(email)) {
      return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
    return ResponseEntity.status(HttpStatus.CONFLICT).body("user_not_exists");
  }


  @RequestMapping(value = "/sign_up", method = RequestMethod.POST, produces = "application/json")
  @ResponseBody
  public ResponseEntity signUp(@RequestBody User user) {
    return userService.userRegistration(user);
  }

//  @RequestMapping(value = "/forget", method = RequestMethod.GET, produces = "application/json")
//  @ResponseBody
//  User forget() {
//    return null;
//  }

  // Метод для отладки
  @RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  Iterable<User> hello() {
    return userService.testRequest();
  }

}
