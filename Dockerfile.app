# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies separately (mejor caché de capas)
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src src

# Build the application with production profile
RUN ./mvnw clean package -DskipTests -Pprod

# Stage 2: Extract layered JAR for faster startup
FROM eclipse-temurin:17-jdk-alpine AS extract
WORKDIR /workspace/app
COPY --from=build /workspace/app/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 3: Run the application
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Add non-root user for security
RUN addgroup --system --gid 1001 appgroup \
    && adduser --system --uid 1001 --ingroup appgroup appuser \
    && mkdir -p /logs /resources \
    && chown -R appuser:appgroup /app /logs /resources

# Copy layered application
COPY --from=extract --chown=appuser:appgroup /workspace/app/dependencies/ ./
COPY --from=extract --chown=appuser:appgroup /workspace/app/spring-boot-loader/ ./
COPY --from=extract --chown=appuser:appgroup /workspace/app/snapshot-dependencies/ ./
COPY --from=extract --chown=appuser:appgroup /workspace/app/application/ ./

# Crear volúmenes para logs y configuración
VOLUME ["/logs", "/resources"]

# Habilitar health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget -q --spider http://localhost:8011/actuator/health || exit 1

EXPOSE 8011

# Cambiar al usuario no-root
USER appuser

# Configurar opciones JVM optimizadas para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+OptimizeStringConcat -XX:+UseStringDeduplication"

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.JarLauncher --spring.config.location=file:/resources/application-docker.properties --spring.profiles.active=docker"]

#how to run this file:
#multiplatform image
#docker buildx build --no-cache --platform linux/amd64,linux/arm64 -t myimage:latest .
#docker buildx build --no-cache --platform linux/amd64,linux/arm64 -t hftamayo/jsbtodo:0.1.3-experimental -f Dockerfile.app .

#specific platform
#docker buildx build --no-cache --platform linux/amd64 -t hftamayo/jsbtodo:0.1.3-experimental -f Dockerfile.app .

#docker run -d --name jsbtodo --network mysqldev_network -p 8011:8011 -v $(pwd)/src/main/resources:/resources hftamayo/jsbtodo:0.1.3-experimental
