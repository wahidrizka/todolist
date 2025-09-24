package com.example.todolist.todo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TodoV1Controller.class)
class TodoV1ControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private TodoService service;

  @Test
  void list_defaultPaging_shouldReturn200_withEmptyItems() throws Exception {
    // service dipanggil dengan default page=0,size=20 ketika param kosong
    when(service.findAll(null, null, 0, 20)).thenReturn(new PageResult<>(List.of(), 0));

    mvc.perform(get("/api/v1/todos").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.items").isArray())
        .andExpect(jsonPath("$.items.length()").value(0))
        .andExpect(jsonPath("$.total").value(0));
  }

  @Test
  void list_sizeEqualsZero_shouldReturn400_withProblemJson() throws Exception {
    mvc.perform(get("/api/v1/todos").param("size", "0"))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
        .andExpect(jsonPath("$.title").value("Request parameter validation failed"))
        .andExpect(jsonPath("$.status").value(400));
  }

  @Test
  void list_sizeIsNotANumber_shouldReturn400_withProblemJsonTypeMismatch() throws Exception {
    mvc.perform(get("/api/v1/todos").param("size", "abc"))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
        .andExpect(jsonPath("$.title").value("Parameter type mismatch"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.param").value("size"))
        .andExpect(jsonPath("$.expectedType").value("Integer"));
  }

  @Test
  void list_pageNegative_shouldReturn400_withProblemJson() throws Exception {
    mvc.perform(get("/api/v1/todos").param("page", "-1"))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
        .andExpect(jsonPath("$.title").value("Request parameter validation failed"))
        .andExpect(jsonPath("$.status").value(400));
  }
}
