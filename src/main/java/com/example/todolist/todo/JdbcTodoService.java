package com.example.todolist.todo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
@Profile("prod") // aktif hanya saat profile prod
class JdbcTodoService implements TodoService {

  private static final RowMapper<TodoResponse> M =
      (rs, n) ->
          new TodoResponse(
              rs.getLong("id"),
              rs.getString("title"),
              rs.getBoolean("completed"),
              ts(rs.getTimestamp("created_at")),
              ts(rs.getTimestamp("updated_at")));
  private final JdbcTemplate jdbc;

  JdbcTodoService(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  private static Instant ts(Timestamp t) {
    return (t == null) ? null : t.toInstant();
  }

  @Override
  public List<TodoResponse> findAll() {
    return jdbc.query(
        "select id, title, completed, created_at, updated_at from todos order by id", M);
  }

  @Override
  public Optional<TodoResponse> findById(long id) {
    return jdbc
        .query("select id, title, completed, created_at, updated_at from todos where id = ?", M, id)
        .stream()
        .findFirst();
  }

  @Override
  public TodoResponse create(CreateTodoRequest req) {
    // gunakan DEFAULT untuk kolom yg ada default (completed=false, created_at, updated_at=now())
    return jdbc.queryForObject(
        "insert into todos(title) values (?) "
            + "returning id, title, completed, created_at, updated_at",
        M,
        req.title());
  }

  @Override
  public boolean delete(long id) {
    return jdbc.update("delete from todos where id = ?", id) > 0;
  }
}
