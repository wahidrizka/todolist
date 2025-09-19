package com.example.todolist.todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {
  TodoResponse create(CreateTodoRequest req);

  List<TodoResponse> list();

  Optional<TodoResponse> findById(long id);
}
