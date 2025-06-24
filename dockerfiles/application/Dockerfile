#################### MAVEN BUILD STAGE ####################

FROM maven:3.8.5-openjdk-17 AS build
#directorio de trabajo dentro del contenedor
#ENV APP_HOME = /opt/jsbtodo
RUN mkdir -p /opt/jsbtodo; exit 0

WORKDIR /opt/jsbtodo
COPY src ./src
COPY pom.xml .
RUN mvn clean package

################# JDK RUN STAGE ################### 
FROM openjdk:17
WORKDIR /opt/jsbtodo
COPY --from=build /opt/jsbtodo/target/todo-0.0.1-SNAPSHOT.jar ./todo001.jar
ENTRYPOINT ["java", "-jar", "./todo001.jar"]


