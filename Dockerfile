FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build target/backend-khoa-luan-0.0.1-SNAPSHOT.jar backend-khoa-luan.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","backend-khoa-luan.jar"]