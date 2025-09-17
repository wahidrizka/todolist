package com.example.todolist.todo;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryTodoService implements TodoService {
    private final AtomicLong idSeq = new AtomicLong(0);
    private final ConcurrentMap<Long, Todo> store = new ConcurrentHashMap<>();

    @Override
    public TodoResponse create(CreateTodoRequest req) {
        Objects.requireNonNull(req, "request tidak boleh null");
        String title = req.title() == null ? "" : req.title().trim();
        if (title.isEmpty()) {
            //  validasi dasar  di service (nanti controller juga pakai @Valid)
            throw new IllegalArgumentException("title wajib diisi");
        }
        long id = idSeq.incrementAndGet();
        Instant now = Instant.now();
        Todo t = new Todo(id, title, false, now, now);
        store.put(id, t);
        return toResponse(t);
    }

    @Override
    public List<TodoResponse> list() {
        return store.values().stream()
                .sorted(Comparator.comparing(Todo::id)) // urut berdasarkan id
                .map(this::toResponse)
                .toList();
    }

    private TodoResponse toResponse(Todo t) {
        return new TodoResponse(t.id(), t.title(), t.completed(), t.createdAt(), t.updatedAt());
    }

    private record Todo(Long id, String title, boolean completed, Instant createdAt, Instant updatedAt) {
    }
}
