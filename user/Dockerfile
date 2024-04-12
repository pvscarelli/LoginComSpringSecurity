FROM openjdk

WORKDIR /home/pvscarelli/Documents/User/user

COPY target/user-0.0.1-SNAPSHOT.jar user-app.jar

ENTRYPOINT [ "java", "-jar", "user-app.jar" ]