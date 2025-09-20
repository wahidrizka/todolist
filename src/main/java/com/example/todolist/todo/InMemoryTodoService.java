package com.example.todolist.todo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class InMemoryTodoService implements TodoService {

  private final List<TodoResponse> store = new ArrayList<>();
  private long sequence = 0L;

  @Override
  public synchronized List<TodoResponse> findAll() {
    return List.copyOf(store);
  }

  @Override
  public synchronized Optional<TodoResponse> findById(long id) {
    return store.stream().filter(t -> t.id() == id).findFirst();
  }

  @Override
  public synchronized TodoResponse create(CreateTodoRequest req) {
    long id = ++sequence;
    Instant now = Instant.now();
    TodoResponse created = new TodoResponse(id, req.title(), false, now, now);
    store.add(created);
    return created;
  }

  @Override
  public synchronized boolean delete(long id) {
    return store.removeIf(t -> t.id() == id);
  }
}
