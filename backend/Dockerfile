# ---- Etapa 1: build ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN chmod +x gradlew
COPY src src
RUN ./gradlew --no-daemon shadowJar

# ---- Etapa 2: runtime ----
FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app
RUN useradd -ms /bin/bash appuser
COPY --from=build /app/build/libs/app.jar app.jar
RUN mkdir -p /app/data && chown -R appuser:appuser /app
USER appuser
EXPOSE 7070 9092
VOLUME ["/app/data"]
ENTRYPOINT ["java", "-jar", "app.jar"]
