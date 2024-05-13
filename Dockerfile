FROM openjdk:17-alpine
WORKDIR /deploy
COPY /target/backend-khoa-luan.jar backend-khoa-luan.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","backend-khoa-luan.jar"]