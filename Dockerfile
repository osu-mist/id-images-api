FROM openjdk:7

ADD build/libs/latest.jar latest.jar

CMD ["java", "-jar", "latest.jar", "server", "configuration.yaml"]

USER nobody:nogroup

EXPOSE 8080

EXPOSE 8081