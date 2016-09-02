FROM openjdk:7

ADD build/libs/latest.jar latest.jar

CMD java -jar latest.jar server configuration.yaml

EXPOSE 8080

EXPOSE 8081