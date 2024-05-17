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
ENTRYPOINT ["java","-jar","/jsbtodo.jar"]

#how to run this file:
#docker buildx build --platform linux/amd64,linux/arm64 -t myimage:latest .
#docker run -p 8080:8080 -v $(pwd)/src/main/resources/<property file>:/workspace/app/src/main/resources/<property file> myimage:latest