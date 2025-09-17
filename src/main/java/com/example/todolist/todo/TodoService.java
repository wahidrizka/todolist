package com.example.todolist.todo;

import java.util.List;

public interface TodoService {
    TodoResponse create(CreateTodoRequest req);

    List<TodoResponse> list();
}
