# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
COPY src ./src

ARG APP_VERSION=0.0.1-SNAPSHOT
ARG GIT_SHA=local
ARG GIT_REF=local
ENV GIT_SHA=$GIT_SHA GIT_REF=$GIT_REF

# Penting: gunakan APP_VERSION, bukan REVISION
RUN SANITIZED="$(echo "${APP_VERSION}" | sed 's/^v//')" \
    && mvn -B -ntp -DskipTests -Drevision="${SANITIZED}" package

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ARG GIT_SHA
ARG GIT_REF
ENV GIT_SHA=${GIT_SHA} GIT_REF=${GIT_REF}
COPY --from=build /workspace/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
