package com.example.todolist.todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {
  /**
   * Versi sederhana untuk kebutuhan existing controller & test (tanpa filter/pagination).
   * Implementasi dev/prod cukup mengembalikan seluruh list terurut.
   */
  List<TodoResponse> findAll();

  /**
   * Versi lengkap dengan filter & pagination. Untuk sementara diberi default agar implementasi lama
   * tetap kompatibel. Langkah berikutnya kita akan implementasikan logic sebenarnya.
   */
  default PageResult<TodoResponse> findAll(
      String q, Boolean completed, Integer page, Integer size) {
    List<TodoResponse> items = findAll();
    return new PageResult<>(items, items.size());
  }

  Optional<TodoResponse> findById(long id);

  TodoResponse create(CreateTodoRequest request);

  boolean delete(long id);

  Optional<TodoResponse> update(long id, UpdateTodoRequest request);
}
