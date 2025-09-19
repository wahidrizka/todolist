package com.example.todolist.todo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "Todos", description = "CRUD sederhana untuk Todo")
public class TodoController {

    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Buat todo baru")
    public ResponseEntity<TodoResponse> create(@Valid @RequestBody CreateTodoRequest req) {
        TodoResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/api/todos/" + created.id())).body(created);
    }

    @GetMapping
    @Operation(summary = "Daftar semua todo")
    public List<TodoResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Todo berdasarkan id")
    public ResponseEntity<TodoResponse> getById(@PathVariable long id) {
        return service
            .findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
