package com.example.todolist.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.http.ResponseEntity;
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

    public VersionController(BuildProperties build, ObjectProvider<GitProperties> gitProvider) {
        this.build = build;
        this.git = gitProvider.getIfAvailable();
    }

    @GetMapping
    @Operation(summary = "Cek versi aplikasi")
    public ResponseEntity<Map<String, Object>> get() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("name", build.getName());
        body.put("version", build.getVersion());
        body.put("buildTime", build.getTime());       // Instant
        if (git != null) {
            body.put("commitId", git.getShortCommitId());
            body.put("branch", git.getBranch());
            body.put("commitTime", git.getInstant("commit.time"));
        }
        body.put("now", Instant.now());
        return ResponseEntity.ok(body);
    }
}
