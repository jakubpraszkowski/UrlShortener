FROM maven:3.9-eclipse-temurin-17-focal AS builder

WORKDIR /build

COPY pom.xml .

COPY src/main/resources/application.properties src/main/resources/

RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -DskipTests

COPY src/ src/

RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-focal

WORKDIR /app

ARG APP_USER=appuser
ARG APP_USER_GROUP=appgroup

COPY --from=builder /build/target/*.jar app.jar

COPY --from=builder /build/src/main/resources /build/src/main/

RUN chown -R ${APP_USER}:${APP_USER_GROUP} /app

USER ${APP_USER}

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "java -jar app.jar"]