FROM gradle:8-jdk21 AS build
WORKDIR /app

RUN apt-get update && apt-get install -y openssl

COPY build.gradle settings.gradle ./

RUN mkdir -p src/main/resources
RUN openssl genpkey -algorithm RSA -out src/main/resources/key.pem
RUN openssl rsa -pubout -in src/main/resources/key.pem -out src/main/resources/key.pub

COPY src ./src
RUN gradle clean build -x test

FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
