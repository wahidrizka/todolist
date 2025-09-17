package com.example.todolist.todo;

import java.time.Instant;

/**
 * Bentuk respons standar untuk Todo.
 */
public record TodoResponse(
        Long id,
        String title,
        boolean completed,
        Instant createdAt,
        Instant updatedAt
) {
}
