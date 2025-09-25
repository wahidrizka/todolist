package com.example.todolist.api;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Coverage untuk VersionController#get (GET /api/version). Tidak memakai @WebMvcTest agar
 * BuildProperties/GitProperties asli ter-load.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class VersionControllerTest {

  @Autowired MockMvc mvc;

  @Autowired BuildProperties buildProperties;

  @Autowired(required = false)
  GitProperties gitProperties; // opsional; environment CI biasanya menyediakan

  @Test
  @DisplayName("GET /api/version mengembalikan metadata build & git")
  void get_version_shouldReturnBuildAndGitMetadata() throws Exception {
    mvc.perform(get("/api/version"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        // nilai stabil dari BuildProperties
        .andExpect(jsonPath("$.name").value(buildProperties.getName()))
        .andExpect(jsonPath("$.version").value(buildProperties.getVersion()))
        // field waktu & git bisa berubah -> cukup pastikan ada
        .andExpect(jsonPath("$.buildTime", notNullValue()))
        .andExpect(jsonPath("$.now", notNullValue()))
        .andExpect(jsonPath("$.commitId", notNullValue()))
        .andExpect(jsonPath("$.branch", notNullValue()))
        .andExpect(jsonPath("$.commitTime", notNullValue()));
  }
}
