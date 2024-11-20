FROM openjdk:21

ARG JAR_FILE

COPY target/${JAR_FILE} registry-service.jar

# RUN export PORT_EUREKA=${PORT_EUREKA} && \
#     export PROFILES=${PROFILES} && \
#     export HOSTNAME_EUREKA=${HOSTNAME_EUREKA} && \
#     export USERNAME_EUREKA=${USERNAME_EUREKA} && \
#     export PASSWORD_EUREKA=${PASSWORD_EUREKA} && \
#     export GIT_BRANCH=${GIT_BRANCH} && \
#     export DNS=${HOSTNAME_EUREKA}:${PORT_EUREKA} && \
#     export HTTP=${HTTP} && \
#     export BASIC_AUTH_EUREKA=${USERNAME_EUREKA}:${PASSWORD_EUREKA}@

ENTRYPOINT ["java", "-jar", "/registry-service.jar" ]