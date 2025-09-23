package com.example.todolist.todo;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/api/v1/todos")
@Tag(name = "Todos v1", description = "Versioned API dengan pagination default")
@Validated
public class TodoV1Controller {

  private final TodoService service;

  public TodoV1Controller(TodoService service) {
    this.service = service;
  }

  @GetMapping
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "OK"),
    @ApiResponse(
        responseCode = "400",
        description = "Validation error (Problem Details)",
        content =
            @Content(
                mediaType = "application/problem+json",
                schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
  })
  @Operation(summary = "Ambil todos (paged) dengan default page=0,size=20")
  public ResponseEntity<PageResult<TodoResponse>> findAllPagedDefault(
      @RequestParam(name = "q", required = false) String q,
      @RequestParam(name = "completed", required = false) Boolean completed,
      @RequestParam(name = "page", required = false, defaultValue = "0") @PositiveOrZero
          Integer page,
      @RequestParam(name = "size", required = false, defaultValue = "20") @Positive @Max(200)
          Integer size) {
    return ResponseEntity.ok(service.findAll(q, completed, page, size));
  }
}
