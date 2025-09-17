package com.example.todolist.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TodoController.class)
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TodoService todoService;

    @Test
    @DisplayName("POST /api/todos -> 201 Created")
    void create_ok() throws Exception {
        CreateTodoRequest req = new CreateTodoRequest("Belajar Spring");
        TodoResponse resp = new TodoResponse(
                1L, "Belajar Spring", false,
                Instant.parse("2025-01-01T00:00:00Z"),
                Instant.parse("2025-01-01T00:00:00Z")
        );
        when(todoService.create(any(CreateTodoRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/api/todos/")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Belajar Spring"))
                .andExpect(jsonPath("$.completed").value(false));

        verify(todoService).create(any(CreateTodoRequest.class));
    }

    @Test
    @DisplayName("POST /api/todos dengan title kosong -> 400 Bad Request (validasi)")
    void create_blankTitle_400() throws Exception {
        String badJson = "{\"title\":\"\"}";

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badJson))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(todoService);
    }

    @Test
    @DisplayName("GET /api/todos -> 200 OK dengan list")
    void list_ok() throws Exception {
        List<TodoResponse> data = List.of(
                new TodoResponse(1L, "A", false,
                        Instant.parse("2025-01-01T00:00:00Z"),
                        Instant.parse("2025-01-01T00:00:00Z")),
                new TodoResponse(2L, "B", true,
                        Instant.parse("2025-01-02T00:00:00Z"),
                        Instant.parse("2025-01-02T00:00:00Z"))
        );
        when(todoService.list()).thenReturn(data);

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("A"))
                .andExpect(jsonPath("$[1].completed").value(true));

        verify(todoService).list();
    }
}
