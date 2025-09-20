package com.example.todolist.todo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * WebMvc tests untuk {@link TodoController}. Pola: Arrange – Act – Assert. Semua akses ke storage
 * dimock via {@link TodoService}.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private TodoService todoService;

  // === Helpers ===

  private static TodoResponse todo(long id, String title, boolean completed) {
    Instant now = Instant.parse("2025-01-01T00:00:00Z");
    return new TodoResponse(id, title, completed, now, now);
  }

  // === CREATE ===

  @Test
  @DisplayName("POST /api/todos -> 201 Created + Location header saat input valid")
  void create_shouldReturn201_andLocation_whenValid() throws Exception {
    when(todoService.create(any())).thenReturn(todo(1L, "Buy milk", false));

    mockMvc
        .perform(
            post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Buy milk\"}"))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/todos/1"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Buy milk"));

    verify(todoService).create(new CreateTodoRequest("Buy milk"));
  }

  @Test
  @DisplayName("POST /api/todos -> 400 Bad Request saat title kosong")
  void create_shouldReturn400_whenTitleBlank() throws Exception {
    // Tidak perlu stub service; gagal di layer validation (@NotBlank)
    mockMvc
        .perform(
            post("/api/todos").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"\"}"))
        .andExpect(status().isBadRequest());
  }

  // === LIST ===

  @Test
  @DisplayName("GET /api/todos -> 200 OK + array todos")
  void list_shouldReturn200_andArray() throws Exception {
    when(todoService.findAll()).thenReturn(List.of(todo(1, "A", false), todo(2, "B", false)));

    mockMvc
        .perform(get("/api/todos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].title").value("A"))
        .andExpect(jsonPath("$[1].id").value(2))
        .andExpect(jsonPath("$[1].title").value("B"));
  }

  // === GET BY ID ===

  @Test
  @DisplayName("GET /api/todos/{id} -> 200 OK saat data ada")
  void getById_shouldReturn200_whenExists() throws Exception {
    when(todoService.findById(42L)).thenReturn(Optional.of(todo(42, "Answer", false)));

    mockMvc
        .perform(get("/api/todos/{id}", 42))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(42))
        .andExpect(jsonPath("$.title").value("Answer"));
  }

  @Test
  @DisplayName("GET /api/todos/{id} -> 404 Not Found saat data tidak ada")
  void getById_shouldReturn404_whenMissing() throws Exception {
    when(todoService.findById(999L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/todos/{id}", 999)).andExpect(status().isNotFound());
  }

  // === DELETE ===

  @Test
  @DisplayName("DELETE /api/todos/{id} -> 204 No Content saat data ada")
  void delete_shouldReturn204_whenExists() throws Exception {
    when(todoService.delete(42L)).thenReturn(true);

    mockMvc.perform(delete("/api/todos/{id}", 42)).andExpect(status().isNoContent());

    verify(todoService).delete(42L);
  }

  @Test
  @DisplayName("DELETE /api/todos/{id} -> 404 Not Found saat data tidak ada")
  void delete_shouldReturn404_whenMissing() throws Exception {
    when(todoService.delete(999L)).thenReturn(false);

    mockMvc.perform(delete("/api/todos/{id}", 999)).andExpect(status().isNotFound());

    verify(todoService).delete(999L);
  }
}
