package com.example.todolist.api;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health", description = "Health check untuk service")
public class HealthController {
  private final String appName;

  public HealthController(@Value("${spring.application.name:todolist}") String appName) {
    this.appName = appName;
  }

  @GetMapping
  @Operation(summary = "Cek status service")
  public ResponseEntity<HealthResponse> get() {
    return ResponseEntity.ok(new HealthResponse("UP", appName, Instant.now()));
  }

  public record HealthResponse(String status, String app, Instant now) {}
}
