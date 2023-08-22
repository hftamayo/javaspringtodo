FROM openjdk:17
COPY target/todo-0.0.1-SNAPSHOT.jar todo001.jar
COPY enviro.properties /enviro.properties
ENV JAVA_OPTS=""
CMD java \
	-jar todo001.jar \
	--spring.config.location=file:enviro.properties \
	$JAVA_OPTS

