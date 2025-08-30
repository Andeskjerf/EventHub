FROM docker.io/gradle:8-jdk21 AS build
WORKDIR /app

RUN apt-get update && apt-get install -y openssl

COPY build.gradle settings.gradle ./

RUN mkdir -p src/main/resources
# we generate the keys used for JWT...
RUN openssl genpkey -algorithm RSA -out src/main/resources/key.pem
RUN openssl rsa -pubout -in src/main/resources/key.pem -out src/main/resources/key.pub

# ..then we overwrite them using the keys that already exist for development!
# feature? bug? is it a good idea to create new keys every time anyway?
COPY src ./src
RUN gradle clean build -x test

FROM docker.io/openjdk:21-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
