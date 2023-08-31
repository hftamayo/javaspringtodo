FROM openjdk:17

WORKDIR /todo
#RUN mvn clean install
COPY target/todo-0.0.1-SNAPSHOT.jar todo001.jar
ENTRYPOINT ["java", "-jar", "todo001.jar"]

#alternative method: compile inside the docker, it requires internet access
#COPY . .
#RUN mvn clean install
#CMD mvn spring-boot:run

