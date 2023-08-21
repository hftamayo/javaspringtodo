FROM openjdk:17
COPY target/todo-0.0.1-SNAPSHOT.jar todo001.jar
COPY enviro.properties /enviro.properties
ENV JAVA_OPTS=""
CMD java \
	-Dspring.config.location=/enviro.properties \
	-Djava.security.egd=file:/dev/ ./random \
	$JAVA_OPTS \
	-jar /todo001.jar

