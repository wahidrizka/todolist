package com.example.todolist.todo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Memastikan parameter size > 200 pada /api/v1/todos menghasilkan Problem Details (400) dengan
 * field "violations".
 */
@WebMvcTest(TodoV1Controller.class)
class TodoV1ControllerProblemDetailsTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private TodoService todoService;

  @Test
  void get_withTooLargeSize_shouldReturnProblemDetails() throws Exception {
    mockMvc
        .perform(get("/api/v1/todos").param("size", "500"))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
        // pastikan struktur problem details membawa daftar violations
        .andExpect(jsonPath("$.violations").exists());
  }
}
