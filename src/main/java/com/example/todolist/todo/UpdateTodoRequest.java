package com.example.todolist.todo;

/**
 * Payload update parsial. Field null = tidak diubah. title: jika diisi, akan di-trim dan harus
 * tidak kosong (dicek di service). completed: jika diisi, akan mengganti status selesai.
 */
public record UpdateTodoRequest(String title, Boolean completed) {}
