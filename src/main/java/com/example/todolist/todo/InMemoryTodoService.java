package com.example.todolist.todo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!prod")
public class InMemoryTodoService implements TodoService {

  private final ConcurrentHashMap<Long, TodoResponse> store = new ConcurrentHashMap<>();
  private final AtomicLong seq = new AtomicLong(0);

  @Override
  public List<TodoResponse> findAll() {
    List<TodoResponse> list = new ArrayList<>(store.values());
    list.sort(Comparator.comparing(TodoResponse::id));
    return list;
  }

  @Override
  public Optional<TodoResponse> findById(long id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public TodoResponse create(CreateTodoRequest req) {
    long id = seq.incrementAndGet();
    Instant now = Instant.now();
    TodoResponse created = new TodoResponse(id, req.title(), false, now, now);
    store.put(id, created);
    return created;
  }

  @Override
  public boolean delete(long id) {
    return store.remove(id) != null;
  }

  @Override
  public Optional<TodoResponse> update(long id, UpdateTodoRequest request) {
    return Optional.ofNullable(
        store.computeIfPresent(
            id,
            (k, oldVal) -> {
              String title = oldVal.title();
              if (request.title() != null) {
                String t = request.title().trim();
                if (!t.isEmpty()) title = t;
              }
              boolean completed =
                  (request.completed() != null) ? request.completed() : oldVal.completed();
              Instant now = Instant.now();
              return new TodoResponse(k, title, completed, oldVal.createdAt(), now);
            }));
  }

  @Override
  public PageResult<TodoResponse> findAll(String q, Boolean completed, Integer page, Integer size) {
    String normalizedQuery = (q == null) ? null : q.trim().toLowerCase();

    List<TodoResponse> filtered =
        new ArrayList<>(store.values())
            .stream()
                .filter(
                    t ->
                        normalizedQuery == null
                            || t.title().toLowerCase().contains(normalizedQuery))
                .filter(t -> completed == null || t.completed() == completed)
                .sorted(Comparator.comparing(TodoResponse::id))
                .toList();

    int total = filtered.size();
    int fromIndex = Math.min(page * size, total);
    int toIndex = Math.min(fromIndex + size, total);

    List<TodoResponse> items = filtered.subList(fromIndex, toIndex);
    return new PageResult<>(items, total);
  }
}
