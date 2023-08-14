FROM openjdk:17
COPY target/todo.war todo.war
ENTRYPOINT["java", "-jar", "/todo.war"]
