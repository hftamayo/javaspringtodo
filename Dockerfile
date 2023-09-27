#################### MAVEN BUILD STAGE ####################

FROM maven:3.8.5-openjdk-17 AS build
#directorio de trabajo dentro del contenedor
ENV MVN_HOME = /opt/maventodo
ENV APP_HOME = /opt/jsbtodo

RUN mkdir $MVN_HOME
RUN mkdir $APP_HOME

WORKDIR $MVN_HOME
COPY src $MVN_HOME
COPY pom.xml $MVN_HOME
RUN mvn clean install

################# JDK RUN STAGE ################### 
FROM openjdk:17
WORKDIR $APP_HOME
COPY --from=build $MVN_HOME/target/todo-0.0.1-SNAPSHOT.jar $APP_HOME/todo001.jar
ENTRYPOINT ["java", "-jar", "todo001.jar"]


