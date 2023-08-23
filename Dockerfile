FROM openjdk:17
COPY target/todo-0.0.1-SNAPSHOT.jar todo001.jar
EXPOSE 8002
ENTRYPOINT ["java", "-jar", "todo001.jar"]

