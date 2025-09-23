package com.example.todolist.todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {
  /**
   * Versi sederhana untuk kebutuhan existing controller & test (tanpa filter/pagination).
   * Implementasi dev/prod cukup mengembalikan seluruh list terurut.
   */
  List<TodoResponse> findAll();

  PageResult<TodoResponse> findAll(String q, Boolean completed, Integer page, Integer size);

  Optional<TodoResponse> findById(long id);

  TodoResponse create(CreateTodoRequest request);

  boolean delete(long id);

  Optional<TodoResponse> update(long id, UpdateTodoRequest request);
}
