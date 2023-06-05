FROM openjdk:11.0.16 as builder

WORKDIR /app/springk-usuarios

COPY ./pom.xml /app
COPY ./springk-usuarios/.mvn ./.mvn
COPY ./springk-usuarios/mvnw .
COPY ./springk-usuarios/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./springk-usuarios/src ./src

RUN ./mvnw clean package -DskipTests


FROM openjdk:11.0.16
WORKDIR /app
RUN mkdir ./logs
COPY --from=builder /app/springk-usuarios/target/springk-usuarios-0.0.1-SNAPSHOT.jar .
EXPOSE 8001

ENTRYPOINT ["java","-jar","springk-usuarios-0.0.1-SNAPSHOT.jar"]