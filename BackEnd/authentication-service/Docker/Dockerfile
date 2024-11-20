FROM openjdk:21

ARG JAR_FILE

COPY ../target/${JAR_FILE} authentication-service.jar

ENTRYPOINT [ "java", "-jar", "/authentication-service.jar" ]