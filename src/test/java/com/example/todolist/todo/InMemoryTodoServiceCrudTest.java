package com.example.todolist.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit test murni untuk InMemoryTodoService (tanpa Spring context). Fokus: create, findAll,
 * findById, update, delete.
 */
class InMemoryTodoServiceCrudTest {

  @Test
  @DisplayName("create() dan findAll() mengembalikan item yang disimpan")
  void create_and_findAll_shouldReturnItems() {
    InMemoryTodoService svc = new InMemoryTodoService();

    TodoResponse a = svc.create(new CreateTodoRequest("Alpha"));
    TodoResponse b = svc.create(new CreateTodoRequest("Beta"));

    List<TodoResponse> all = svc.findAll();
    assertEquals(2, all.size());

    // Pastikan kedua judul ada, tanpa asumsi urutan
    assertTrue(all.stream().anyMatch(t -> t.id().equals(a.id()) && t.title().equals("Alpha")));
    assertTrue(all.stream().anyMatch(t -> t.id().equals(b.id()) && t.title().equals("Beta")));

    // findById bekerja
    assertTrue(svc.findById(a.id()).isPresent());
    assertEquals("Alpha", svc.findById(a.id()).orElseThrow().title());
  }

  @Test
  @DisplayName("update() memperbarui title/completed dan tetap mengembalikan Optional non-empty")
  void update_shouldModifyFields() {
    InMemoryTodoService svc = new InMemoryTodoService();

    TodoResponse saved = svc.create(new CreateTodoRequest("Todo"));
    Optional<TodoResponse> updatedOpt =
        svc.update(saved.id(), new UpdateTodoRequest("Todo Updated", true));

    assertTrue(updatedOpt.isPresent());
    TodoResponse updated = updatedOpt.orElseThrow();

    assertEquals(saved.id(), updated.id()); // id tetap sama
    assertEquals("Todo Updated", updated.title()); // title berubah
    assertTrue(updated.completed()); // completed jadi true

    // createdAt boleh tetap/berubah sesuai implementasi; yang penting updated punya nilai (tidak
    // null)
    assertNotNull(updated.createdAt());
    assertNotNull(updated.updatedAt());
  }

  @Test
  @DisplayName("delete() menghapus item dan findById() mengembalikan empty")
  void delete_shouldRemoveItem() {
    InMemoryTodoService svc = new InMemoryTodoService();

    TodoResponse saved = svc.create(new CreateTodoRequest("To be deleted"));
    assertTrue(svc.findById(saved.id()).isPresent());

    boolean removed = svc.delete(saved.id());
    assertTrue(removed);
    assertTrue(svc.findById(saved.id()).isEmpty());
  }
}
