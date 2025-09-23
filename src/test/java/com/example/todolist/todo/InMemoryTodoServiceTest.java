package com.example.todolist.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryTodoServiceTest {

  private InMemoryTodoService service;

  @BeforeEach
  void setUp() {
    service = new InMemoryTodoService();
  }

  @Test
  void create_shouldTrimTitle_andSetDefaults() {
    // arrange
    CreateTodoRequest req = new CreateTodoRequest("   Buy milk   ");

    // act
    TodoResponse res = service.create(req);

    // assert
    assertEquals("Buy milk", res.title(), "title harus di-trim");
    assertFalse(res.completed(), "completed default harus false");
    assertNotNull(res.createdAt(), "createdAt harus terisi");
    assertNotNull(res.updatedAt(), "updatedAt harus terisi");
  }
}
