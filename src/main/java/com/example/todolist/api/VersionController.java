package com.example.todolist.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/version")
@Tag(name = "Version", description = "Informasi versi aplikasi")
public class VersionController {

    private final BuildProperties build;
    private final GitProperties git; // opsional

    public VersionController(BuildProperties build,
                             ObjectProvider<GitProperties> gitProvider) {
        this.build = build;
        this.git = gitProvider.getIfAvailable(); // bisa null kalau git.properties tidak ada
    }

    @GetMapping
    @Operation(summary = "Cek versi aplikasi")
    public ResponseEntity<Map<String, Object>> get() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("name", build.getName());
        body.put("version", build.getVersion());
        body.put("buildTime", build.getTime()); // Instant dari build-info

        // Ambil metadata git: prefer GitProperties; fallback ke ENV (GIT_SHA/REF) yang diinject di Docker build
        String commitId = (git != null) ? git.getShortCommitId() : System.getenv("GIT_SHA");
        String branch = (git != null) ? git.getBranch() : System.getenv("GIT_REF");

        if (StringUtils.hasText(commitId)) {
            body.put("commitId", commitId);
        }
        if (StringUtils.hasText(branch)) {
            body.put("branch", branch);
        }
        if (git != null) {
            body.put("commitTime", git.getInstant("commit.time"));
        }

        body.put("now", Instant.now());
        return ResponseEntity.ok(body);
    }
}
