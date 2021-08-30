FROM openjdk:8
WORKDIR /
COPY target/ .
EXPOSE 8080
CMD java -jar *.jar