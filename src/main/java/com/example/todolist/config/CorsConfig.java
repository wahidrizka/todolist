package com.example.todolist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("!prod") // aktif di default/dev/test; non-aktif di prod
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOriginPatterns("*")
        .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS", "HEAD")
        .allowedHeaders("*")
        .exposedHeaders("Location")
        .allowCredentials(false)
        .maxAge(3600);
  }
}
