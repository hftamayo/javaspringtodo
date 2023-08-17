FROM openjdk:17
COPY out/artifacts/todo_jar/todo.jar todo.jar
ENTRYPOINT ["java", "-jar", "/todo.jar"]
