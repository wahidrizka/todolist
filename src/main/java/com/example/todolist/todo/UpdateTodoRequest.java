package com.example.todolist.todo;

import com.example.todolist.validation.NotBlankIfPresent;

import jakarta.validation.constraints.Size;

/**
 * Payload update parsial. Field null = tidak diubah. title: jika diisi, akan di-trim dan harus
 * tidak kosong (dicek di service). completed: jika diisi, akan mengganti status selesai.
 */
public record UpdateTodoRequest(
    @Size(max = 200, message = "title maksimal 200 karakter") @NotBlankIfPresent String title,
    Boolean completed) {}
