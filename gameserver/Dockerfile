FROM maven:3-amazoncorretto-21

WORKDIR /app

RUN yum install -y curl

COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src ./src

CMD ["mvn", "compile", "exec:java"]
