FROM openjdk:8
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ["chmod", "+x", "mvnw"]
RUN ./mvnw install -DskipTests
RUN cp target/ChessApi-*.jar /app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]