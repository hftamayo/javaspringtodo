# Stage 1: Build the application
FROM eclipse-temurin:17 as build
WORKDIR /workspace/app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies separately (better layer caching)
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17
VOLUME /tmp
WORKDIR /workspace/app
ARG JAR_FILE=/workspace/app/target/*.jar
COPY --from=build ${JAR_FILE} jsbtodo.jar
#HEALTHCHECK --interval=5s \
#            --timeout=3s \
#            CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8011

CMD ["java", \
     "-Dspring.config.location=/resources/application-docker.properties", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-Dspring.profiles.active=docker", \
     "-jar", "jsbtodo.jar"]
#ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/jsbtodo.jar"]

#how to run this file:
#multiplatform image
#docker buildx build --no-cache --platform linux/amd64,linux/arm64 -t myimage:latest .
#docker buildx build --no-cache --platform linux/amd64,linux/arm64 -t hftamayo/jsbtodo:0.1.3-experimental -f Dockerfile.app .

#specific platform
#docker buildx build --no-cache --platform linux/amd64 -t hftamayo/jsbtodo:0.1.3-experimental -f Dockerfile.app .

#docker run --name jsbtodo --network mysqldev_network -p 8011:8011 -v $(pwd)/src/main/resources:/resources hftamayo/jsbtodo:0.1.3-experimental
