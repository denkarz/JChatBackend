package com.denkarz.jcat.backend.service;

import com.denkarz.jcat.backend.controller.ControllerUtils;
import com.denkarz.jcat.backend.model.user.AuthenticationUser;
import com.denkarz.jcat.backend.model.user.Role;
import com.denkarz.jcat.backend.model.user.User;
import com.denkarz.jcat.backend.repository.UserRepository;
import com.denkarz.jcat.backend.security.jwt.JwtGenerator;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private BCryptPasswordEncoder passwordEncoder;
  @Autowired
  private JwtGenerator jwtGenerator;
  @Autowired
  private NotificationService notificationService;

  @Override
  public User loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> userFromDb = userRepository.findByEmail(email);
    return userFromDb.orElse(null);
  }

  public ResponseEntity userRegistration(User user, BindingResult bindingResult) {
    Optional<User> userFromDb = userRepository.findByIdOrNick(user.getId(), user.getNickname());
    if (userFromDb.isPresent()) {
      // ToDo: add logger for error
      JSONObject json = new JSONObject();
      json.put("emailError", "email_in_use");
      return ResponseEntity.status(HttpStatus.CONFLICT).body(json);
    }
    user.setActive(true);
    user.setRoles(Collections.singleton(Role.USER));
    if (bindingResult.hasErrors()) {
      log.warn(ControllerUtils.getErrorsLog(bindingResult));
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ControllerUtils.getErrorsResponse(bindingResult));
    }
    user.setActivationCode(UUID.randomUUID().toString());
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    User savedUser = userRepository.save(user);
    // todo: refactor of duplicating id into nickname
    savedUser.setNickname(savedUser.getId());
    savedUser = userRepository.save(savedUser);

//todo replase email hardcode
    String message = String.format("Hello %s %s! \n" +
            "Welcome to JTalki. Please, visit next link: http://localhost:8080/activate/%s",
            savedUser.getFirstName(), savedUser.getLastName(), savedUser.getActivationCode());
    notificationService.sendMailNotification(savedUser.getEmail(), "Activation Code", message);

    String jwt = jwtGenerator.generate(savedUser);
    log.info("Add new user to database: {}", savedUser);
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add("Authorisation", "Token " + jwt);
    return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(null);
  }


  public ResponseEntity userUpdate(User user, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn(ControllerUtils.getErrorsLog(bindingResult));
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ControllerUtils.getErrorsResponse(bindingResult));
    }
    Optional<User> userFromDb = userRepository.findByEmail(user.getEmail());
    if (userFromDb.isPresent()) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      User savedUser = userRepository.save(user);
      String jwt = jwtGenerator.generate(savedUser);
      log.info("Add new user to database: {}", savedUser);
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.add("Authorisation", "Token " + jwt);
      return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(null);
    }
    // ToDo: add logger for error
    JSONObject json = new JSONObject();
    json.put("emailError", "email_in_use");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(json);
  }

  public boolean hasMail(String email) {
    email = email.trim().toLowerCase();
    if (!email.isBlank()) {
      Optional<User> userFromDb = userRepository.findByEmail(email);
      return userFromDb.isEmpty();
    }
    return false;
  }

  public ResponseEntity userLogin(AuthenticationUser authUser, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn(ControllerUtils.getErrorsLog(bindingResult));
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ControllerUtils.getErrorsResponse(bindingResult));
    }
    authUser.setEmail(authUser.getEmail().trim().toLowerCase());
    Optional<User> userFromDb = userRepository.findByEmail(authUser.getEmail());
    if (userFromDb.isPresent() && passwordEncoder.matches(authUser.getPassword(), userFromDb.get().getPassword())) {
      // ToDo: add logger for error
      User user = userFromDb.get();
      user.setActive(true);
      String jwt = jwtGenerator.generate(user);
      userRepository.save(user);
      log.info("Login as {}", userFromDb.get());
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.add("Authorisation", "Token " + jwt);
      return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(null);
    }
    JSONObject json = new JSONObject();
    json.put("emailError", "user_not_found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json);
  }

  public Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }

  public ResponseEntity registrationSetup(AuthenticationUser authUser, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn(ControllerUtils.getErrorsLog(bindingResult));
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ControllerUtils.getErrorsResponse(bindingResult));
    }
    authUser.setEmail(authUser.getEmail().trim().toLowerCase());
    if (this.hasMail(authUser.getEmail())) {
      return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
    JSONObject json = new JSONObject();
    json.put("emailError", "email_in_use");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(json);
  }

  public ResponseEntity getUser(String id, String nickname) {
    Optional<User> userFromDb = userRepository.findByIdOrNick(id, nickname);
    if (userFromDb.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(userFromDb);
    }
    JSONObject json = new JSONObject();
    json.put("emailError", "user_not_found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json);
  }

  public ResponseEntity logout(String id) {
    Optional<User> userFromDb = userRepository.findById(id);
    if (userFromDb.isPresent()) {
      User user = userFromDb.get();
      user.setActive(false);
      userRepository.save(user);
      return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
    JSONObject json = new JSONObject();
    json.put("emailError", "user_not_found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json);
  }

  public ResponseEntity updateRoles(JSONObject json) {
    String id = json.get("id").toString();
    String jsonRoles = json.get("roles").toString();
    String[] roles = jsonRoles
            .replaceAll("\\[", "")
            .replaceAll("\\]", "")
            .replaceAll("\\,", "")
            .split(" ");

    Optional<User> userFromDb = userRepository.findById(id);
    if (userFromDb.isPresent()) {
      User user = userFromDb.get();
      try {
        user.getRoles().clear();
        for (String key : roles) {

          user.getRoles().add(Role.valueOf(key));
        }
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(user);
      } catch (IllegalArgumentException ex) {
        System.out.println("no roles exists");
        JSONObject jsonError = new JSONObject();
        jsonError.put("rolesError", "empty_user_roles");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonError);
      }

    }
    JSONObject jsonError = new JSONObject();
    jsonError.put("emailError", "user_not_found");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonError);
  }

  public ResponseEntity activationByCode(String code) {
    Optional<User> userFromDb = userRepository.findByActivationCode(code);
    if (userFromDb.isPresent()) {
      User user = userFromDb.get();
      user.setActivationCode(null);
      String jwt = jwtGenerator.generate(user);
      userRepository.save(user);
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.add("Authorisation", "Token " + jwt);
      return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(null);
    }
    JSONObject json = new JSONObject();
    json.put("emailError", "user_not_found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json);
  }

  public ResponseEntity forgot(String userEmail) {
    System.out.println(userEmail);
    Optional<User> userFromDb = userRepository.findByEmail(userEmail);
    if (userFromDb.isPresent()) {
      User user = userFromDb.get();
      user.setResetPassword(UUID.randomUUID().toString());
      userRepository.save(user);

//todo replase email hardcode
      String message = String.format("Hello %s %s! \n" +
                      "Welcome to JTalki. Please, visit next link: http://localhost:8080/reset_password/%s",
              user.getFirstName(), user.getLastName(), user.getResetPassword());
      notificationService.sendMailNotification(user.getEmail(), "Reset Password", message);
      return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
    JSONObject json = new JSONObject();
    json.put("emailError", "user_not_found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json);
  }

  public ResponseEntity resetPasswordByCode(String code, String userPassword) {
    Optional<User> userFromDb = userRepository.findByResetPasswordCode(code);
    if (userFromDb.isPresent()) {
      User user = userFromDb.get();
      user.setPassword(passwordEncoder.encode(userPassword));
      user.setActive(true);
      user.setResetPassword(null);
      String jwt = jwtGenerator.generate(user);
      userRepository.save(user);
      HttpHeaders responseHeaders = new HttpHeaders();
      responseHeaders.add("Authorisation", "Token " + jwt);
      return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(null);
    }
    JSONObject json = new JSONObject();
    json.put("emailError", "user_not_found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(json);
  }
}
