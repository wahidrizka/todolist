package com.example.todolist.todo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Memastikan query param 'completed' yang tidak valid memicu Problem Details (400). */
@WebMvcTest(TodoV1Controller.class)
class TodoV1ControllerTypeMismatchTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private TodoService todoService;

  void get_withInvalidCompletedParam_shouldReturnProblemDetails() throws Exception {
    mockMvc
        .perform(get("/api/v1/todos").param("completed", "maybe"))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
  }
}
