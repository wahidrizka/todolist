package com.example.todolist.todo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** Slice test untuk {@link TodoController} (MVC only). Storage dimock via {@link TodoService}. */
@WebMvcTest(TodoController.class)
class TodoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TodoService todoService;

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

  @Test
  @DisplayName(
      "GET /api/todos?page&size -> 200 OK + objek PageResult dengan items & total; meneruskan param ke service")
  void list_paged_shouldReturn200_andPageResult() throws Exception {
    when(todoService.findAll(eq("milk"), eq(true), eq(0), eq(2)))
        .thenReturn(new PageResult<>(List.of(todo(1, "A", true), todo(2, "B", true)), 5));

    mockMvc
        .perform(
            get("/api/todos")
                .param("q", "milk")
                .param("completed", "true")
                .param("page", "0")
                .param("size", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.items.length()").value(2))
        .andExpect(jsonPath("$.items[0].id").value(1))
        .andExpect(jsonPath("$.items[0].title").value("A"))
        .andExpect(jsonPath("$.total").value(5));

    verify(todoService).findAll(eq("milk"), eq(true), eq(0), eq(2));
  }

  @Test
  @DisplayName("GET /api/todos?page=-1&size=10 -> 400 Bad Request (page negatif)")
  void list_paged_shouldReturn400_whenPageNegative() throws Exception {
    mockMvc
        .perform(get("/api/todos").param("page", "-1").param("size", "10"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/todos?page=0&size=0 -> 400 Bad Request (size 0)")
  void list_paged_shouldReturn400_whenSizeZero() throws Exception {
    mockMvc
        .perform(get("/api/todos").param("page", "0").param("size", "0"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/todos?page=0&size=1000 -> 400 Bad Request (size > 200)")
  void list_paged_shouldReturn400_whenSizeTooLarge() throws Exception {
    mockMvc
        .perform(get("/api/todos").param("page", "0").param("size", "1000"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/todos?page=0&size=0 -> 400 dengan body error dari handler")
  void list_paged_shouldReturn400_withValidationBody() throws Exception {
    mockMvc
        .perform(get("/api/todos").param("page", "0").param("size", "0"))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
        .andExpect(jsonPath("$.title").value("Request parameter validation failed"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.violations[0].message").value("must be greater than 0"));
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

  // === PATCH (EDIT) ===
  @Test
  @DisplayName("PATCH /api/todos/{id} -> 200 OK saat berhasil update title & completed")
  void patch_shouldReturn200_whenUpdated() throws Exception {
    when(todoService.update(eq(42L), any())).thenReturn(Optional.of(todo(42L, "Edited", true)));

    mockMvc
        .perform(
            patch("/api/todos/{id}", 42L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Edited\",\"completed\":true}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(42))
        .andExpect(jsonPath("$.title").value("Edited"))
        .andExpect(jsonPath("$.completed").value(true));

    verify(todoService).update(eq(42L), any());
  }

  @Test
  @DisplayName("PATCH /api/todos/{id} -> 200 OK saat hanya toggle completed")
  void patch_shouldReturn200_whenToggleCompletedOnly() throws Exception {
    when(todoService.update(eq(7L), any())).thenReturn(Optional.of(todo(7L, "Keep", true)));

    mockMvc
        .perform(
            patch("/api/todos/{id}", 7L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"completed\":true}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(7))
        .andExpect(jsonPath("$.title").value("Keep"))
        .andExpect(jsonPath("$.completed").value(true));

    verify(todoService).update(eq(7L), any());
  }

  @Test
  @DisplayName("PATCH /api/todos/{id} -> 404 Not Found saat id tidak ada")
  void patch_shouldReturn404_whenMissing() throws Exception {
    when(todoService.update(eq(999L), any())).thenReturn(Optional.empty());

    mockMvc
        .perform(
            patch("/api/todos/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"x\"}"))
        .andExpect(status().isNotFound());

    verify(todoService).update(eq(999L), any());
  }
}
