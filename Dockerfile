FROM openjdk:11-jre-slim

WORKDIR /app

ENV APP_JVM_OPTS ""
ENV APP_PROFILE ""

COPY target/*.jar ./app.jar

# application
EXPOSE 8080

CMD exec java $APP_JVM_OPTS  -Duser.timezone=UTC -jar app.jar $APP_PROFILE