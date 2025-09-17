package com.example.todolist.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload untuk membuat Todo baru.
 */
public record CreateTodoRequest(
        @NotBlank(message = "title wajib diisi")
        @Size(max = 200, message = "title maksimal 200 karakter")
        String title
) {
}
