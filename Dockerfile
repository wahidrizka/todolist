# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy POM dulu untuk cache dependency
COPY pom.xml .
# Cache repo Maven di layer terpisah
RUN --mount=type=cache,target=/root/.m2 mvn -q -B dependency:go-offline

# Copy source & build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -B package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# User non-root
RUN useradd -r -u 1001 spring
USER spring

# Copy fat-jar dari stage build
COPY --from=build /app/target/todolist-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
