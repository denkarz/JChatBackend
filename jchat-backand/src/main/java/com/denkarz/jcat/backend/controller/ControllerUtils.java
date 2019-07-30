package com.denkarz.jcat.backend.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {
  public static String getErrorsLog(BindingResult bindingResult) {
    Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(FieldError::getField,
            FieldError::getDefaultMessage);
    Map<String, String> map = bindingResult.getFieldErrors().stream().collect(collector);
    return map.keySet().stream()
            .map(key -> key + "=" + map.get(key))
            .collect(Collectors.joining(", ", "{", "}"));
  }

  public static Map<String, String> getErrorsResponse(BindingResult bindingResult) {
    Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(fieldError -> fieldError.getField() + "Error",
            fieldError -> ControllerUtils.parseErr(Objects.requireNonNull(fieldError.getDefaultMessage())));

    return bindingResult.getFieldErrors().stream().collect(collector);
  }

  private static String parseErr(String errorMessage) {
    String prettifyErrorMessage = errorMessage.toLowerCase().replaceAll("\\s", "_");
    Pattern p = Pattern.compile("^([\\w-]+\\s?){1,5}");
    Matcher m = p.matcher(prettifyErrorMessage);
    if (m.find()) {
      prettifyErrorMessage = m.group(1);
    }
    return prettifyErrorMessage;
  }
}
