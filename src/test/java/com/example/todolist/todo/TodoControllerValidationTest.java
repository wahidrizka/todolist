package com.example.todolist.todo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Memastikan validasi @NotBlankIfPresent pada PATCH berjalan: title hadir tapi blank => 400 Bad
 * Request (Problem Details).
 */
@WebMvcTest(TodoController.class)
public class TodoControllerValidationTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private TodoService todoService;

  @Test
  @DisplayName("PATCH /api/todos/{id} dengan title blank => 400 ProblemDetails")
  void patch_withBlankTitle_shouldReturn400() throws Exception {
    // Arrange
    long id = 1L;

    String body = """
            { "title": "   " }
            """;

    // Act & Assert
    mockMvc
        .perform(patch("/api/todos/{id}", id).contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
  }
}
