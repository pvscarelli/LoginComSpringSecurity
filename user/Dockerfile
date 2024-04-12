FROM openjdk

WORKDIR /user

COPY target/user-0.0.1-SNAPSHOT.jar user-app.jar

ENTRYPOINT [ "java", "-jar", "user-app.jar" ]