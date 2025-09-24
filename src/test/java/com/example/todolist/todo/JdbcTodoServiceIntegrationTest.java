package com.example.todolist.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Integration test untuk JdbcTodoService menggunakan PostgreSQL nyata via Testcontainers.
 * Menggunakan profil "prod" agar bean JdbcTodoService yang aktif.
 */
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("prod")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@org.springframework.context.annotation.Import(JdbcTodoServiceIntegrationTest.TestDbConfig.class)
class JdbcTodoServiceIntegrationTest {
  @Container
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15-alpine");

  @Autowired JdbcTemplate jdbcTemplate;
  @Autowired TodoService todoService;

  @BeforeEach
  void clean() {
    // Bersihkan data sebelum tiap test; abaikan jika tabel belum ada (akan ada setelah Flyway)
    jdbcTemplate.execute("truncate table todos restart identity cascade");
  }

  @Test
  @DisplayName("findAll dengan filter q & completed berjalan benar di PostgreSQL")
  void findAll_withFilters_shouldWork() {
    // Arrange: seed data
    jdbcTemplate.update("insert into todos (title, completed) values (?,?)", "Alpha task", false);
    jdbcTemplate.update("insert into todos (title, completed) values (?,?)", "Beta", true);
    jdbcTemplate.update("insert into todos (title, completed) values (?,?)", "Gamma", false);

    // Act 1: q = 'a', completed = false
    PageResult<TodoResponse> page1 = todoService.findAll("a", false, 0, 10);

    // Assert 1: hanya Alpha & Gamma
    assertEquals(2, page1.total());
    assertEquals(2, page1.items().size());

    // Act 2: q = null, completed = true
    PageResult<TodoResponse> page2 = todoService.findAll(null, true, 0, 10);

    // Assert 2: hanya Beta
    assertEquals(1, page2.total());
    assertEquals(1, page2.items().size());
  }

  @Test
  @DisplayName("findAll dengan pagination limit/offset konsisten")
  void findAll_pagination_shouldWork() {
    // Arrange: 25 rows
    for (int i = 1; i <= 25; i++) {
      jdbcTemplate.update("insert into todos (title, completed) values (?,?)", "Task " + i, false);
    }

    // page=0,size=10
    PageResult<TodoResponse> p0 = todoService.findAll(null, null, 0, 10);
    assertEquals(25, p0.total());
    assertEquals(10, p0.items().size());
    assertEquals("Task 1", p0.items().get(0).title());

    // page=1,size=10
    PageResult<TodoResponse> p1 = todoService.findAll(null, null, 1, 10);
    assertEquals(25, p1.total());
    assertEquals(10, p1.items().size());
    assertEquals("Task 11", p1.items().get(0).title());

    // page=2,size=10 (sisa 5)
    PageResult<TodoResponse> p2 = todoService.findAll(null, null, 2, 10);
    assertEquals(25, p2.total());
    assertEquals(5, p2.items().size());
    assertEquals("Task 21", p2.items().get(0).title());
  }

  @org.springframework.boot.test.context.TestConfiguration
  static class TestDbConfig {

    @org.springframework.context.annotation.Bean
    javax.sql.DataSource dataSource() {
      org.springframework.jdbc.datasource.DriverManagerDataSource ds =
          new org.springframework.jdbc.datasource.DriverManagerDataSource();
      ds.setDriverClassName("org.postgresql.Driver");
      ds.setUrl(POSTGRES.getJdbcUrl());
      ds.setUsername(POSTGRES.getUsername());
      ds.setPassword(POSTGRES.getPassword());
      return ds;
    }

    @org.springframework.context.annotation.Bean
    org.springframework.jdbc.core.JdbcTemplate jdbcTemplate(javax.sql.DataSource ds) {
      return new org.springframework.jdbc.core.JdbcTemplate(ds);
    }

    // Jalankan migrasi agar tabel tersedia
    @org.springframework.context.annotation.Bean(initMethod = "migrate")
    org.flywaydb.core.Flyway flyway(javax.sql.DataSource ds) {
      return org.flywaydb.core.Flyway.configure()
          .dataSource(ds)
          .locations("classpath:db/migration")
          .load();
    }
  }
}
