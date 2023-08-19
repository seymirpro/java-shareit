FROM amazoncorretto:11
COPY target/*.jar shareit-server.jar
RUN env
ENTRYPOINT ["java","-jar","/shareit-server.jar"]