package com.example.todolist.todo;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "Todos", description = "CRUD sederhana untuk Todo")
@Validated
public class TodoController {

  private final TodoService service;

  public TodoController(TodoService service) {
    this.service = service;
  }

  @PostMapping
  @Operation(summary = "Buat todo baru")
  public ResponseEntity<TodoResponse> create(@Valid @RequestBody CreateTodoRequest request) {
    TodoResponse created = service.create(request);
    return ResponseEntity.created(URI.create("/api/todos/" + created.id())).body(created);
  }

  @GetMapping
  @Operation(summary = "Ambil semua todo")
  public ResponseEntity<List<TodoResponse>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping(params = {"page", "size"})
  @Operation(summary = "Ambil todos dengan filter dan pagination")
  public ResponseEntity<PageResult<TodoResponse>> findAllPaged(
      @RequestParam(name = "q", required = false) String q,
      @RequestParam(name = "completed", required = false) Boolean completed,
      @PositiveOrZero @RequestParam(name = "page") Integer page,
      @Positive @Max(200) @RequestParam(name = "size") Integer size) {

    PageResult<TodoResponse> result = service.findAll(q, completed, page, size);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Ambil todo by id")
  public ResponseEntity<TodoResponse> findById(@PathVariable long id) {
    return service
        .findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Edit todo (partial update)")
  public ResponseEntity<TodoResponse> update(
      @PathVariable long id, @RequestBody UpdateTodoRequest request) {
    return service
        .update(id, request)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Hapus todo by id")
  public ResponseEntity<Void> delete(@PathVariable long id) {
    boolean removed = service.delete(id);
    return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
  }
}
