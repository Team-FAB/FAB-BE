FROM openjdk:11
ARG JAR_FILE=build/libs/BangGabGo.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=${USE_PROFILE}","-jar","app.jar"]