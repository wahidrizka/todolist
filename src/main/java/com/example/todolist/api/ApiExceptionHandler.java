package com.example.todolist.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraintViolation(
      ConstraintViolationException ex) {
    List<Map<String, String>> violations =
        ex.getConstraintViolations().stream().map(this::toViolationEntry).toList();

    Map<String, Object> body =
        Map.of(
            "error", "validation",
            "message", "Invalid request parameters",
            "violations", violations);

    return ResponseEntity.badRequest().body(body);
  }

  private Map<String, String> toViolationEntry(ConstraintViolation<?> violation) {
    // contoh propertyPath: "findAllPaged.page" atau "findAllPaged.size"
    return Map.of(
        "param", String.valueOf(violation.getPropertyPath()),
        "message", violation.getMessage());
  }
}
