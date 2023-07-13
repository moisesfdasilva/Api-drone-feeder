FROM maven:3.8-jdk-11

RUN mkdir /project

COPY . /project

WORKDIR /project

RUN mvn clean package -DskipTests

CMD ["java", "-jar", "target/drone.feeder-0.0.1-SNAPSHOT.jar"]