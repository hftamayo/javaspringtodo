# Stage 1: Build the application
FROM eclipse-temurin:17 as build
WORKDIR /workspace/app

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Copy the source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17
VOLUME /tmp
ARG JAR_FILE=/workspace/app/target/*.jar
COPY --from=build ${JAR_FILE} jsbtodo.jar
WORKDIR /workspace/app
CMD java \
    -Dspring.config.location=/resources/application-docker.properties \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.profiles.active=docker \
    -jar jsbtodo.jar

#HEALTHCHECK --interval=5s \
#            --timeout=3s \
#            CMD curl -f http://localhost:8080/actuator/health || exit 1
#ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","/jsbtodo.jar"]

#how to run this file:
#docker buildx build --platform linux/amd64,linux/arm64 -t myimage:latest .
#docker run --name jsbtodo -p 8002:8002 \
#-v $(pwd)/src/main/resources/application.properties:/workspace/app/src/main/resources/application.properties \
#-v $(pwd)/src/main/resources/application-docker.properties:/workspace/app/src/main/resources/application-docker.properties \
#hftamayo/jsbtodo:experimental-0.0.1

#docker run --name jsbtodo -p 8002:8002 \
#-v $(pwd)/src/main/resources\:/resources \
#-e JAVA_OPTS=" \
#   -Dspring.config.location=/resources/application-docker.properties" \
#hftamayo/jsbtodo:experimental-0.0.1

#docker run --name jsbtodo -p 8002:8002 -v $(pwd)/src/main/resources/application-docker.properties:/workspace/app/src/main/resources/application-docker.properties hftamayo/jsbtodo:experimental-0.0.1