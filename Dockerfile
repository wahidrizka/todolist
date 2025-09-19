# syntax=docker/dockerfile:1

########################
# Build stage (Maven)
########################
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy sources
COPY pom.xml .
COPY src ./src

# Versi & metadata dari workflow
ARG APP_VERSION=0.0.1-SNAPSHOT
ARG GIT_SHA=local
ARG GIT_REF=local
ENV GIT_SHA=$GIT_SHA GIT_REF=$GIT_REF

# Build jar dengan versi sesuai REVISION (tanpa test)
RUN mvn -B -ntp -DskipTests -Drevision=${APP_VERSION} package

########################
# Runtime stage (JRE)
########################
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Teruskan metadata ke runtime untuk /api/version fallback
ARG GIT_SHA
ARG GIT_REF
ENV GIT_SHA=${GIT_SHA}
ENV GIT_REF=${GIT_REF}

# Copy jar hasil build
COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
