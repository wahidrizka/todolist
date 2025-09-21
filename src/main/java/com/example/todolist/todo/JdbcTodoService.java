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
@Profile("prod")
class JdbcTodoService implements TodoService {

  private static final RowMapper<TodoResponse> M =
      (resultSet, n) ->
          new TodoResponse(
              resultSet.getLong("id"),
              resultSet.getString("title"),
              resultSet.getBoolean("completed"),
              toInstant(resultSet.getTimestamp("created_at")),
              toInstant(resultSet.getTimestamp("updated_at")));
  private final JdbcTemplate jdbc;

  JdbcTodoService(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  private static Instant toInstant(Timestamp timestamp) {
    return timestamp == null ? null : timestamp.toInstant();
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
  public TodoResponse create(CreateTodoRequest request) {
    return jdbc.queryForObject(
        "insert into todos(title) values (?) "
            + "returning id, title, completed, created_at, updated_at",
        M,
        request.title());
  }

  @Override
  public boolean delete(long id) {
    return jdbc.update("delete from todos where id = ?", id) > 0;
  }

  @Override
  public Optional<TodoResponse> update(long id, UpdateTodoRequest request) {
    // Normalisasi title bila diisi
    String newTitle = request.title();
    if (newTitle != null) {
      newTitle = newTitle.trim();
      if (newTitle.isEmpty()) {
        // anggap invalid -> jangan update title (treat as null)
        newTitle = null;
      }
    }
    Boolean newCompleted = request.completed();

    // Update parsial: COALESCE(nilaiBaru, kolomLama)
    return jdbc
        .query(
            "update todos set "
                + "title = COALESCE(?, title), "
                + "completed = COALESCE(?, completed), "
                + "updated_at = now() "
                + "where id = ? "
                + "returning id, title, completed, created_at, updated_at",
            M,
            newTitle,
            newCompleted,
            id)
        .stream()
        .findFirst();
  }
}
