package com.example.todolist.todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {
  List<TodoResponse> findAll();

  Optional<TodoResponse> findById(long id);

  TodoResponse create(CreateTodoRequest request);

  boolean delete(long id);

  Optional<TodoResponse> update(long id, UpdateTodoRequest request);
}
