package com.example.todolist.api;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ProblemDetail> handleConstraintViolation(
      ConstraintViolationException ex, HttpServletRequest request) {

    // Kumpulkan pelanggaran per-parameter (memakai helper yang sudah ada)
    List<Map<String, String>> violations =
        ex.getConstraintViolations().stream().map(this::toViolationEntry).toList();

    // Bangun Problem Details (RFC 7807)
    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setType(URI.create("about:blank"));
    problemDetail.setTitle("Request parameter validation failed");
    problemDetail.setDetail("One or more request paramteres are invalid.");
    problemDetail.setInstance(URI.create(request.getRequestURI().toString()));

    // Extension member: daftar pelanggaran
    problemDetail.setProperty("violations", violations);

    return ResponseEntity.badRequest().body(problemDetail);
  }

  private Map<String, String> toViolationEntry(ConstraintViolation<?> violation) {
    // contoh propertyPath: "findAllPaged.page" atau "findAllPaged.size"
    return Map.of(
        "param", String.valueOf(violation.getPropertyPath()),
        "message", violation.getMessage());
  }
}
