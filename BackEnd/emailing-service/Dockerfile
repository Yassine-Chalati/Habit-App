FROM ubuntu

RUN apt update

RUN apt install curl -y

RUN cd /opt &&  /bin/curl -L -O https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz && tar -xvzf jdk-21_linux-x64_bin.tar.gz

ENV PATH="$PATH:/opt/jdk-21.0.5/bin"

ARG JAR_FILE

VOLUME /tmp

COPY target/${JAR_FILE} emailing-service.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/emailing-service.jar" ]