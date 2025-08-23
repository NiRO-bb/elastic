FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY ./target/*.jar elastic.jar

ENTRYPOINT ["java", "-jar", "elastic.jar"]
