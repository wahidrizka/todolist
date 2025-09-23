package com.example.todolist.api;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ProblemDetailsExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    // RFC 7807 fields
    problemDetail.setType(URI.create("about:blank"));
    problemDetail.setTitle("Request validation failed");
    problemDetail.setDetail("One or more fields are invalid");
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    // Extension members: kumpulkan error per-field
    Map<String, List<String>> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.groupingBy(
                    FieldError::getField,
                    Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));

    problemDetail.setProperty("errors", errors);
    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ProblemDetail> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex, HttpServletRequest request) {

    ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problemDetail.setType(URI.create("about:blank"));
    problemDetail.setTitle("Malformed JSON request");
    problemDetail.setDetail("Request body is not readable or is malformed JSON.");
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    return ResponseEntity.badRequest().body(problemDetail);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

    ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    pd.setType(URI.create("about:blank"));
    pd.setTitle("Parameter type mismatch");
    pd.setDetail("One or more request parameters have an invalid type.");
    pd.setInstance(URI.create(request.getRequestURI()));

    // Extension members untuk debugging ringan di klien
    pd.setProperty("param", ex.getName());
    pd.setProperty(
        "expectedType", ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : null);
    pd.setProperty("value", ex.getValue());

    return ResponseEntity.badRequest().body(pd);
  }
}
