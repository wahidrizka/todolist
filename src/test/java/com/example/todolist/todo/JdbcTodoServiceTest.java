package com.example.todolist.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

class JdbcTodoServiceTest {

  @Test
  void create_shoudlTrimTitle_andPassTrimmedToJdbc() {
    // arrange
    JdbcTemplate jdbc = mock(JdbcTemplate.class);
    JdbcTodoService service = new JdbcTodoService(jdbc);

    CreateTodoRequest req = new CreateTodoRequest("   Buy milk   ");
    String expectedSql =
        "insert into todos(title) values (?) "
            + "returning id, title, completed, created_at, updated_at";

    Instant now = Instant.parse("2025-01-01T00:00:00Z");
    TodoResponse mocked = new TodoResponse(1L, "Buy milk", false, now, now);

    when(jdbc.queryForObject(eq(expectedSql), any(RowMapper.class), any())).thenReturn(mocked);

    // act
    TodoResponse res = service.create(req);

    // assert
    ArgumentCaptor<Object> argCaptor = ArgumentCaptor.forClass(Object.class);
    verify(jdbc).queryForObject(eq(expectedSql), any(RowMapper.class), argCaptor.capture());
    assertEquals("Buy milk", String.valueOf(argCaptor.getValue()), "title harus sudah di-trim");
  }
}
